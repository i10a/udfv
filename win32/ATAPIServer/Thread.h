// Thread �N���X
// _beginthread() / _endthread() �ɂ��X���b�h���Ǘ�����N���X�ł�
// M.Kawaguchi (Heart Solutiosn, Inc.)
//
// $Id: Thread.h,v 1.1 2004/10/13 04:56:56 kawa Exp $
//

// �g����
// - GuardValue �N���X�AEvent �N���X���g�p���Ă��܂�
// - Thread ���p�������N���X���쐬���܂��B
// - �X���b�h�œ������������\�b�h�� ThreadProc() �ɃI�[�o�[���C�h���܂�
// - ThreadProc() �� message �����o���Q�Ƃ��AM_STOP ��������
//   �I������悤�ɍ쐬���Ă��������B
// - Start() �ŃX���b�h�̊J�n�AStop() �ŃX���b�h���~���܂��B
// - GetState() �ŃX���b�h�̏�Ԃ��擾�ł��܂�

#ifndef __THREAD_H__
#define __THREAD_H__

#include <process.h>

#include "GuardValue.h"
#include "Event.h"

//#include "dbg.h"

namespace hs {
	class Thread
	{
	public:
		// for debug
		char *dbg_threadname;

		CRITICAL_SECTION cs;

		// �X���b�h�̏��
		enum State {
			ST_NULL,  // ���s�O
			ST_RUN,   // ���s��
	//		ST_PAUSE, // �|�[�Y��
			ST_STOP,  // ��~
		};

		// �R���X�g���N�^
		Thread() : dbg_threadname(NULL) {
			func = NULL;
			state.Set(ST_NULL);
			message = M_NULL;

			InitializeCriticalSection(&cs);
			InitializeCriticalSection(&stop_cs);
		};

		// �f�X�g���N�^
		virtual ~Thread() {
			State st = state.Get();
			{
				if (dbg_threadname) {
					//dbg("1[%s]    ", dbg_threadname);
				}
				if (ST_NULL == st) {
//					dbg("ST_NULL\n");
				}
				else if (ST_RUN == st) {
//					dbg("ST_RUN\n");
				}
				else if (ST_STOP == st) {
//					dbg("ST_STOP\n");
				}
				else {
//					dbg("ST_??\n");
				}
			}

			if (ST_RUN == st) {
				Stop();
			}
			if (dbg_threadname) {
				//dbg("2[%s]", dbg_threadname);
			}
			//dbg("DESTRACT\n");

			EnterCriticalSection(&cs);
			LeaveCriticalSection(&cs);

			EnterCriticalSection(&stop_cs);
			LeaveCriticalSection(&stop_cs);

			DeleteCriticalSection(&cs);
			DeleteCriticalSection(&stop_cs);
		};

		// �X���b�h���J�n����
		virtual void Start(void) {
			State st = state.Get();
			if (ST_NULL == st) {
				_beginthread(ThreadCallback, 0, (void *)this);
	//			state.Set(ST_RUN);
			}
			else {
				//dbg("start failed\n");
			}
		};

		// �X���b�h���~����
		virtual void Stop() {
			EnterCriticalSection(&stop_cs);

			State st = state.Get();
			if (ST_RUN == st) {
				stop_event.ResetEvent();

				// �X���b�h�ɒ�~���b�Z�[�W�𑗂�
				message = M_STOP;

				// �X���b�h����~����܂ő҂�
				if (NULL != dbg_threadname) {
					//dbg("3[%s] ", dbg_threadname);
				}
				//dbg("wait thread stop\n");

				while (1) {
					DWORD eret = stop_event.Wait(1000);
					if (WAIT_TIMEOUT == eret) {
						//dbg("[%s] time_out\n", dbg_threadname);
						continue;
					}
					break;
				}

				state.Set(ST_NULL);
				if (NULL != dbg_threadname) {
					//dbg("4[%s] ", dbg_threadname);
				}
				//dbg("stop\n");
			}
			else {
				if (dbg_threadname) {
					//dbg("5[%s] ", dbg_threadname);
				}
				//dbg("Thread state is not ST_RUN\n");
			}
			LeaveCriticalSection(&stop_cs);
		};

		// �X���b�h�̏�Ԃ��擾
		virtual State GetState(void) {
			State st;
			st = state.Get();
			return st;
		};

		// �X���b�h�̒��ڂ̃R�[���o�b�N�B
		// _beginthread() �̃R�[���o�b�N�֐��ɂ��邽�߂ɂ�������A
		// �X�^�e�B�b�N�ȃ��\�b�h�Ŏ󂯎���Ă���
		static void ThreadCallback(void *this_pointer) {
			Thread *thread = reinterpret_cast<Thread *>(this_pointer);

			if (NULL != thread->dbg_threadname) {
				//dbg("6[%s]", thread->dbg_threadname);
			}
			//dbg("%X ThreadCallback() START ThreadProc() \n", thread);

			// �X���b�h�̎��̂�
			thread->ThreadProc();

			EnterCriticalSection(&thread->cs);

			if (NULL != thread->dbg_threadname) {
				//dbg("7[%s]", thread->dbg_threadname);
			}
			//dbg("ThreadCallback() return ThreadProc() \n");

			// �X���b�h�I������
			thread->SetState(ST_STOP);

			if (NULL != thread->dbg_threadname) {
				//dbg("8[%s]", thread->dbg_threadname);
			}
			//dbg("Thread ThreadCallback() stop_event.SetEvent()\n");

			// Stop() ���\�b�h���N����
			thread->stop_event.SetEvent();

			if (NULL != thread->dbg_threadname) {
				//dbg("9[%s]", thread->dbg_threadname);
			}
			//dbg("_endthread()\n");

			LeaveCriticalSection(&thread->cs);

			_endthread();
		};

		// ���̃��\�b�h���I�[�o�[���C�h���Ă�������
		virtual void  ThreadProc(void) = 0;

	protected:
		// �X���b�h�ւ̃��b�Z�[�W
		enum Message {
			M_NULL,   // ���ߖ���
			M_RUN,    // �X�^�[�g����
	//		M_PAUSE,
			M_STOP,   // ��~����
		};

		GuardValue <State> state;                  // �X���b�h�̏��
		Message            message;                // �X���b�h�ւ̃��b�Z�[�W
		void               (_cdecl *func)(void *); // �R�[���o�b�N�֐�
		Event              stop_event;             // �X���b�h��~�p�C�x���g

		CRITICAL_SECTION stop_cs;


		// �X���b�h�̏�Ԃ�ݒ肷��
		virtual void  SetState(State st) {
			state.Set(st);
		}
	};
};

#endif __THREAD_H__



