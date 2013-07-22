// Event.h
// イベントのクラス
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

/// Event クラス
namespace hs {
	class Event
	{
	public:
		/// コンストラクタ
		Event(char *name = NULL) : handle(NULL) {
			InitializeCriticalSection(&cs);

			// 非シグナル状態でイベントを作成
			handle = CreateEvent(NULL,
			                     FALSE, // 非シグナル状態
			                     FALSE, // 自動リセット
			                     name);

			event_name = name;
			signal = false;
		};

		/// デストラクタ
		virtual ~Event() {
			if (NULL != handle) {
				CloseHandle(handle);
				handle = NULL;
			}
			DeleteCriticalSection(&cs);
		};

		/// イベントの名前を設定
		/**
			デバッグのためにオブジェクト毎に任意の文字列ポインタを
			割り振ることができます
			@param name 登録する文字列
		*/
		void SetEventName(char *name) {
			event_name = name;
		};


		// シグナル状態になるまで待つ
		/**
			シグナル状態になるまで待機します
			@param t タイムアウトになるまでの時間をミリ秒で指定します
			         -1 を指定するとタイムアウトせずに待機します。
			@return 実行結果
				@retval WAIT_OBJECT_0 シグナル状態になりました
				@retval WAIT_TIMEOUT  タイムアウト時間が経過しました
				@retval WAIT_FAILED    関数が失敗しました
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

		/// 非シグナル状態にする
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

		/// シグナル状態にする
		virtual void SetEvent(void) {
			EnterCriticalSection(&cs);
			::SetEvent(handle);
			if (event_name) {
//				dbg("[%s] signal\n", event_name);
			}
			signal = true;
			LeaveCriticalSection(&cs);
		};

		// シグナル状態を確認する
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
