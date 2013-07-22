// Ring.h
// リングバッファを実装するクラス
// M.Kawaguchi (Heart Solutions, Inc.)
//
// $Id: Ring.h,v 1.4 2004/11/10 04:49:24 kawa Exp $
//

// 使い方
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
		/// コンストラクタ
		/**
			@param sz リングバッファのサイズ
		*/
		Ring(int sz) {
			buf = new char [sz];
			bufsz = sz;
			cur_ptr = 0;
			use_sz = 0;
		};

		/// デストラクタ
		virtual ~Ring(void) {
			delete [] buf;
		};

		/// データの格納
		/**
			データの格納を行います。データサイズが大きすぎる場合格納はされません。
			@param dat 格納データ
			@param sz  データサイズ
			@return 実行結果
				@retval true  成功
				@retval flase 失敗
		*/
		bool SetData(void *dat, int sz) {
			cs.Enter();

			// リングバッファの空き容量が足りない
			if (sz + use_sz > bufsz) {
				//dbg("ring set error\n");
				cs.Leave();
				return false;
			}


			// コピー先インデックスを計算
			int tail_ptr = cur_ptr + use_sz;
			if (tail_ptr >= bufsz) {
				tail_ptr -= bufsz;
			}

			int sz1, sz2;
			// コピーサイズが buf の境界をまたぐとき
			if (tail_ptr + sz > bufsz) {
				sz1 = bufsz - tail_ptr;
				sz2 = sz - sz1;
			}
			else {
				sz1 = sz;
				sz2 = 0;
			}

			// コピー
			memcpy(&buf[tail_ptr], dat, sz1);
			use_sz += sz;

			if (0 < sz2) {
				memcpy(buf, &(((char *)dat)[sz1]), sz2);
			}

			cs.Leave();
			return true;
		};

		/// データの取得
		/**
			データを取得します。リングバッファからは破棄されません
			@param dat データの格納先
			@param sz  データサイズ
			@return 実行結果
				@retval true  成功
				@retval false 失敗
		*/
		bool GetData(void *dat, int sz) {
			cs.Enter();

			// 要求されたサイズより格納されたサイズが小さいとき
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

		/// 格納データのサイズを返す
		/**
			格納されているデータのサイズを返します
			@return データサイズ
		*/
		int GetSize(void)
		{
			return use_sz;
		};

		/// データの削除
		/**
			バッファの先頭から指定されたサイズ削除します
			@param sz 削除するサイズ
			@return 実行結果
				@retval true  成功
				@retval false 失敗
		*/
		bool DeleteData(int sz) {
			// 要求されたサイズが大きすぎる場合
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
		char *buf;   // 格納バッファ
		int  bufsz;  // バッファサイズ
		int  cur_ptr; // 使用バッファの先頭
		int  use_sz;  // 使用バッファのサイズ

		CriticalSection cs;
	};
};

#endif // __RING_H__
