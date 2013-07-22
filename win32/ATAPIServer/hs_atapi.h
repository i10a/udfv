//
#ifndef __HS_ATAPI_H__
#define __HS_ATAPI_H__

//#define _WIN32_WINNT 0x0501
//#define WIN32_WINNT 0x0501
//#define _WINVER 0x0501
//#define WINVER 0x0501

#include <windows.h>

// 必要
#define ULONG_PTR DWORD

#include <devioctl.h>
#include <ntddscsi.h>
#include <spti.h>

#include "CriticalSection.h"


#define DVD_RAM     0
#define DVD_R       1
#define DVD_RW      2
#define CD_R        3
#define CD_RW       4
#define DVD_PLUS_RW 5
#define DVD_PLUS_R  6
#define TEST_WRITE  7

#define COM_1B_START_DISC          0x00
#define COM_1B_START_DISC_READ_TOC 0x01
#define COM_1B_EJECT               0x02
#define COM_1B_LOAD_DISC           0x03


/// Heart Solutions, Inc. の名前空間
namespace hs {
	/// ATAPI デバイスを扱うクラス
	class Atapi
	{
	public:
		/// 読み書き機能のフラグ
		struct ReadWriteState {
			int read;
			int write;
		};

		/// デバイスとドライブの状態を管理する構造体
		struct DriveState {
			int tray_open;        //!<トレイの状態(0:クローズ/1:オープン)
			int capacity;         //!<?
			int block_length;     //!<ブロックの長さ
			ReadWriteState rw[8]; //!<各ドライブタイプの読み書き可能/不可能
			int first_rzone;      //!<先頭の RZone
			int last_rzone;       //!<最後の RZone
			int max_w_speed;      //!<最大書き込みスピード
			int buf_e;           //!<Buffer Under-run Free
			_int64 max_size;
		};

		/// ATAPI コマンドのエラー値 SENSE データを格納する構造体
		struct SenseData {
			int sense_key;
			int asc;
			int ascq;
		};

		/// RZone 情報管理構造体
		struct RZone {
			int rzone_num;
			int border_num;
			int damage;
			int copy;
			int track_mode;
			int rt;
			int blank;
			int packet_inc;
			int fp;
			int data_mode;
			int lrb_v;
			int nwa_v;
			int rzone_start;
			int next_w_add;
			int free_blocks;
			int packet_size;
			int rzone_sz;
			int last_red_add;
		};

		Atapi(void);
		virtual ~Atapi(void);

		int open(char *drivename);
		int close(void);

		int test_ready(void);

		int read_sector(unsigned char *buf, _int64 sector);
		_int64 read_info(void);

		int init_write_image(void);
		int finish_write_image(void);
		int write_image(_int64 image_sz);
		int add_buf(unsigned char *buf, int sz);
		int make_rzone_info(void);
		_int64 get_size(void);

		int start_stop_unit(int);
		int test_unit_ready(void);
		int mechanism_status(void);
		int read_capacity(void);
		int read10(int sec, unsigned char *buf, int bufsz);
		int read_disc_info(void);
		int read_rzone_inf(int rzone_n, RZone *);
		int mode_sense10(int, int, unsigned char *, int);
		int mode_select10(unsigned char *buf, int bufsz);
		int write10(int sec, unsigned char *buf, int bufsz);
		int reserve_rzone(_int64 sz);
		int set_cd_speed(int, int);
		int request_sense(unsigned char *buf, int bufsz);
		int seek(int sec);
		int get_configuration(int rt, int sfn, unsigned char *buf, int bufsz);


	protected:
		HANDLE dev;                    //!<デバイスハンドル
		Atapi::DriveState drive_state; //!<ドライブ情報
		Atapi::SenseData sense_data;   //!<SENSE データ
		Atapi::RZone *rzone;           //!<RZone 情報
		int rzone_sz;                  //!<RZone の数

		int error_no;                  //!<エラー情報
		CriticalSection log_cs;        //!<ログ出力のクリティカルセクション
		CriticalSection send_cs;       //!<ATAPIコマンド送出のクリティカルセクション

		// イメージ書き込み用メンバ
		bool image_writing;       //!<イメージ書き込み中フラグ
		FILE *image_fp;           //!<イメージファイルの FILE 構造体
		_int64 read_p;              //!<イメージ読み込みのポイント
		CriticalSection image_cs; //!<イメージファイルアクセスのクリティカルセクション

		int read_sector_cache(unsigned char *buf, _int64 sector);

		int analyz_configuration_info(unsigned char *buf, int bufsz);

		unsigned char *allign(unsigned char *);
		void print_sense(unsigned char *buf, int bufsz);

		void print_sptwb_result(BOOL, DWORD, SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER *);
		char *scsistat_str(int);
		void dump(void *, int);
		void print_get_configuration_result(unsigned char *buf, int bufsz);

		void set_sptdwb(SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER *,
		                unsigned char *, int, int, void *, int);
		int send_atapi(SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER *sptdwb);

		int get_be_16(unsigned char *);
		int get_be_32(unsigned char *);
		int get_be_data(unsigned char *, int);
		int get_bit(unsigned char, int);
//		void log(char *fmt, ...);
		int is_sensekey(int sense_key, int asc, int ascq);

	};
};


#endif // __HS_ATAPI_H__


