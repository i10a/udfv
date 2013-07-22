// Ring.h
// �����O�o�b�t�@����������N���X
// M.Kawaguchi (Heart Solutions, Inc.)
//
// $Id: Ring.h,v 1.4 2004/11/10 04:49:24 kawa Exp $
//

// �g����
//	Ring *ring = new Ring(sizeof (int) * 4);
//
//	int set_data = 4;
//	ring.SetData(&set_data, sizeof (set_data));
//
//	int get_data;
//	ring.GetData(&get_data, sizeof (get_data));
//
//	delete ring;

#ifndef __RING_H__
#define __RING_H__

//#include "dbg.h"
#include "CriticalSection.h"

namespace hs {

	class Ring
	{
	public:
		/// �R���X�g���N�^
		/**
			@param sz �����O�o�b�t�@�̃T�C�Y
		*/
		Ring(int sz) {
			buf = new char [sz];
			bufsz = sz;
			cur_ptr = 0;
			use_sz = 0;
		};

		/// �f�X�g���N�^
		virtual ~Ring(void) {
			delete [] buf;
		};

		/// �f�[�^�̊i�[
		/**
			�f�[�^�̊i�[���s���܂��B�f�[�^�T�C�Y���傫������ꍇ�i�[�͂���܂���B
			@param dat �i�[�f�[�^
			@param sz  �f�[�^�T�C�Y
			@return ���s����
				@retval true  ����
				@retval flase ���s
		*/
		bool SetData(void *dat, int sz) {
			cs.Enter();

			// �����O�o�b�t�@�̋󂫗e�ʂ�����Ȃ�
			if (sz + use_sz > bufsz) {
				//dbg("ring set error\n");
				cs.Leave();
				return false;
			}


			// �R�s�[��C���f�b�N�X���v�Z
			int tail_ptr = cur_ptr + use_sz;
			if (tail_ptr >= bufsz) {
				tail_ptr -= bufsz;
			}

			int sz1, sz2;
			// �R�s�[�T�C�Y�� buf �̋��E���܂����Ƃ�
			if (tail_ptr + sz > bufsz) {
				sz1 = bufsz - tail_ptr;
				sz2 = sz - sz1;
			}
			else {
				sz1 = sz;
				sz2 = 0;
			}

			// �R�s�[
			memcpy(&buf[tail_ptr], dat, sz1);
			use_sz += sz;

			if (0 < sz2) {
				memcpy(buf, &(((char *)dat)[sz1]), sz2);
			}

			cs.Leave();
			return true;
		};

		/// �f�[�^�̎擾
		/**
			�f�[�^���擾���܂��B�����O�o�b�t�@����͔j������܂���
			@param dat �f�[�^�̊i�[��
			@param sz  �f�[�^�T�C�Y
			@return ���s����
				@retval true  ����
				@retval false ���s
		*/
		bool GetData(void *dat, int sz) {
			cs.Enter();

			// �v�����ꂽ�T�C�Y���i�[���ꂽ�T�C�Y���������Ƃ�
			if (sz > use_sz) {
				//dbg("ring get error\n");
				cs.Leave();
				return false;
			}


			int sz1, sz2;
			if (cur_ptr + sz > bufsz) {
				sz1 = bufsz - cur_ptr;
				sz2 = sz - sz1;
			}
			else {
				sz1 = sz;
				sz2 = 0;
			}

			memcpy(dat, &buf[cur_ptr], sz1);
			if (0 < sz2) {
				memcpy(&((char *)dat)[sz1], buf, sz2);
			}

	//		dbg("RING GET [cur_ptr = %d, use_sz = %d]\n", cur_ptr, use_sz);

			cs.Leave();
			return true;

		};

		/// �i�[�f�[�^�̃T�C�Y��Ԃ�
		/**
			�i�[����Ă���f�[�^�̃T�C�Y��Ԃ��܂�
			@return �f�[�^�T�C�Y
		*/
		int GetSize(void)
		{
			return use_sz;
		};

		/// �f�[�^�̍폜
		/**
			�o�b�t�@�̐擪����w�肳�ꂽ�T�C�Y�폜���܂�
			@param sz �폜����T�C�Y
			@return ���s����
				@retval true  ����
				@retval false ���s
		*/
		bool DeleteData(int sz) {
			// �v�����ꂽ�T�C�Y���傫������ꍇ
			if (sz > use_sz) {
				//dbg("ring del error\n");
				return false;
			}

			cs.Enter();

			int old = cur_ptr;

			cur_ptr += sz;
			if (cur_ptr >= bufsz) {
				cur_ptr -= bufsz;
			}

			use_sz -= sz;

	//		dbg("RING DEL [cur_ptr = %d, use_sz = %d]\n", cur_ptr, use_sz);

			cs.Leave();
			return true;
		};

//		void Dump(void) {
//			bool in = false;
//			int sp = cur_ptr;
//			int ep = cur_ptr + use_sz;
//			if (ep >= bufsz) {
//				ep -= bufsz;
//				in = true;
//			}
//
//			int i;
//			for (i = 0; i < bufsz; i+= sizeof (int)) {
//				bool p = false;
//				if (ep == i) {
//					dbg("|  ");
//					in = false;
//					p = true;
//				}
//				if (in) {
//					dbg("===");
//					p = true;
//				}
//				if (sp == i) {
//					dbg("|==");
//					in = true;
//					p = true;
//				}
//				if (!p) {
//					dbg("   ");
//				}
//
//			}
//			dbg("\n");
//			for (i = 0; i < bufsz; i+= sizeof (int)) {
//				dbg(" %02d", *(int *)&buf[i]);
//			}
//			dbg("\n");
//		}

	protected:
		char *buf;   // �i�[�o�b�t�@
		int  bufsz;  // �o�b�t�@�T�C�Y
		int  cur_ptr; // �g�p�o�b�t�@�̐擪
		int  use_sz;  // �g�p�o�b�t�@�̃T�C�Y

		CriticalSection cs;
	};
};

#endif // __RING_H__
