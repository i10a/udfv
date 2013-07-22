// Event.h
// �C�x���g�̃N���X
// M.Kawaguchi (Heart Solutions, Inc.)
//
// $Id: Event.h,v 1.1 2005/04/28 11:26:01 kawa Exp $
//

#ifndef __EVENT_H__
#define __EVENT_H__

//#include "GuardValue.h"

#ifdef WIN32
#include <windows.h>
#endif


#include "dbg.h"

/// Event �N���X
namespace hs {
	class Event
	{
	public:
		/// �R���X�g���N�^
		Event(char *name = NULL) : handle(NULL) {
			InitializeCriticalSection(&cs);

			// ��V�O�i����ԂŃC�x���g���쐬
			handle = CreateEvent(NULL,
			                     FALSE, // ��V�O�i�����
			                     FALSE, // �������Z�b�g
			                     name);

			event_name = name;
			signal = false;
		};

		/// �f�X�g���N�^
		virtual ~Event() {
			if (NULL != handle) {
				CloseHandle(handle);
				handle = NULL;
			}
			DeleteCriticalSection(&cs);
		};

		/// �C�x���g�̖��O��ݒ�
		/**
			�f�o�b�O�̂��߂ɃI�u�W�F�N�g���ɔC�ӂ̕�����|�C���^��
			����U�邱�Ƃ��ł��܂�
			@param name �o�^���镶����
		*/
		void SetEventName(char *name) {
			event_name = name;
		};


		// �V�O�i����ԂɂȂ�܂ő҂�
		/**
			�V�O�i����ԂɂȂ�܂őҋ@���܂�
			@param t �^�C���A�E�g�ɂȂ�܂ł̎��Ԃ��~���b�Ŏw�肵�܂�
			         -1 ���w�肷��ƃ^�C���A�E�g�����ɑҋ@���܂��B
			@return ���s����
				@retval WAIT_OBJECT_0 �V�O�i����ԂɂȂ�܂���
				@retval WAIT_TIMEOUT  �^�C���A�E�g���Ԃ��o�߂��܂���
				@retval WAIT_FAILED    �֐������s���܂���
		*/
		virtual DWORD Wait(int t = -1) {
			DWORD result;
			if (event_name) {
//				dbg("[%s] wait\n", event_name);
//				dbg("hoge = %d\n", signal);
			}
			if (-1 == t) {
				result = ::WaitForSingleObject(handle, INFINITE);
			}
			else {
				result = ::WaitForSingleObject(handle, t);
			}

			if (event_name) {
				if (result == WAIT_TIMEOUT) {
					if (signal == true) {
//						dbg("????????????????\n");
					}
				}
				else {
//					dbg("wakeup, hoge = %d\n", signal);
				}
			}

			return result;
		};

		/// ��V�O�i����Ԃɂ���
		virtual void ResetEvent(void) {
			EnterCriticalSection(&cs);
			if (signal) {
				::ResetEvent(handle);
				if (event_name) {
//					dbg("[%s] reset signal\n", event_name);
				}
			}
			signal = false;

			LeaveCriticalSection(&cs);
		};

		/// �V�O�i����Ԃɂ���
		virtual void SetEvent(void) {
			EnterCriticalSection(&cs);
			::SetEvent(handle);
			if (event_name) {
//				dbg("[%s] signal\n", event_name);
			}
			signal = true;
			LeaveCriticalSection(&cs);
		};

		// �V�O�i����Ԃ��m�F����
		virtual bool GetSignalState(void) {
			if (event_name) {
//				dbg("signal = %d\n", signal);
			}
			return signal;
		}

	protected:
		CRITICAL_SECTION cs;
		HANDLE handle;
		char *event_name;
		bool signal;
	};
};


#endif // __EVENT_H__
