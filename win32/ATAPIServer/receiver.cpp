//

#include <stdio.h>
#include <string.h>

#include <Winsock2.h>
#include <Ws2tcpip.h>

#include "receiver.h"

#include "dbg.h"


#define PORT          12345 // 受け付けるポート
#define CMD_BUFSZ     10    // cmd_buf のサイズ
#define CMDSTR_MAX_SZ (2048*64)   // 受信コマンド文字列の長さ

#define PACKETSZ (2048*64)       // 受信パケットサイズ
#define RECVRINGSZ (PACKETSZ*10) // 受信パケットのリングバッファ

static void msgdump(unsigned char *, int, bool binary = false);
static int StringSplit(char *in, char **out);



using namespace hs;

Receiver::Receiver(void)
{
	fd = 0;
	memset((void *)&addr, 0, sizeof addr);
	error_no = 0;
	recv_buf = NULL;
	cmd_buf = NULL;
	accept_flag = false;
	cmd_event.SetEventName("cmd_event");

};

Receiver::~Receiver(void)
{
	if (recv_buf) {
		delete recv_buf;
	}

	if (cmd_buf) {
		delete cmd_buf;
	}
}



// 初期化
// 1 成功
// 0 失敗
int Receiver::Init(void)
{
	recv_buf = new Ring(sizeof (unsigned char) * RECVRINGSZ);
	cmd_buf = new Ring(sizeof (CmdStruct) * CMD_BUFSZ);

	// ソケット作成
	WSADATA dat;
	int ret = WSAStartup(MAKEWORD(1, 1), &dat);
	fd = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
	if (0 > fd) {
		return 0;
	}

	// REUSE
	unsigned int yes = 1;
	ret = setsockopt(fd, SOL_SOCKET, SO_REUSEADDR,
	                 (char *)&yes, sizeof (yes));
	if (0 > ret) {
		error_no = WSAGetLastError();
		return 0;
	}

	//
	struct sockaddr_in recv_addr;
	memset(&recv_addr, 0, sizeof (recv_addr));
	recv_addr.sin_family = AF_INET;
	recv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
	recv_addr.sin_port = htons(PORT);

	// bind
	ret = bind(fd, (struct sockaddr *)&recv_addr, sizeof (recv_addr));
	if (0 > ret) {
		error_no = WSAGetLastError();
		return 0;
	}

	return 1;
}


void Receiver::ThreadProc(void)
{
	State st = state.Get();
	if (ST_RUN == st) {
		return;
	}

	state.Set(ST_RUN);

//	int i;
	timeval timeout = {1, 0};

	if (0 != listen(fd, 0)) {
		error_no = WSAGetLastError();
		return;
	}

	int addrlen = sizeof (addr);
	fd_set fdset;
	memset((void *)&fdset, 0, sizeof (fdset));

	bool error_flag = false;
	accept_flag = false;

	// メインループ
	while (1) {
		if (M_STOP == message) {
			message = M_NULL;
			break;
		}

		FD_ZERO(&fdset);
		if (accept_flag) {
			FD_SET(recvsock, &fdset);
		}
		FD_SET(fd, &fdset);


		int ret = select(1, &fdset, NULL, NULL, &timeout);

		// タイムアウト
		if (0 == ret) {
//			dbg("Receiver::select timeout\n");
			continue;
		}

		// エラー
		if (0 > ret) {
			error_no = WSAGetLastError();
			error_flag = true;
			dbg("error in %d\n", __LINE__);
			break;
		}

		// accept
		if (FD_ISSET(fd, &fdset)) {
			SOCKET sock = accept(fd, (struct sockaddr *)&addr, &addrlen);
			if (INVALID_SOCKET == sock) {
				error_no = WSAGetLastError();
				error_flag = true;
				dbg("error in %d\n", __LINE__);
				break;
			}

			if (!accept_flag) {
				recvsock = sock;
				accept_flag = true;
			}
			else {
				// send(サーバーは忙しい);
			}
		}

		// データ受信
		if (accept_flag && FD_ISSET(recvsock, &fdset)) {
			dbg("RECEIVER ACCEPT\n");
//#define PACKETSZ 256
			unsigned char buf[PACKETSZ] = {0};
			ret = recvfrom(recvsock, (char *)buf, PACKETSZ, 0,
			               (struct sockaddr *)&addr, &addrlen);
			if (0 > ret) {
				error_no = WSAGetLastError();
				if (WSAECONNRESET == error_no) {
					ret = 0;
				}
				else {
					error_flag = true;
//					dbg("ret = %d\n", ret);
//					dbg("error_no = %d\n", error_no);
//					dbg("error in %d\n", __LINE__);
					break;
				}
			}

			// ソケットを閉じる
			if (0 == ret) {
				closesocket(recvsock);
				accept_flag = false;
				dbg("close socket\n");
			}

//			msgdump((unsigned char *)buf, ret, false);

			// 受信データの処理
			recv_data_dispatch(buf, ret);
		}
	}

	return;
}


DWORD Receiver::WaitEvent(int t)
{
	cmd_event.ResetEvent();
	return cmd_event.Wait(t);
}


Receiver::CMD_TYPE Receiver::GetCommand(_int64 &num, char *str, char **ptr)
{
	CmdStruct cmd;
	int ret = cmd_buf->GetData(&cmd, sizeof (cmd));
	if (!ret) {
		dbg("GetCommand error\n");
		return CMD_ERROR;
	}

	cmd_buf->DeleteData(sizeof (cmd));
	num = cmd.int_val;
	strncpy(str, cmd.str, sizeof (cmd.str));
	*ptr = cmd.ptr;

	return cmd.type;
}


/// 受信データの処理
/**
*/
void Receiver::recv_data_dispatch(unsigned char *buf, int sz)
{
	int setbuf_result;

	do {
		if (sz) {
			// 今回受信したデータをリングバッファに登録
			setbuf_result = recv_buf->SetData((unsigned char*)buf, sz);
			dbg("recv data %d\n", sz);

			// 保存に成功したら
			if (setbuf_result) {
				dbg("recv_buf->SetData 成功\n");
				sz = 0;
			}
		}

		// 登録したバッファのサイズを取得
		int bufsz = recv_buf->GetSize();

		// リングバッファが空
		if (bufsz <= 0) {
			// リングバッファにまだ登録していないなら
			if (!setbuf_result) {
				dbg("not SetData, continue\n");
				continue;
			}
			dbg("bufsz == 0 and SetData, end\n");
			return;
		}

		// バッファの終端がヌル文字で終了しているかチェック
		char *cmd_str = (char *)malloc(bufsz);
		int ret = recv_buf->GetData(cmd_str, bufsz);
		int i;
		bool nullchar = false;
		for (i = 0; i < bufsz; i++) {
			if (cmd_str[i] == '\0') {
				nullchar = true;
				dbg("found null char\n");
				break;
			}
		}

		// バッファの終端までヌル文字がないということは以下のどちらか
		// - まだ受信してない部分がある
		// - パケットが大きすぎる(パケットが壊れている)
		if (i == bufsz && !nullchar) {
			// パケットが大きすぎる場合は
			if (CMDSTR_MAX_SZ <= bufsz) {
				dbg("パケットが大きすぎる\n");

				// 今ある分を全部削除
				recv_buf->DeleteData(bufsz);
			}

			// 今回受信したデータをリングバッファに登録し終わっていないなら
			if (!setbuf_result) {
				dbg("まだ読み切ってない\n");
				free(cmd_str);
				continue;
			}
			dbg("not null receive\n");
			free(cmd_str);
			return;
		}
		else {
			dbg("OK\n");
		}

		// １メッセージ分削除
		recv_buf->DeleteData(i + 1);

		// debug
//		dbg("cmd_str dump\n");
//		msgdump((unsigned char *)cmd_str, i+1);
//		dbg("---\n");

		// 受信文字列解析
		//char *word[CMDSTR_MAX_SZ]; // 1/2 で大丈夫なはずだけど
		char *word[2];
		int wnum = StringSplit(cmd_str, word);
//		dbg("wnum = %d\n", wnum);
		if (wnum <= 0) {
			free(cmd_str);
			continue;
		}
//		dbg("Receriver::receiver_cmd_dispatch : %d : %s\n", wnum, word[0]);

		// READSECTOR
		if (0 == strcmp(word[0], "READSECTOR")) {
			dbg("read sector\n");
			// analyze_str();
			CmdStruct cmd(READSECTOR, _atoi64(word[1]), NULL, NULL);
			set_cmd_buf(&cmd);
		}
		// READINFO
		else if (0 == strcmp(word[0], "READINFO")) {
			dbg("read info\n");
			CmdStruct cmd(READINFO, 0, NULL, NULL);
			set_cmd_buf(&cmd);
		}

		// OPEN
		else if (0 == strcmp(word[0], "OPEN")) {
			dbg("open drive\n");
			CmdStruct cmd(OPEN, 0, word[1], NULL);
			set_cmd_buf(&cmd);
		}

		// WRITEIMAGE
		else if (0 == strcmp(word[0], "WRITEIMAGE")) {
			dbg("recv write image\n");
			CmdStruct cmd(WRITEIMAGE, _atoi64(word[1]), NULL, NULL);
			set_cmd_buf(&cmd);
		}

		// WRITE
		else if (0 == strcmp(word[0], "WRITE")) {
			dbg("write\n");
			// tmp は main.cpp recv_image_buf() で delete する
			char *tmp = new char [strlen(word[1]) + 1];
//			dbg("allocate %X\n", tmp);
			memcpy(tmp, word[1], strlen(word[1]) + 1);
			CmdStruct cmd(WRITE, strlen(word[1]), NULL, tmp);
			set_cmd_buf(&cmd);
		}

		else if (0 == strcmp(word[0], "WRITETEST")) {
			dbg("set_cmd_buf(%s) %s\n", word[0], word[1]);
			dbg("sz = %I64d\n", _atoi64(word[1]));


			CmdStruct cmd(WRITETEST, _atoi64(word[1]), NULL, NULL);
			set_cmd_buf(&cmd);
		}

		// 以下コマンド続く
		else {
			dbg("invalid command \n");
		}
//		dbg(">>\n");

		free(cmd_str);
	} while (false == setbuf_result);
//	dbg("end recv_data_dispatch\n");
}


int Receiver::set_cmd_buf(CmdStruct *cmd)
{
	int ret;
	do {
		ret = cmd_buf->SetData(cmd, sizeof (CmdStruct));
		if (!ret) {
			Sleep(1000);
		}
	} while (!ret);
//	dbg("cmd_event.SetEvent()\n");

	while (true) {
		if (cmd_event.GetSignalState()) {
//			dbg("sleep\n");
			Sleep(10);
		}
		else {
			break;
		}
	}
	cmd_event.SetEvent();

	return ret;
}



int Receiver::send_result(unsigned char *buf, int sz)
{
//	dbg("send_result wait\n");
	send_cs.Enter();
//	dbg("send_result enter\n");

	bool result = true;

	unsigned char *out = new unsigned char [sz + 1];
	memcpy(out, buf, sz + 1);
	out[sz] = 0;
//	msgdump(out, sz + 1);
//	dbg("\nsend size = %d\n", sz + 1);

	if (accept_flag) {
		int ret = sendto(recvsock, (char *)out, sz + 1, 0,
		                 (struct sockaddr *)&addr, sizeof (addr));
	}
	else {
		result = false;
	}

	delete [] out;

	send_cs.Leave();
//	dbg("send_result leave\n");

	return result;
}


static void msgdump(unsigned char *buf, int sz, bool binary)
{
//	dbg(">msg dump\n");
	int i;
	for (i = 0; i < sz; i++) {
		if (isprint(buf[i]) && binary == false) {
			dbg("%c", buf[i]);
		}
		else {
			dbg("[%02X]", (unsigned int)buf[i]);
		}
	}
	dbg("\n");
//	dbg("<msg dump\n");
}

/**
	入力文字列を最初の空白で分離したサイズ2の配列を返します
	@param in   入力文字列
	@param out  入力文字列を分離し、各単語の先頭ポインタを格納した配列
	@return out 配列のサイズ
	例)
		in :   "abc def  ghi\000" (アドレス 0x0000 から開始)
		out:   [0x0000, 0x0004]
		size : 3
		in の最初の連続する空白文字はヌル文字に変換されます
*/
static int StringSplit(char *in, char **out)
{
	int len = strlen(in);
	int i, j;
	bool space = true;

	// 行頭の空白をとばす
	for (i = 0; i < len; i++) {
		if (!isspace(in[i])) {
			break;
		}
	}

	for (j = 0; i < len; i++) {
		// 空白
		if (isspace(in[i])) {
			in[i] = 0;
			space = true;
		}
		// 非空白
		else if (space) {
			out[j++] = &in[i];
			space = false;
			if (j >= 2) {
				break;
			}
		}
	}

	return j;
}










