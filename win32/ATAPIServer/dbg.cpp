// Debug ópä÷êîåQ
// M.Kawaguchi (Heart Solutions, Inc.)
//
// $Id: dbg.cpp,v 1.4 2004/11/10 04:49:24 kawa Exp $
//


#ifdef WIN32
#include <Windows.h>
#endif

#include <stdio.h>

#include "dbg.h"

static CRITICAL_SECTION cs;
static CRITICAL_SECTION disp_cs;
static bool init_cs = false;


void init_dbg(void)
{
	if (!init_cs) {
		::InitializeCriticalSection(&cs);
		::InitializeCriticalSection(&disp_cs);
		init_cs = true;
	}
}

void dbg(char *fmt, ...)
{
#ifndef _NDEBUG
	int r;
	va_list va;
	char tmp[1024];

	if (!init_cs) {
		init_dbg();
	}

	EnterCriticalSection(&cs);
	va_start(va, fmt);
	r = _vsnprintf(tmp, 1023, fmt, va);
	va_end(va);

	OutputDebugString(tmp);

	LeaveCriticalSection(&cs);

//	return r;
#endif // _NDEBUG
}

void disp(char *fmt, ...)
{
	int r;
	va_list va;
	char tmp[1024];

	if (!init_cs) {
		init_dbg();
	}

	EnterCriticalSection(&disp_cs);

	va_start(va, fmt);
	r = _vsnprintf(tmp, 1023, fmt, va);
	va_end(va);

	printf("%s", tmp);

	LeaveCriticalSection(&disp_cs);
}



void finish_dbg(void)
{
	::DeleteCriticalSection(&cs);
}


// test
