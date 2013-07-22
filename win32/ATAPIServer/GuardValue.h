// GuardValue.h
// �X���b�h�Z�[�t�ȃR���e�i�̂��߂̃e���v���[�g
// Set() �ő���AGet() �Ŏ擾���܂��B
// �N���e�B�J���Z�N�V�����ɂ���ăK�[�h����Ă��܂�
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
		// �R���X�g���N�^
		GuardValue() {
			::InitializeCriticalSection(&cs);

		};

		// �f�X�g���N�^
		virtual ~GuardValue() {
			DeleteCriticalSection(&cs);
		};

		// �l���Z�b�g
		virtual void  Set(T data) {
			EnterCriticalSection(&cs);
			value = data;
			LeaveCriticalSection(&cs);
		};

		// �l���擾
		virtual T Get(void) {
			T tmp;
			EnterCriticalSection(&cs);
			tmp = value;
			LeaveCriticalSection(&cs);
			return tmp;
		};

	protected:
		CRITICAL_SECTION cs; // �N���e�B�J���Z�N�V�����𐧌䂷�邽�߂̃I�u�W�F�N�g
		T value;             // �ێ�����l
	};
};

#endif // __GUARD_VALUE_H__

