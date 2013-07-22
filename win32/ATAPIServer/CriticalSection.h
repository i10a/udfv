// 環境によるクリティカルセクションを吸収するクラス

#ifndef __CRITICALSECTION_H__
#define __CRITICALSECTION_H__

#ifdef WIN32
#include <Windows.h>
#endif


//#include "dbg.h"



namespace hs {
	class CriticalSection
	{
	public:
		CriticalSection(void)
#ifndef NDEBUG
		: counter(0)
#endif // NDEBUG
		{
			::InitializeCriticalSection(&cs);
		};

		virtual ~CriticalSection(void) {
#ifndef NDEBUG
			if (0 < counter) {
//				dbg("[CriticalSection] : 終了していない CriticalSection があります\n");
				//LeaveCriticalSection(&cs);
			}
#endif // NDEBUG

			::DeleteCriticalSection(&cs);
		};

		void Enter(void) {
			EnterCriticalSection(&cs);
#ifndef NDEBUG
			counter++;
#endif // NDEBUG
		}

		void Leave(void) {
#ifndef NDEBUG
			counter--;
			if (0 < counter) {
//				dbg("[CriticalSection] : 余計に Leave しています\n");
			}
#endif // NDEBUG
			LeaveCriticalSection(&cs);
		}

	protected:
		CRITICAL_SECTION cs;

#ifndef NDEBUG
		int counter;
#endif // NDEBUG
	};
};


#endif // __CRITICALSECTION_H__



