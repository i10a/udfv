// Thread クラス
// _beginthread() / _endthread() によるスレッドを管理するクラスです
// M.Kawaguchi (Heart Solutiosn, Inc.)
//
// $Id: Thread.h,v 1.1 2004/10/13 04:56:56 kawa Exp $
//

// 使い方
// - GuardValue クラス、Event クラスを使用しています
// - Thread を継承したクラスを作成します。
// - スレッドで動かしたいメソッドを ThreadProc() にオーバーライドします
// - ThreadProc() は message メンバを参照し、M_STOP だったら
//   終了するように作成してください。
// - Start() でスレッドの開始、Stop() でスレッドを停止します。
// - GetState() でスレッドの状態を取得できます

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

		// スレッドの状態
		enum State {
			ST_NULL,  // 実行前
			ST_RUN,   // 実行中
	//		ST_PAUSE, // ポーズ中
			ST_STOP,  // 停止
		};

		// コンストラクタ
		Thread() : dbg_threadname(NULL) {
			func = NULL;
			state.Set(ST_NULL);
			message = M_NULL;

			InitializeCriticalSection(&cs);
			InitializeCriticalSection(&stop_cs);
		};

		// デストラクタ
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

		// スレッドを開始する
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

		// スレッドを停止する
		virtual void Stop() {
			EnterCriticalSection(&stop_cs);

			State st = state.Get();
			if (ST_RUN == st) {
				stop_event.ResetEvent();

				// スレッドに停止メッセージを送る
				message = M_STOP;

				// スレッドが停止するまで待つ
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

		// スレッドの状態を取得
		virtual State GetState(void) {
			State st;
			st = state.Get();
			return st;
		};

		// スレッドの直接のコールバック。
		// _beginthread() のコールバック関数にするためにいったん、
		// スタティックなメソッドで受け取っている
		static void ThreadCallback(void *this_pointer) {
			Thread *thread = reinterpret_cast<Thread *>(this_pointer);

			if (NULL != thread->dbg_threadname) {
				//dbg("6[%s]", thread->dbg_threadname);
			}
			//dbg("%X ThreadCallback() START ThreadProc() \n", thread);

			// スレッドの実体へ
			thread->ThreadProc();

			EnterCriticalSection(&thread->cs);

			if (NULL != thread->dbg_threadname) {
				//dbg("7[%s]", thread->dbg_threadname);
			}
			//dbg("ThreadCallback() return ThreadProc() \n");

			// スレッド終了処理
			thread->SetState(ST_STOP);

			if (NULL != thread->dbg_threadname) {
				//dbg("8[%s]", thread->dbg_threadname);
			}
			//dbg("Thread ThreadCallback() stop_event.SetEvent()\n");

			// Stop() メソッドを起こす
			thread->stop_event.SetEvent();

			if (NULL != thread->dbg_threadname) {
				//dbg("9[%s]", thread->dbg_threadname);
			}
			//dbg("_endthread()\n");

			LeaveCriticalSection(&thread->cs);

			_endthread();
		};

		// このメソッドをオーバーライドしてください
		virtual void  ThreadProc(void) = 0;

	protected:
		// スレッドへのメッセージ
		enum Message {
			M_NULL,   // 命令無し
			M_RUN,    // スタート命令
	//		M_PAUSE,
			M_STOP,   // 停止命令
		};

		GuardValue <State> state;                  // スレッドの状態
		Message            message;                // スレッドへのメッセージ
		void               (_cdecl *func)(void *); // コールバック関数
		Event              stop_event;             // スレッド停止用イベント

		CRITICAL_SECTION stop_cs;


		// スレッドの状態を設定する
		virtual void  SetState(State st) {
			state.Set(st);
		}
	};
};

#endif __THREAD_H__



