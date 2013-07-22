// ���ɂ��N���e�B�J���Z�N�V�������z������N���X

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
//				dbg("[CriticalSection] : �I�����Ă��Ȃ� CriticalSection ������܂�\n");
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
//				dbg("[CriticalSection] : �]�v�� Leave ���Ă��܂�\n");
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



