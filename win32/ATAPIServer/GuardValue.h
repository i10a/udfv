// GuardValue.h
// スレッドセーフなコンテナのためのテンプレート
// Set() で代入、Get() で取得します。
// クリティカルセクションによってガードされています
// M.Kawaguchi (Heart Solutions, Inc.)
//
// $Id: GuardValue.h,v 1.1 2005/04/28 11:26:01 kawa Exp $
//

#ifndef __GUARD_VALUE_H__
#define __GUARD_VALUE_H__

#include <Windows.h>

namespace hs {
	template <typename T> class GuardValue
	{
	public:
		// コンストラクタ
		GuardValue() {
			::InitializeCriticalSection(&cs);

		};

		// デストラクタ
		virtual ~GuardValue() {
			DeleteCriticalSection(&cs);
		};

		// 値をセット
		virtual void  Set(T data) {
			EnterCriticalSection(&cs);
			value = data;
			LeaveCriticalSection(&cs);
		};

		// 値を取得
		virtual T Get(void) {
			T tmp;
			EnterCriticalSection(&cs);
			tmp = value;
			LeaveCriticalSection(&cs);
			return tmp;
		};

	protected:
		CRITICAL_SECTION cs; // クリティカルセクションを制御するためのオブジェクト
		T value;             // 保持する値
	};
};

#endif // __GUARD_VALUE_H__

