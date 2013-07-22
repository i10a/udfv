// receiver ƒNƒ‰ƒX
#ifndef __RECEIVER_H__
#define __RECEIVER_H__

#include "Thread.h"
#include "Ring.h"
#include "Event.h"


#define RECVBUF (2048*10)

namespace hs {
	class Receiver : public Thread
	{
	public:
		///
		enum CMD_TYPE {
			CMD_ERROR,
			READSECTOR,
			READINFO,
			OPEN,
			CLOSE,
			WRITEIMAGE,
			WRITE,
			WRITEBYTE,
			WRITETEST,
		};

		struct CmdStruct {
			CmdStruct(void) {
				type = CMD_ERROR;
				int_val = 0;
				memset(str, 0, sizeof (str));
				ptr = NULL;
			};
			CmdStruct(CMD_TYPE t, _int64 ival, char *s, char *p) {
				type = t;
				int_val = ival;
				if (s) {
					strncpy(str, s, 256);
				}
				else {
					memset(str, 0, sizeof (str));
				}
				ptr = p;
			};
			CMD_TYPE type;
			_int64 int_val;
			char str[256];
			char *ptr;
		};

		Receiver(void);
		virtual ~Receiver(void);

		int Init(void);

		virtual void ThreadProc(void);

		DWORD WaitEvent(int t = -1);
		CMD_TYPE GetCommand(_int64 &num, char *str, char **ptr);

		int send_result(unsigned char *buf, int sz);


	protected:
		SOCKET fd;
		SOCKET recvsock;
		struct sockaddr_in addr;
		int error_no;
		Ring *recv_buf;
		Event cmd_event;
		Ring *cmd_buf;
		bool accept_flag;// = false;
		CriticalSection send_cs;


		void recv_data_dispatch(unsigned char *buf, int sz);
		int set_cmd_buf(CmdStruct *cmd);


	};
};

#endif // __RECEIVER_H__



