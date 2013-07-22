// Debug ópä÷êîåQ (for Windows)
// M.Kawaguchi (Heart Solutions, Inc.)
//
// $Id: dbg.h,v 1.3 2004/10/18 10:30:04 kawa Exp $
//


#ifndef __DBG_H__
#define __DBG_H__

#define ErrorWinApi(x) _ErrorWinApi(x, __FILE__, __LINE__)


//void Error(TCHAR *str);
//void _ErrorWinApi(int errorno, TCHAR *file, int line);

void dbg(char *fmt, ...);
void disp(char *fmt, ...);
void init_dbg(void);
void finish_dbg(void);

int timecheckStart(void);
int timecheckEnd(void);


#endif // __DBG_H__
