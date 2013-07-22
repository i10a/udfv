//
// 必須

#include <stdio.h>
#include <stddef.h>

#include "hs_atapi.h"

#include "dbg.h"

static const char *rw_labels[][2] = {
	{"DVD-RAM READ", "DVD-RAM WRITE"}, // DVD_RAM
	{"DVD-R READ",   "DVD-R WRITE"},   // DVD_R
	{"DVD-RW READ",  "DVD-RW WRITE"},  // DVD_RW
	{"CD-R READ",    "CD-R WRITE"},    // CD_R
	{"CD-RW READ",   "CD-RW WRITE"},   // CD_RW
	{"DVD+RW READ",  "DVD+RW WRITE"},  // DVD_PLUS_RW
	{"DVD+R READ",   "DVD+R WRITE"},   // DVD_PLUS_R
	{"",             "TEST WRITE"},
};



using namespace hs;

Atapi::Atapi(void)
{
	dev = NULL;
	memset(&drive_state, 0, sizeof (drive_state));
	memset(&sense_data, 0, sizeof (sense_data));
	rzone = NULL;
	rzone_sz = 0;
	//log_fp = fopen("log.txt", "w");
	image_writing = false;
	image_fp = NULL;
}


Atapi::~Atapi(void)
{
	close();
}

int Atapi::open(char *drivename)
{
	char vname[1024];

	GetVolumeNameForVolumeMountPoint(drivename, vname, 1024);
	if (vname[strlen(vname) - 1] == '\\') {
		vname[strlen(vname) - 1] = '\0';
	}

	dev = CreateFile(vname,
	                 GENERIC_READ | GENERIC_WRITE,
	                 FILE_SHARE_READ | FILE_SHARE_WRITE,
	                 NULL,
	                 OPEN_EXISTING,
	                 0,
	                 NULL);
	if (INVALID_HANDLE_VALUE == dev) {
		dev = NULL;
		return 0;
	}

	return 1;
}


int Atapi::close(void)
{
	if (!dev) {
		CloseHandle(dev);
		dev = NULL;
	}

	if (!rzone) {
		free(rzone);
		rzone = NULL;
	}

	return 1;
}


int Atapi::test_ready(void)
{
	if (!dev) {
		return 0;
	}

	int ret = mechanism_status();
	if (!ret) {
		return ret;
	}

	const int loop = 10;
	int i;
	for (i = 0; i < loop; i++) {
		ret = start_stop_unit(COM_1B_LOAD_DISC);
		if (ret) {
			break;
		}
		Sleep(5000);
	}
	if (i >= loop) {
		return 0;
	}

	ret = test_unit_ready();
	if (!ret) {
		return ret;
	}

	for (i = 0; i < loop; i++) {
		ret = start_stop_unit(COM_1B_START_DISC);
		if (!ret) {
			return ret;
		}

		if (sense_data.sense_key == 0) {
			break;
		}
		Sleep(1000);
	}

	if (i >= loop) {
		return 0;
	}

	ret = read_capacity();
	if (!ret) {
		return ret;
	}

	unsigned char orgbuf[2048 + 0x0f] = {0};
	unsigned char *buf = allign(orgbuf);

	// RT = 01b, Starging Feature Number = 0x00?
	ret = get_configuration(0x01, 0x00, buf, 2048);
	analyz_configuration_info(buf, 2048);
//	dump(buf, 2048);

	return true;
}


int Atapi::read_sector(unsigned char *buf, _int64 sector)
{
	dbg("read_sector(%p, %d)\n", buf, sector);
	if (!dev) {
		return 0;
	}

	int ret;
//	int i;

	if (!rzone) {
		ret = make_rzone_info();
	}

	bool found = false;
//	for (i = 1; i <= drive_state.last_rzone; i++) {
//		if (rzone[i].rzone_start <= sector
//		        && sector <= (rzone[i].rzone_start + rzone[i].rzone_sz)) {
//			found = true;
//			break;
//		}
//	}

#if 0//余計なエラーチェックを削除
	if (drive_state.capacity >= sector) {
		found = true;
	}
#endif

//	if (!found) {
		memset(buf, 0, 2048);
//		dbg("Sector %d is not writed\n", sector);
//		return 1;
//	}

	dbg("---------------------------------------------------------------\n");
	dbg("read_sector(, %d)\n", sector);
	ret = read_sector_cache(buf, sector);
	dbg("========================\n");

	return ret;
}


int Atapi::read_sector_cache(unsigned char *buf, _int64 sector)
{
	static int cache_block = -1;
	static unsigned char org_cache_buf[2048 * 16 + 0x0f] = {0};
	static unsigned char *cache_buf = allign(org_cache_buf);

	int ret;

	int bno = (int)(sector / 16.0);

	dbg("sector = %d, blockno = %d (cached = %d)\n", sector, bno, cache_block);

	// キャッシュからはずれ
	if (bno != cache_block) {
		dbg("cache miss\n");
		memset(org_cache_buf, 0, sizeof (org_cache_buf));

//		int rsec = sector + 15;
//		rsec -= rsec % 16;
		int rsec = bno * 16;

		ret = test_unit_ready();
		if (!ret) {
			return 0;
		}
		ret = seek(rsec);
		ret = read10(rsec, cache_buf, 2048 * 16);
		dump(cache_buf, 32);

		if (!ret) {
			return 0;
		}
		cache_block = bno;
	}

	_int64 index = sector - bno * 16;
	memcpy(buf, &cache_buf[index * 2048], 2048);

	return 1;
}

_int64 Atapi::read_info(void)
{
	if (!dev) {
		return 0;
	}

	_int64 ret = (drive_state.capacity + 1) * (_int64)2048;
	dbg("capacity = %I64d\n", ret);
	dump(&ret, 8);

	return ret;
}

int Atapi::make_rzone_info(void) {
	int ret;
	ret = read_disc_info();
	if (!ret) {
		return ret;
	}

	rzone_sz = drive_state.last_rzone + 1;
	if (rzone) {
		free(rzone);
	}
	rzone = (RZone *)malloc(sizeof (RZone) * rzone_sz);
	memset(rzone, 0, sizeof (RZone) * rzone_sz);

	int i;
	_int64 size = 0;
	for (i = 1; i <= drive_state.last_rzone; i++) {
		ret = read_rzone_inf(i, &rzone[i]);
		if (!ret) {
			return ret;
		}

		if (!rzone[i].blank) {
			size = rzone[i].rzone_start + rzone[i].rzone_sz;
		}
	}

	drive_state.max_size = size * (_int64)2048;

	return ret;
}


_int64 Atapi::get_size(void)
{
	return drive_state.max_size;
}


int Atapi::write_image(_int64 image_sz)
{
	if (!dev) {
		return 0;
	}

	if (!image_writing) {
		return 0;
	}

	int ret;

	int i;
	bool ready = false;
	for (i = 0; i < 100; i++) {
		ret = start_stop_unit(COM_1B_LOAD_DISC);
		if (ret) {
			ready = true;
			break;
		}
	}
	if (!ready) {
		return 0;
	}

	unsigned char orgbuf[2048 + 0x0f] = {0};
	unsigned char *buf = allign(orgbuf);


	try {
		// 書き込みモードを設定 (disc at once)
		ret = mode_sense10(0x00, 0x3f, buf, 2048);
		if (!ret) {
			throw 1;
		}

		memset(orgbuf, 0, sizeof (orgbuf));
		ret = mode_sense10(0x00, 0x05, buf, 58);
		if (!ret) {
			throw 1;
		}

		int test_write = 0x01; // テスト:0x01 本番:0x00
		int bufe = 0x00;       // Buffer Under-run Free Enable;
		if (drive_state.buf_e) {
			bufe = 0x01;
		}

		buf[2 + 8] &= 0xa0;
		buf[2 + 8] |= (bufe << 6) | (test_write << 4) | 0x02;

		ret = mode_select10(buf, 60);
		if (sense_data.sense_key) {
			throw 1;
		}

		// 書き込みスピードを設定
		dbg("set_cd_speed 1\n");
		ret = set_cd_speed(0xffff, drive_state.max_w_speed);
		dbg("set_cd_speed 2\n");
		if (!ret) {
			throw 1;
		}

		_int64 fsz = image_sz;

		// 16 セクタサイズにする
		fsz += (2048 * 16) - 1;
		fsz -= fsz % (2048 * 16);

		_int64 bsz = fsz / (2048 * 16);

		ret = reserve_rzone(fsz / 2048);
		if (!ret || sense_data.sense_key) {
			throw 1;
		}

		RZone new_rzone;
		ret = read_disc_info();
		if (!ret || sense_data.sense_key) {
			throw 1;
		}
		ret = read_rzone_inf(drive_state.last_rzone, &new_rzone);
		if (!ret || sense_data.sense_key) {
			throw 1;
		}

		bool eflag = false;
		int i;
		for (i = 0; i < bsz; i++) {
			unsigned char org_imgbuf[2048 * 16 + 0x0f] = {0};
			unsigned char *imgbuf = allign(org_imgbuf);

			_int64 unreadsz;
			while (true) {
				image_cs.Enter();
				// 現在の未読バッファサイズを取得
				fseek(image_fp, 0, SEEK_END);
				_int64 end_p = ftell(image_fp);
				image_cs.Leave();

				unreadsz = end_p - read_p;
				// 32K 以上たまっていたら
				if (unreadsz >= (2048 * 16)) {
					break;
				}
				Sleep(100);
			}

			int rsz;
			image_cs.Enter();
			fseek(image_fp, (unsigned long)read_p, SEEK_SET);
			dbg("[atapi server] fread\n");
			rsz = fread(imgbuf, sizeof (unsigned char), 2048 * 16, image_fp);
			read_p = ftell(image_fp);
			image_cs.Leave();

			if (rsz < 2048 * 16) {
				throw 1;
			}

			do {
				dbg("write10(%d sector)\n", i * 16);
				ret = write10(i * 16, imgbuf, 16);
				if (ret && sense_data.sense_key == 0) {
					break;
				}

				if (is_sensekey(0x05, 0x21, 0x02)) {
					unsigned char org_sensebuf[2048 + 0x0f];
					unsigned char *sensebuf = allign(org_sensebuf);
					ret = request_sense(sensebuf, 2048);
				}
				if (!is_sensekey(0x02, 0x04, 0x08)) {
					dbg("bad sector\n");
					eflag = true;
					break;
				}
			} while (1);
			if (eflag) {
				throw 1;
			}

		}

		do {
			ret = start_stop_unit(0);
			if (is_sensekey(0, 0, 0)) {
				break;
			}
			Sleep(1000);
		} while (1);
	}
	catch (int err) {
		dbg("catch exception\n");
		dbg("%d\n", err);
		ret = 0;
		dbg("error \n");
	}

	return ret;
}


//
int Atapi::start_stop_unit(int flag)
{
	if (!dev) {
		return 0;
	}

	SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER sptdwb;

	//
	if (0 > flag || 3 < flag) {
		return 0;
	}

	unsigned char cdb[16] = {0x1b};
	cdb[4] = 0x03 & flag;

	set_sptdwb(&sptdwb, cdb, 6, SCSI_IOCTL_DATA_UNSPECIFIED, NULL, 0);

	dbg("\n*START/STOP UNIT (0x1b)\n");
	dbg("operation = ");
	if (COM_1B_START_DISC == flag) {
		dbg("START_DISC\n");
	}
	else if (COM_1B_START_DISC_READ_TOC == flag) {
		dbg("START_DISC_READ_TOC\n");
	}
	else if (COM_1B_EJECT == flag) {
		dbg("EJECT\n");
	}
	else if (COM_1B_LOAD_DISC == flag) {
		dbg("LOAD_DISC\n");
	}
	else {
		dbg("UNKOWN\n");
	}

	int ret = send_atapi(&sptdwb);

//	dump(buf, sptdwb.sptd.DataTransferLength);

	return ret;
}

//
int Atapi::test_unit_ready(void)
{
	if (!dev) {
		return 0;
	}

	SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER sptdwb;
//	char orgbuf[SECTOR_SZ + 0x0f] = {0};
//	unsigned char *buf = (unsigned char *)allign((void *)orgbuf);

	unsigned char cdb[16] = {0x00};

	set_sptdwb(&sptdwb, cdb, 6, SCSI_IOCTL_DATA_UNSPECIFIED, NULL, 0);
	dbg("\n*TEST UNIT READY (0x00)\n");

	int ret = send_atapi(&sptdwb);

	return ret;
}


// 0xbd
int Atapi::mechanism_status(void)
{
	if (!dev) {
		return 0;
	}

	static const int sz = 256;
	SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER sptdwb;
	unsigned char cdb[16] = {0xbd};
	cdb[8] = (sz >> 8) & 0xFF;
	cdb[9] = sz & 0xFF;

	unsigned char orgbuf[sz + 0x0f] = {0};
	unsigned char *buf = allign(orgbuf);

	set_sptdwb(&sptdwb, cdb, 12, SCSI_IOCTL_DATA_IN, buf, sz);

	dbg("\n*MECHANISM STATUS\n");

	int ret = send_atapi(&sptdwb);

	dump(buf, sptdwb.sptd.DataTransferLength);

	int c_slot = buf[0] & 0x0F;
	drive_state.tray_open = (buf[1] >> 4) & 0x01;
	int c_lba = (buf[2] << 16 | buf[3] << 8 | buf[4]) & 0xFFFFFF;


	dbg("Current Slot = %d\n", c_slot);

	if (drive_state.tray_open) {
		dbg("Tray Open\n");
	}

	dbg("Current LBA = %d (%03X) \n", c_lba, c_lba);

	return ret;
}


// 0x25
int Atapi::read_capacity(void)
{
	if (!dev) {
		return 0;
	}

	unsigned char orgbuf[8 + 0x0f] = {0};
	unsigned char *buf = allign(orgbuf);

	SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER sptdwb;
	unsigned char cdb[16] = {0x25};

	set_sptdwb(&sptdwb, cdb, 10, SCSI_IOCTL_DATA_IN, buf, 8);

	dbg("\n*READ CAPACITY (0x25)\n");

	int ret = send_atapi(&sptdwb);

	drive_state.capacity = buf[0] << 24 | buf[1] << 16 | buf[2] << 8 | buf[3];
	drive_state.block_length = buf[4] << 24 | buf[5] << 16 | buf[6] << 8 | buf[7];

	dbg("LBA(Capacity) = %d\n", drive_state.capacity);
	dbg("Block Length = %d\n", drive_state.block_length);

	return ret;
}


// 0x28
int Atapi::read10(int sec, unsigned char *buf, int bufsz)
{
	if (!dev) {
		return 0;
	}

	SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER sptdwb;

	int sec_sz = (bufsz + 2047) / 2048;

	unsigned char cdb[16] = {0x28};
	cdb[2] = (sec >> 24) & 0xFF;
	cdb[3] = (sec >> 16) & 0xFF;
	cdb[4] = (sec >> 8) & 0xFF;
	cdb[5] = sec & 0xFF;
	cdb[7] = (sec_sz >> 8) & 0xFF;
	cdb[8] = sec_sz & 0xFF;

	set_sptdwb(&sptdwb, cdb, 10, SCSI_IOCTL_DATA_IN, buf, bufsz);
	dbg("\n*READ(10) (0x28)\n");

	int ret = send_atapi(&sptdwb);
	dbg("sector = %d\n", sec);
	dbg("bufsz = %d\n", bufsz);
	dbg("secsz = %d\n", sec_sz);

	return ret;
}


// 0x51
int Atapi::read_disc_info(void)
{
	if (!dev) {
		return 0;
	}

	const int sz = 256;
	unsigned char orgbuf[sz + 0x0f] = {0};
	unsigned char *buf = allign(orgbuf);

	SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER sptdwb;

	unsigned char cdb[16] = {0x51};
	cdb[7] = (sz >> 8) & 0xFF;
	cdb[8] = sz & 0xFF;

	set_sptdwb(&sptdwb, cdb, 10, SCSI_IOCTL_DATA_IN, buf, sz);

	dbg("\n*READ DISC INFOMATION (0x51)\n");
	int ret = send_atapi(&sptdwb);

	int getsz = (buf[0] << 8) + buf[1];
	dump(buf, getsz + 2);

	int eraseable = (buf[2] >> 4) & 0x01;
	int disc_status = buf[2] & 0x03;
	int rzone_num = buf[3];
	drive_state.first_rzone = (buf[10] << 8) + buf[5];
	drive_state.last_rzone = (buf[11] << 8) + buf[6];

	dbg("eraseable = %d\n", eraseable);

	char *ds_str[] = {"Empty Disc", "Incomplete Disc",
	                "Complete Disc", "Othres"};
	dbg("disc status = %s(%d)\n", ds_str[disc_status], disc_status);

	dbg("rzone num = %d\n", rzone_num);
	dbg("first rzone = %d\n", drive_state.first_rzone);
	dbg("last rzone = %d\n", drive_state.last_rzone);

	return ret;
}


// 0x52
int Atapi::read_rzone_inf(int rzone_n, RZone *rzone)
{
	if (!dev) {
		return 0;
	}

	const int bufsz = 256;
	unsigned char orgbuf[bufsz + 0x0f] = {0};
	unsigned char *buf = allign(orgbuf);

	SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER sptdwb;

	unsigned char cdb[16] = {0x52};
	cdb[1] = 1;
	cdb[2] = (rzone_n >> 24) & 0xff;
	cdb[3] = (rzone_n >> 16) & 0xff;
	cdb[4] = (rzone_n >> 8) & 0xff;
	cdb[5] = rzone_n & 0xff;
	cdb[7] = (bufsz >> 8) & 0xff;
	cdb[8] = bufsz & 0xff;

	set_sptdwb(&sptdwb, cdb, 10, SCSI_IOCTL_DATA_IN, buf, bufsz);

	dbg("\n*READ RZONE INFOMATION (0x52)\n");
	dbg("RZone no = %d\n", rzone_n);
	int ret = send_atapi(&sptdwb);

	int getsz = (buf[0] << 8) + buf[1];
	dump(buf, getsz + 2);


	rzone->rzone_num = buf[2] + (buf[32] << 8);
	rzone->border_num = buf[3] + (buf[33] << 8);
	rzone->damage = (buf[5] >> 5) & 0x01;
	rzone->copy = (buf[5] >> 4) & 0x01;
	rzone->track_mode = buf[5] & 0x0f;
	rzone->rt = (buf[6] >> 7) & 0x01;
	rzone->blank = (buf[6] >> 6) & 0x01;
	rzone->packet_inc = (buf[6] >> 5) & 0x01;
	rzone->fp = (buf[6] >> 4) & 0x01;
	rzone->data_mode = buf[6] & 0x0f;
	rzone->lrb_v = (buf[7] >> 1) & 0x01;
	rzone->nwa_v = buf[7] & 0x01;
	rzone->rzone_start = get_be_data(&buf[8], 4);
	rzone->next_w_add = get_be_data(&buf[12], 4);
	rzone->free_blocks = get_be_data(&buf[16], 4);
	rzone->packet_size = get_be_data(&buf[20], 4);
	rzone->rzone_sz = get_be_data(&buf[24], 4);
	rzone->last_red_add = get_be_data(&buf[28], 4);

	int rzone_state = (rzone->rt << 3) + (rzone->blank << 2)
	                  + (rzone->packet_inc << 1) + rzone->fp;
	char *rzs_str[] = {
		"Complete",                            // 000-
		"Complete",                            // 000-
		"Incomplete or Complete",              // 0010
		"Comlete or Incomplete",               // 0011
		"Invisible",                           // 010-
		"Invisible",                           // 010-
		"Invisible",                           // 0110
		"Invisible",                           // 0111
		"during DAO",                          // 100-
		"during DAO",                          // 100-
		"Parially Recorded Reserved",          // 1010
		"(invalid)",                           // 1011
		"Empty Reserved before start writing", // 110-
		"Empty Reserved before start writing", // 110-
		"Empty Recered",                       // 1110
		"(invalid)",                           // 1111
	};
	dbg("RZone status = %s\n", rzs_str[rzone_state]);

	dbg("RZone num = %d\n", rzone->rzone_num);
	dbg("Border num = %d\n", rzone->border_num);
	dbg("Damage = %d\n", rzone->damage);
	dbg("Copy  = %d\n", rzone->copy);
	dbg("Track Mode = %d\n", rzone->track_mode);
	dbg("RT = %d\n", rzone->rt);
	dbg("Blank = %d\n", rzone->blank);
	dbg("Packet inc = %d\n", rzone->packet_inc);
	dbg("FP = %d\n", rzone->fp);
	dbg("Data Mode = %d\n", rzone->data_mode);
	dbg("LRB_V = %d\n", rzone->lrb_v);
	dbg("NWA_V = %d\n", rzone->nwa_v);
	dbg("RZone Start Address = %d\n", rzone->rzone_start);
	dbg("Next Writable Address= %d\n", rzone->next_w_add);
	dbg("Free Blocks = %d\n", rzone->free_blocks);
	dbg("Packet size = %d\n", rzone->packet_size);
	dbg("RZone Size = %d (sector)\n", rzone->rzone_sz);
	dbg("Last Recorded Address = ");
	if (rzone->lrb_v == 0) {
		dbg("-- ");
	}
	dbg("%d\n", rzone->last_red_add);

	return ret;
}


// 0x5a
int Atapi::mode_sense10(int page_control, int page_code,
                       unsigned char *buf, int bufsz)
{
	if (!dev) {
		return 0;
	}

	SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER sptdwb;

	int sz = bufsz;

	unsigned char cdb[16] = {0x5a};
	cdb[1] = 0x08;
	cdb[2] = ((page_control & 0x03) << 6) + (page_code & 0x3f);
	cdb[7] = (sz >> 8) & 0xff;
	cdb[8] = sz & 0xff;

	set_sptdwb(&sptdwb, cdb, 10, SCSI_IOCTL_DATA_IN, buf, sz);

	dbg("\n*MODE SENSE (10) (0x5a)\n");
//	{
//		int i;
//		for (i = 0; i < 10; i++) {
//			dbg("CDB[%02d] = %02X\n", i, cdb[i]);
//		}
//	}

	int ret = send_atapi(&sptdwb);

//	dump(buf, sptdwb.sptd.DataTransferLength);

	int mpsz = (buf[0] << 8) + buf[1];
	dump(buf, mpsz + 2);

	dbg("Mode Page size = %d (0x%X)\n", mpsz, mpsz);

	int mode_page = buf[8] & 0x3f;
	dbg("Mode Page = 0x%X\n", mode_page);
	if (0x05 == mode_page) {
		int page_len = buf[1 + 8];
		int BUFE = (buf[2 + 8] >> 6) & 0x01;
		int LS_V = (buf[2 + 8] >> 5) & 0x01;
		int TestWrite = (buf[2 + 8] >> 4) & 0x01;
		int write_type = buf[2 +8] & 0x0F;
		int border = (buf[3 + 8] >> 6) & 0x03;
		int FP = (buf[3 + 8] >> 5) & 0x01;
		int Copy = (buf[3 + 8] >> 4) & 0x01;
		int TrackMode = buf[3 + 8] & 0x0F;
		int DataBlockMode = buf[4 + 8] & 0x0F;
		int LinkSize = buf[5 + 8];
		int HostApplicationCode = buf[7 + 8] & 0x3F;
		int SessionFormat = buf[8 + 8];
		int PacketSize = get_be_32(&buf[10 + 8]);
		int AudioPauseLength = get_be_16(&buf[14 + 8]);

		dbg("page_len = %d\n", page_len);
		dbg("BUFE = %d\n", BUFE);
		dbg("LS_V = %d\n", LS_V);
		dbg("TestWrite = %d\n", TestWrite);
		dbg("write_type = %d\n", write_type);
		dbg("border = %d\n", border);
		dbg("FP = %d\n", FP);
		dbg("Copy = %d\n", Copy);
		dbg("TrackMode = %d\n", TrackMode);
		dbg("DataBlockMode = %d\n", DataBlockMode);
		dbg("LinkSize = %d\n", LinkSize);
		dbg("HostApplicationCode = %d\n", HostApplicationCode);
		dbg("SessionFormat = %d\n", SessionFormat);
		if (FP) {
			dbg("PacketSize = %d\n", PacketSize);
		}
		dbg("AudioPauseLength = %d\n", AudioPauseLength);
	}
	// C/DVD Capabilities and Mechanical Status
	else if (0x2a == mode_page) {
		drive_state.rw[DVD_RAM].read = get_bit(buf[2 + 8], 5);
		drive_state.rw[DVD_R].read   = get_bit(buf[2 + 8], 4);
		drive_state.rw[DVD_ROM].read = get_bit(buf[2 + 8], 3);
		drive_state.rw[CD_RW].read   = get_bit(buf[2 + 8], 1);
		drive_state.rw[CD_R].read    = get_bit(buf[2 + 8], 0);

		drive_state.rw[DVD_RAM].write = get_bit(buf[3 + 8], 5);
		drive_state.rw[DVD_R].write   = get_bit(buf[3 + 8], 4);
		drive_state.rw[CD_RW].write   = get_bit(buf[3 + 8], 1);
		drive_state.rw[CD_R].write    = get_bit(buf[3 + 8], 0);

		drive_state.rw[TEST_WRITE].write = get_bit(buf[3 + 8], 2);

		int i;
		for (i = 0; i < 8; i++) {
			if (drive_state.rw[i].read) {
				dbg("%s\n", rw_labels[i][0]);
			}
			if (drive_state.rw[i].write) {
				dbg("%s\n", rw_labels[i][1]);
			}
		}

		int buffer_size = get_be_16(&buf[12 + 8]);
		int c_speed = get_be_16(&buf[28 + 8]);
		int write_speed_table_num = get_be_16(&buf[30 + 8]);

		dbg("Logical unit buffer size = %d\n", buffer_size);
		dbg("Current Write Speed = %d [kbyte/sec]\n", c_speed);
		dbg("Write Speed Table num = %d\n", write_speed_table_num);

		int max = 0;
		for (i = 0; i < write_speed_table_num; i++) {
			int speed = get_be_16(&buf[i * 4 + 28 + 8]);
			dbg("[%d] : [%d/0x%X] %d [kbyte/sec]\n",
			    i, (i * 4 + 28 + 8), (i * 4 + 28 + 8), speed);
			if (max < speed) {
				max = speed;
			}
		}
		if (max == 0) {
			dbg("[WARNING!] max speed is ZERO\n");
			return false;
		}
		drive_state.max_w_speed = max;

	}
	else {
		dbg("other page code\n");
	}

	return ret;
}


// 0x55
int Atapi::mode_select10(unsigned char *buf, int bufsz)
{
	if (!dev) {
		return 0;
	}

	SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER sptdwb;

	int size = ((buf[0] << 8) + buf[1]) + 2;

	unsigned char cdb[16] = {0x55};
	cdb[1] = 0x10;
	cdb[7] = (size >> 8) & 0xff;
	cdb[8] = size & 0xff;

	set_sptdwb(&sptdwb, cdb, 10, SCSI_IOCTL_DATA_OUT, buf, size);

	dbg("\n*MODE SELECT (10) (0x55)\n");

	int ret = send_atapi(&sptdwb);

	dump(buf, sptdwb.sptd.DataTransferLength);
	return ret;
}


// 0x2a
// @param sec セクタ番号
// @param buf バッファ
// @param sec_n セクタ数 (sec_n * 2048 = バッファサイズ)
int Atapi::write10(int sec, unsigned char *buf, int sec_n)
{
	if (!dev) {
		return 0;
	}

	SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER sptdwb;

	unsigned char cdb[16] = {0x2a};
	cdb[2] = (sec >> 24) & 0xFF;
	cdb[3] = (sec >> 16) & 0xff;
	cdb[4] = (sec >> 8) & 0xff;
	cdb[5] = sec & 0xFF;
	cdb[7] = (sec_n >> 8) & 0xff;
	cdb[8] = sec_n & 0xff;

	set_sptdwb(&sptdwb, cdb, 10, SCSI_IOCTL_DATA_OUT, buf, sec_n * 2048);

	dbg("\n*WRITE (10) (0x2a)\n");
	dbg("sector = %d, size = %d\n", sec, sec_n * 2048);

	int ret = send_atapi(&sptdwb);

	return ret;
}


// 0x53
int Atapi::reserve_rzone(_int64 sz)
{
	if (!dev) {
		return 0;
	}

	SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER sptdwb;

	unsigned char cdb[16] = {0x53};
	cdb[5] = (unsigned char)(sz >> 24) & 0xff;
	cdb[6] = (unsigned char)(sz >> 16) & 0xff;
	cdb[7] = (unsigned char)(sz >> 8) & 0xff;
	cdb[8] = (unsigned char)sz & 0xff;

	set_sptdwb(&sptdwb, cdb, 10, SCSI_IOCTL_DATA_OUT, NULL, 0);

	dbg("\n*RESERVE RZone (0x53)\n");

	int ret = send_atapi(&sptdwb);
	return ret;
}


// 0xbb
int Atapi::set_cd_speed(int r_speed, int w_speed)
{
	dbg(">set_cd_speed\n");
	if (!dev) {
		dbg("dev is null\n");
		return 0;
	}

	SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER sptdwb;

	unsigned char cdb[16] = {0xbb};
	cdb[2] = (r_speed >> 8) & 0xff;
	cdb[3] = r_speed & 0xff;
	cdb[4] = (w_speed >> 8) & 0xff;
	cdb[5] = w_speed & 0xff;

	set_sptdwb(&sptdwb, cdb, 12, SCSI_IOCTL_DATA_OUT, NULL, 0);

	dbg("\n*SET CD SPEED (0xbb)\n");

	int ret = send_atapi(&sptdwb);
	dbg("set_cd_speed end %d\n", ret);
	return ret;
}


// 0x03
int Atapi::request_sense(unsigned char *buf, int bufsz)
{
	if (!dev) {
		return 0;
	}

	SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER sptdwb;
	unsigned char cdb[16] = {0x03};
	cdb[4] = 0xff;

	set_sptdwb(&sptdwb, cdb, 6, SCSI_IOCTL_DATA_IN, buf, 0xff);

	dbg("\n*REQUEST SENSE (0x03)\n");

	int ret = send_atapi(&sptdwb);

	int len = buf[7];
	dump(buf, len + 18);

	return ret;
}

// 0x2b
int Atapi::seek(int sec)
{
	if (!dev) {
		return 0;
	}

	SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER sptdwb;
	unsigned char cdb[16] = {0x2b};
	cdb[2] = (sec >> 24) & 0xFF;
	cdb[3] = (sec >> 16) & 0xFF;
	cdb[4] = (sec >> 8) & 0xFF;
	cdb[5] = sec & 0xFF;

	set_sptdwb(&sptdwb, cdb, 10, SCSI_IOCTL_DATA_UNSPECIFIED, NULL, 0);
	dbg("\n*SEEK (0x2b)\n");

	int ret = send_atapi(&sptdwb);

	return ret;
}


// 0x46
int Atapi::get_configuration(int rt, int sfn, unsigned char *buf, int bufsz)
{
	if (!dev) {
		return 0;
	}

	SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER sptdwb;
	unsigned char cdb[16] = {0x46};
	cdb[1] = rt & 0x03;
	cdb[2] = (sfn >> 8) & 0xff;
	cdb[3] = sfn & 0xff;
	cdb[7] = (bufsz >> 8) & 0xff;
	cdb[8] = bufsz & 0xff;

	set_sptdwb(&sptdwb, cdb, 10, SCSI_IOCTL_DATA_IN, buf, bufsz);

	dbg("\n*GET CONFIGURATION (0x46)\n");
	dbg("rt = %d, sfn = %d\n", rt, sfn);

	int ret = send_atapi(&sptdwb);

	return ret;
}


//
/**
	GET CONFIGURATION で取得したデータを解析する
	@param buf   取得したデータ
	@param bufsz buf のバイトサイズ
	@return 実行結果
		@retval 1 成功
		@retval 0 失敗
*/
int Atapi::analyz_configuration_info(unsigned char *buf, int bufsz)
{
	int idx = 0;

	// Feature Header
	int data_length = get_be_32(&buf[0]);
	int current_profile = get_be_16(&buf[6]);

	dbg("[%03X]\n", 0);
	dump(buf, 8);
	dbg("data_length = %d\n", data_length);
	dbg("current_profile = %d\n", current_profile);

	// 個々の Feature Descriptor
	idx = 8;
	while (true) {
		// 共通部分
		int feature_code = get_be_16(&buf[idx + 0]);
		int persistent = get_bit(buf[idx + 2], 1);
		int current = get_bit(buf[idx + 2], 0);
		int additional_length = buf[idx + 3];

		dbg("[%03X]\n", idx);
		dump(&buf[idx], 4);
		dbg("feature_code = %d(0x%X)\n", feature_code, feature_code);
		dbg("persistent = %d\n", persistent);
		dbg("current = %d\n", current);
		dbg("additional_length = %d\n", additional_length);

		// DVD-R/RW Write Feature Descriptor
		if (0x002f == feature_code && current) {
			drive_state.buf_e = get_bit(buf[idx + 4], 6);
		}

		dump(&buf[idx + 4], additional_length);
		dbg("\n\n");

		idx += 4 + additional_length;

		if (idx > data_length || idx > 2048) {
			dbg("end(idx = %d)\n", idx);
			break;
		}
	}

	return 1;
}



// lp を 4 バイト境界にあわせる
unsigned char *Atapi::allign(unsigned char *ptr)
{
	return ((unsigned char *)(((DWORD)ptr + 0x0f) & ~0x0f));
}


// ATAPI コマンドの送信結果を出力
void Atapi::print_sptwb_result(BOOL status, DWORD returned,
                        SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER *sptdwb)
{
	dbg("status = %d, returned = %d\n", status, returned);
	dbg("ScsiStatus = %s\n", scsistat_str(sptdwb->sptd.ScsiStatus));

	// Sense データがあれば
	if (sptdwb->sptd.ScsiStatus) {
		dbg("Sense data\n");
		dump(sptdwb->ucSenseBuf, sptdwb->sptd.SenseInfoLength);
		print_sense(sptdwb->ucSenseBuf, sptdwb->sptd.SenseInfoLength);
	}
}

void Atapi::print_sense(unsigned char *buf, int bufsz)
{
	int error_code = buf[0] & 0x7f;
	int sense_key = buf[2] & 0x0f;
	int asc = buf[12];
	int ascq = buf[13];

	int infomation = get_be_16(&buf[3]);

	int sksv = get_bit(buf[15], 7);
	int sks_val = get_be_16(&buf[16]);

//	bool last_flag = print_log;
//	print_log = true;

	if (0x70 != error_code && 0x71 != error_code) {
		dbg("Unkown error code (%02X)\n", error_code);
	}

	dbg("Sense data = %02X/%02X/%02X\n", sense_key, asc, ascq);
	dbg("Infomation = %d (0x%04X)\n", infomation, infomation);

	if (sksv) {
		dbg("Sense-key Specific = %d (0x%04X)\n", sks_val, sks_val);
	}

//	print_log = last_flag;

	return;
}

char *Atapi::scsistat_str(int no)
{
	if (no == 0x00) {
		return "SCSISTAT_GOOD";
	}
	if (no == 0x02) {
		return "SCSISTAT_CHECK_CONDITION";
	}
	if (no == 0x04) {
		return "SCSISTAT_CONDITION_MET";
	}
	if (no == 0x08) {
		return "SCSISTAT_BUSY";
	}
	if (no == 0x10) {
		return "SCSISTAT_INTERMEDIATE";
	}
	if (no == 0x14) {
		return "SCSISTAT_INTERMEDIATE_COND_MET";
	}
	if (no == 0x18) {
		return "SCSISTAT_RESERVATION_CONFLICT";
	}
	if (no == 0x22) {
		return "SCSISTAT_COMMAND_TERMINATED";
	}
	if (no == 0x28) {
		return "SCSISTAT_QUEUE_FULL";
	}

	return "UNKOWN_ERROR";
}



void Atapi::dump(void *start, int size)
{
	dbg("      00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f\n");
	dbg("------------------------------------------------------");

	int i;
	for (i = 0; i < size; i++) {
		if ((i % 16) == 0) {
			dbg("\n%03X : ", i);
		}
		dbg("%02X ", (unsigned int)(((unsigned char *)start)[i]));
	}
	dbg("\n\n");
}

// コマンド構造体を設定
// sptdwb  ( /o)
// cdb     (i/ ) ATAPI コマンドパケット
// cdb_sz  (i/ ) CDB のサイズ
// in_out  (i/ ) SCSI_IOCTL_DATA_XXX (IN/OUT/UNSPECIFIED)
// buf     (i/ ) バッファ
// buf_sz  (i/ ) バッファサイズ
void Atapi::set_sptdwb(SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER *sptdwb,
                unsigned char *cdb, int cdb_sz,
                int in_out,
                void *buf, int buf_sz)
{
	memset(sptdwb, 0, sizeof (SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER));
	sptdwb->sptd.Length = sizeof (SCSI_PASS_THROUGH_DIRECT);
	sptdwb->sptd.PathId = 0;    // 設定しても無効
	sptdwb->sptd.TargetId = 0;
	sptdwb->sptd.Lun = 0;
	sptdwb->sptd.CdbLength = cdb_sz;
	sptdwb->sptd.DataIn = in_out;
	sptdwb->sptd.SenseInfoLength = 32; // ucSenseBuf を使うサイズ?
	sptdwb->sptd.DataTransferLength = buf_sz;
	sptdwb->sptd.TimeOutValue = 30; // タイムアウト(秒)
	sptdwb->sptd.DataBuffer = buf;
	sptdwb->sptd.SenseInfoOffset = offsetof(SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER, ucSenseBuf);

	int i;
	for (i = 0; i < cdb_sz; i++) {
		sptdwb->sptd.Cdb[i] = cdb[i];
	}

	return;
}


int Atapi::send_atapi(SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER *sptdwb)
{
	send_cs.Enter();
	memset(&sense_data, 0, sizeof (sense_data));
	DWORD len = sizeof (SCSI_PASS_THROUGH_DIRECT_WITH_BUFFER);

	DWORD returned  = 0;
	int status = DeviceIoControl(dev,
	                             IOCTL_SCSI_PASS_THROUGH_DIRECT,
	                             sptdwb, len,
	                             sptdwb, len,
	                             &returned, NULL);
	error_no = GetLastError();

	print_sptwb_result(status, returned, sptdwb);

	unsigned char *buf = sptdwb->ucSenseBuf;

	if (sptdwb->sptd.ScsiStatus && sptdwb->sptd.SenseInfoLength) {
		sense_data.sense_key = buf[2] & 0x0f;
		sense_data.asc = buf[12];
		sense_data.ascq = buf[13];
	}

	send_cs.Leave();

	return status;
}


int Atapi::get_be_16(unsigned char *buf)
{
	return get_be_data(buf, 2);
}

int Atapi::get_be_32(unsigned char *buf)
{
	return get_be_data(buf, 4);
}


int Atapi::get_be_data(unsigned char *buf, int sz)
{
	int ret = 0;
	int i;
	for (i = 0; i < sz; i++) {
		ret = (ret << 8) + buf[i];
	}

	return ret;
}


int Atapi::get_bit(unsigned char buf, int bit)
{
	return (buf >> bit) & 0x01;
}

//void Atapi::dbg(char *fmt, ...)
//{
//	log_cs.Enter();
//
//	int r;
//	va_list va;
//	char tmp[1024];
//
//	va_start(va, fmt);
//	r = _vsnprintf(tmp, 1023, fmt, va);
//	va_end(va);
//
//	::dbg(tmp);
//
//	log_cs.Leave();
//}

int Atapi::is_sensekey(int sense_key, int asc, int ascq)
{
	if (sense_data.sense_key == sense_key
	        && sense_data.asc == asc && sense_data.ascq == ascq) {
		return true;
	}

	return false;
}


// イメージ書き込みの準備を行う
int Atapi::init_write_image(void)
{
	if (image_writing) {
		return 0;
	}

	image_fp = fopen("recv.img", "wb");
	if (NULL == image_fp) {
		return 0;
	}

	fclose(image_fp);
	image_fp = fopen("recv.img", "a+b");
	if (NULL == image_fp) {
		return 0;
	}

	image_writing = true;
	read_p = 0;

	return 1;
}


int Atapi::finish_write_image(void)
{
	if (!image_writing) {
		return 0;
	}

	if (image_fp) {
		fclose(image_fp);
		image_fp = NULL;
	}

	image_writing = false;

	return 1;
}


int Atapi::add_buf(unsigned char *buf, int sz)
{
	if (!image_writing) {
		return 0;
	}

	image_cs.Enter();

	fwrite(buf, sizeof (unsigned char), sz, image_fp);

	image_cs.Leave();

	return 1;
}







