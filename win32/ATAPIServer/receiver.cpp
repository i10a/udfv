//

#include <stdio.h>
#include <string.h>

#include <Winsock2.h>
#include <Ws2tcpip.h>

#include "receiver.h"

#include "dbg.h"


#define PORT          12345 // �󂯕t����|�[�g
#define CMD_BUFSZ     10    // cmd_buf �̃T�C�Y
#define CMDSTR_MAX_SZ (2048*64)   // ��M�R�}���h������̒���

#define PACKETSZ (2048*64)       // ��M�p�P�b�g�T�C�Y
#define RECVRINGSZ (PACKETSZ*10) // ��M�p�P�b�g�̃����O�o�b�t�@

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



// ������
// 1 ����
// 0 ���s
int Receiver::Init(void)
{
	recv_buf = new Ring(sizeof (unsigned char) * RECVRINGSZ);
	cmd_buf = new Ring(sizeof (CmdStruct) * CMD_BUFSZ);

	// �\�P�b�g�쐬
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

	// ���C�����[�v
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

		// �^�C���A�E�g
		if (0 == ret) {
//			dbg("Receiver::select timeout\n");
			continue;
		}

		// �G���[
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
				// send(�T�[�o�[�͖Z����);
			}
		}

		// �f�[�^��M
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

			// �\�P�b�g�����
			if (0 == ret) {
				closesocket(recvsock);
				accept_flag = false;
				dbg("close socket\n");
			}

//			msgdump((unsigned char *)buf, ret, false);

			// ��M�f�[�^�̏���
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


/// ��M�f�[�^�̏���
/**
*/
void Receiver::recv_data_dispatch(unsigned char *buf, int sz)
{
	int setbuf_result;

	do {
		if (sz) {
			// �����M�����f�[�^�������O�o�b�t�@�ɓo�^
			setbuf_result = recv_buf->SetData((unsigned char*)buf, sz);
			dbg("recv data %d\n", sz);

			// �ۑ��ɐ���������
			if (setbuf_result) {
				dbg("recv_buf->SetData ����\n");
				sz = 0;
			}
		}

		// �o�^�����o�b�t�@�̃T�C�Y���擾
		int bufsz = recv_buf->GetSize();

		// �����O�o�b�t�@����
		if (bufsz <= 0) {
			// �����O�o�b�t�@�ɂ܂��o�^���Ă��Ȃ��Ȃ�
			if (!setbuf_result) {
				dbg("not SetData, continue\n");
				continue;
			}
			dbg("bufsz == 0 and SetData, end\n");
			return;
		}

		// �o�b�t�@�̏I�[���k�������ŏI�����Ă��邩�`�F�b�N
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

		// �o�b�t�@�̏I�[�܂Ńk���������Ȃ��Ƃ������Ƃ͈ȉ��̂ǂ��炩
		// - �܂���M���ĂȂ�����������
		// - �p�P�b�g���傫������(�p�P�b�g�����Ă���)
		if (i == bufsz && !nullchar) {
			// �p�P�b�g���傫������ꍇ��
			if (CMDSTR_MAX_SZ <= bufsz) {
				dbg("�p�P�b�g���傫������\n");

				// �����镪��S���폜
				recv_buf->DeleteData(bufsz);
			}

			// �����M�����f�[�^�������O�o�b�t�@�ɓo�^���I����Ă��Ȃ��Ȃ�
			if (!setbuf_result) {
				dbg("�܂��ǂݐ؂��ĂȂ�\n");
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

		// �P���b�Z�[�W���폜
		recv_buf->DeleteData(i + 1);

		// debug
//		dbg("cmd_str dump\n");
//		msgdump((unsigned char *)cmd_str, i+1);
//		dbg("---\n");

		// ��M��������
		//char *word[CMDSTR_MAX_SZ]; // 1/2 �ő��v�Ȃ͂�������
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
			// tmp �� main.cpp recv_image_buf() �� delete ����
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

		// �ȉ��R�}���h����
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
	���͕�������ŏ��̋󔒂ŕ��������T�C�Y2�̔z���Ԃ��܂�
	@param in   ���͕�����
	@param out  ���͕�����𕪗����A�e�P��̐擪�|�C���^���i�[�����z��
	@return out �z��̃T�C�Y
	��)
		in :   "abc def  ghi\000" (�A�h���X 0x0000 ����J�n)
		out:   [0x0000, 0x0004]
		size : 3
		in �̍ŏ��̘A������󔒕����̓k�������ɕϊ�����܂�
*/
static int StringSplit(char *in, char **out)
{
	int len = strlen(in);
	int i, j;
	bool space = true;

	// �s���̋󔒂��Ƃ΂�
	for (i = 0; i < len; i++) {
		if (!isspace(in[i])) {
			break;
		}
	}

	for (j = 0; i < len; i++) {
		// ��
		if (isspace(in[i])) {
			in[i] = 0;
			space = true;
		}
		// ���
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










