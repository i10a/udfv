//

#include <windows.h>
#include <stdio.h>
#include <stddef.h>

#include "hs_atapi.h"
#include "receiver.h"

// for debug
#include "dbg.h"

//
#define SECTOR_SZ 2048

//
#define ERR_CREATE_FILE_FAILED 0x0000
#define ERR_TEST_ERR           0x0001
#define ERR_NOT_DVD            0x0002

// 固定メッセージ
#define ERR_RESULT     ((unsigned char*)"ERROR")
#define OK_RESULT      ((unsigned char *)"OK")

//
using namespace hs;


//
int test_disc(HANDLE);
int read_buffer(HANDLE, int, int);
int write_image(HANDLE dev, char *img_name);
int write_speed_check(HANDLE dev);
void recv_image_buf(void *ptr);
void recv_image_buf2(void *ptr);


static char *rw_labels[][2] = {
	{"DVD-RAM READ", "DVD-RAM WRITE"}, // DVD_RAM
	{"DVD-R READ",   "DVD-R WRITE"},   // DVD_R
	{"DVD-RW READ",  "DVD-RW WRITE"},  // DVD_RW
	{"CD-R READ",    "CD-R WRITE"},    // CD_R
	{"CD-RW READ",   "CD-RW WRITE"},   // CD_RW
	{"DVD+RW READ",  "DVD+RW WRITE"},  // DVD_PLUS_RW
	{"DVD+R READ",   "DVD+R WRITE"},   // DVD_PLUS_R
	{"",             "TEST WRITE"},
};

//bool print_log = true;


static Receiver receiver;
static Event    recv_thread_stop_event;
static bool     recv_thread_stop = false;
static Atapi    atapi;


int main(void)
{
	// debug
	init_dbg();

	disp("Initialize\n");
	receiver.Init();

	receiver.Start();
	disp("Start\n");

	bool err_flag = false;
	while (1) {
		DWORD result = receiver.WaitEvent(1000);
		if (WAIT_TIMEOUT == result) {
//			printf("receriver::WaitEvent() timeout\n");
			continue;
		}

		if (WAIT_FAILED == result) {
			dbg("UNKOWN ERROR in \"%s\"(%d)\n", __FILE__, __LINE__);
			err_flag = true;
			break;
		}

		// Receiver クラスで受信したコマンドバッファをチェック
		_int64 int_val;
		char str_val[1024];
		char *ptr;
		Receiver::CMD_TYPE cmd = receiver.GetCommand(int_val, str_val, &ptr);
		dbg("cmd = %d, int_val = %I64d\n", cmd, int_val);

		if (Receiver::READSECTOR == cmd) {
			disp("READSECTOR\n");
			unsigned char buf[2048] = {0};
			int result = atapi.read_sector(buf, int_val);

			char retstr[2048 * 4 + 1] = {0};
			int  ret_sz = 1;
			if (result) {
				int i;
				for (i = 0; i < 2048; i++) {
					char tmp[10];
					_snprintf(tmp, 10, "%02X ", (unsigned int)buf[i]);
					strncat(retstr, tmp, 2048);
				}
				ret_sz = strlen(retstr);
			}

			receiver.send_result((unsigned char*)retstr, strlen(retstr));
		}

		else if (Receiver::READINFO == cmd) {
			disp("READINFO\n");

			atapi.read_info();
//			disp("disc size = %d\n", size);

			atapi.make_rzone_info();
			_int64 size = atapi.get_size();
			disp("disc size = %d\n", size);
			disp("disc size = %I64d\n", size);

			char retstr[255] = {0};
			_snprintf(retstr, 255, "%I64d", size);
			receiver.send_result((unsigned char*)retstr, strlen(retstr));
		}

		else if (Receiver::OPEN == cmd) {
			disp("OPEN\n");
			char drive[4] = {' ', ':', '\\', '\0'};
			int i;
			for (i = 0; i < 256; i++) {
				if (isalpha(str_val[i])) {
					drive[0] = str_val[i];
					break;
				}
			}

			do {
				if (i == 256) {
					receiver.send_result(ERR_RESULT, strlen((char *)ERR_RESULT));
					break;
				}

				int ret = atapi.open(drive);
				if (!ret) {
					receiver.send_result(ERR_RESULT, strlen((char *)ERR_RESULT));
					break;
				}
				ret = atapi.test_ready();
				if (!ret) {
					receiver.send_result(ERR_RESULT, strlen((char *)ERR_RESULT));
					break;
				}

				receiver.send_result(OK_RESULT, strlen((char *)OK_RESULT));
			} while (0);
		}
		//
		else if (Receiver::CLOSE == cmd) {
			disp("CLOSE\n");
			atapi.close();
			receiver.send_result(OK_RESULT, strlen((char *)OK_RESULT));
		}

		// イメージの書き込み
		else if (Receiver::WRITEIMAGE == cmd) {
			disp("WRITEIMAGE\n");
			dbg("Receiver::WRITEIMAGE\n");

			receiver.send_result(OK_RESULT, strlen((char *)OK_RESULT));

			dbg("call recv_image_buf\n");

			int ret = atapi.init_write_image();
			if (ret) {
				// 長時間戻らないのでデータの受信は別スレッドに任せる
				_beginthread(recv_image_buf, 0, (void *)&int_val);

				ret = atapi.write_image(int_val);
				dbg("atapi.write_image() return %d\n", ret);
				recv_thread_stop = true;

				dbg("write_image end\nwait recv_image_buf thread end\n");
				while (true) {
					DWORD result = recv_thread_stop_event.Wait(1000);
					if (WAIT_TIMEOUT == result) {
						dbg("recv_thread_stop_event.Wati() timeout\n");
						continue;
					}
					if (WAIT_OBJECT_0 == result) {
						break;
					}
				}
			}
			else {
				dbg("write image initialize failed\n");
			}
			dbg("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXn");
		}
		else if (Receiver::WRITETEST == cmd) {
			disp("WRITE TEST\n");

			receiver.send_result(OK_RESULT, strlen((char *)OK_RESULT));
			recv_thread_stop_event.ResetEvent();
			_beginthread(recv_image_buf2, 0, (void *)&int_val);

			while (true) {
				DWORD result = recv_thread_stop_event.Wait(1000);
				if (WAIT_TIMEOUT == result) {
					continue;
				}
				if (WAIT_OBJECT_0 == result) {
					break;
				}
			}
			dbg("end\n");
		}

		else {
			dbg("unkown command %d\n", cmd);
			if (Receiver::WRITE == cmd) {
				receiver.send_result(ERR_RESULT, strlen((char *)ERR_RESULT));
			}
		}
	}
	if (err_flag) {
		dbg("err\n");
	}

	return 1;
}


void recv_image_buf(void *ptr)
{
	dbg("[!] start recv_image_buf()\n");

	recv_thread_stop_event.ResetEvent();

	int sz = *(int *)ptr;
	int recv_sz = 0;
	bool err_flag = false;
	while (true) {
		if (recv_thread_stop) {
			dbg("[!] recv_thread_stop\n");
			recv_thread_stop = false;
			break;
		}

		DWORD result = receiver.WaitEvent(1000);

		if (WAIT_TIMEOUT == result) {
//			dbg("[!] recv_image_buf WaitEvent timeout\n");
			continue;
		}

		if (WAIT_FAILED == result) {
			dbg("[!] UNKOWN ERROR in \"%s\"(%d)\n", __FILE__, __LINE__);
			err_flag = true;
			break;
		}

		dbg("[!] recv_image_buf wakeup\n");

		_int64 bufsz;
		char str_val[1024];
		char *buf;
		Receiver::CMD_TYPE cmd = receiver.GetCommand(bufsz, str_val, &buf);

		if (Receiver::WRITE == cmd) {
			disp("WRITE\n");
			unsigned char *dat = new unsigned char [(int)bufsz];
			int dat_sz = 0;

			char tmp[3] = {0};
			bool space = true;
			int i, j;
			for (i = 0; i < bufsz; i++) {
				if (!isspace(buf[i])) {
					break;
				}
			}

			for (; i < bufsz; i++) {
				// 空白
				if (isspace(buf[i])) {
					if (!space) {
						char *dummy;
						int val = strtol(tmp, &dummy, 16);
						if (!dummy) {
							break;
						}
						dat[dat_sz++] = (unsigned char)val;
						space = true;
					}
				}
				else {
					if (space) {
						j = 0;
					}
					tmp[j++] = buf[i];
					space = false;
					if (j >= 3) {
//						dbg("[!] !!!\n");
					}
				}
			}

//			dbg("[!] redv %d\n", dat_sz);
			atapi.add_buf(dat, dat_sz);
			delete [] dat;
			receiver.send_result(OK_RESULT, strlen((char *)OK_RESULT));
//			dbg("[!] send_result() end\n");

			recv_sz += dat_sz;
			if (recv_sz >= sz) {
				break;
			}
//			dbg("[!] free %X\n", buf);
			delete [] buf;
		}

		else {
			receiver.send_result(ERR_RESULT, strlen((char *)ERR_RESULT));
		}
	}

	recv_thread_stop_event.SetEvent();
	_endthread();
}


void recv_image_buf2(void *ptr)
{
	int sz = *(int *)ptr;
	_int64 bufsz;
	char str_val[1024];
	char *buf;

	dbg("=================================================\n");


	while (true) {
		DWORD result = receiver.WaitEvent(1000);
		if (WAIT_TIMEOUT == result) {
			continue;
		}
		if (WAIT_FAILED == result) {
			dbg("unkown error \"%s\" (%d)\n", __FILE__, __LINE__);
			break;
		}

		Receiver::CMD_TYPE cmd = receiver.GetCommand(bufsz, str_val, &buf);

		if (Receiver::WRITE == cmd) {
			disp("WRITE\n");
			receiver.send_result(OK_RESULT, strlen((char *)OK_RESULT));
		}
		else {
			dbg("cmd = %d\n", cmd);
			receiver.send_result(ERR_RESULT, strlen((char *)ERR_RESULT));
		}
	}
	dbg("#######################################################\n");

}








