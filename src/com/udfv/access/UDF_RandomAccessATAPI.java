/*
   Copyright 2013 Heart Solutions Inc.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
//

package com.udfv.access;

import java.net.*;
import java.lang.*;
import java.io.*;
import java.util.*;
import com.udfv.access.*;
import com.udfv.exception.*;




/**
	ソケットを通して ATAPI デバイスにアクセスするクラス
*/
public class UDF_RandomAccessATAPI extends UDF_RandomAccess
{
	private Socket conn;
	private InputStream inp_s;
	private OutputStream out_s;

	private long fp;
//	private boolean eof_f;
	private long disc_sz;

	private long cache_sector;
	private byte [] cache_buf;

	/**
		コンストラクタ
		@param addr  接続先アドレスの文字列
		@param drive ドライブレター (ex. "D" "E")
	*/
	public UDF_RandomAccessATAPI(String addr, String drive) throws UDF_DeviceNotOpenException, IOException  {
		connect(addr);
		open(drive);
		read_disc_info();
		cache_sector = -1;
		cache_buf = new byte [2048];
	}

	/**
		ATAPIServerアプリと接続
		@param addr 接続先アドレスの文字列
	*/
	private void connect(String addr) throws IOException, SecurityException, UDF_DeviceNotOpenException {
		//System.out.println("connect(" + addr + ")");
		try {
			conn = new Socket(addr, 12345);
		}
		catch (Exception e) {
			throw new UDF_DeviceNotOpenException();
		}
		conn.setSoTimeout(60000); // msec
		inp_s = conn.getInputStream();
		out_s = conn.getOutputStream();
	}

	/**
		File#open 的な open
		@param drive ドライブ
	*/
	private void open(String drive) throws UDF_DeviceNotOpenException {
//		System.out.println("open(" + drive + ")");
		if (0 >= drive.length()) {
			throw new UDF_DeviceNotOpenException();
		}
		String devicename = drive.substring(0, 1) + ":\\";
		try {
			open_device(devicename);
		}
		catch (Exception e) {
			throw new UDF_DeviceNotOpenException();
		}
		fp = 0;
//		eof_f = false;
	}

	/**
		ディスク情報の読み取り
	*/
	public void read_disc_info() throws IOException {
		String msg = "READINFO";
		StringBuffer ret_str = new StringBuffer("");
		int ret_val = send_message(msg, ret_str);

		if (ret_val == 0) {
			throw new IOException("can't send packet");
		}

		if (ret_val == -1) {
			return;
		}

		String str = ret_str.toString().trim();
//		System.out.println("READINFO ret_str = " + ret_str);
//		str.trim();
		disc_sz = Long.parseLong(str, 10);
		//System.out.println("disc size = " + disc_sz);
	}


	/**
		ATAPIServer でドライブをオープンsする
		@param drive ドライブ文字列
	*/
	private void open_device(String drive) throws UDF_DeviceNotOpenException, IOException {
		String msg = "OPEN " + drive;
		StringBuffer ret_str = new StringBuffer("");
		int ret_val = send_message(msg, ret_str);

		if (ret_val == 0 || !ret_str.toString().equals("OK")) {
			throw new UDF_DeviceNotOpenException();
		}

	}


	public int read(byte [] b, int off, int len) throws IOException {
//		if (eof_f) {
//			return -1;
//		}
		if (fp >= disc_sz) {
			return -1;
		}

//		boolean test = false;
//
//		System.out.println("read() " + off + ", " + len);
//		if (fp == (long)4601542656L) {
//			System.out.println("okasii?\n");
//			test = true;
//			System.out.println("read() " + off + ", " + len);
//		}


		// 開始セクタと終了セクタ
		long s_sec = (long)(fp / 2048.0);

		long e_fp = (long)(fp + len);
		if (e_fp > disc_sz) {
			e_fp = (long)disc_sz;
		}

		long e_sec = (long)((e_fp + 2047) / 2048.0);

//		if (test) {
//			System.out.println("s_sec = " + s_sec);
//			System.out.println("e_sec = " + e_sec);
//		}

		int readsz = 0;
		long i;
		for (i = s_sec; i < e_sec; i++) {
			byte buf[] = new byte [2048];
//			System.out.println("read_sector() sector = " + i);
			int sz = read_sector(buf, (int)i);
			// EOF
			if (sz == -1) {
				System.out.println("EOF");
//				eof_f = true;
				break;
			}

//			System.out.println("size = " + sz);
			if (sz < 2048) {
				String msg = new String("Can't read sector " + i);
				throw new IOException(msg);
			}

			// 読み込んだセクタ先頭のファイルポインタ
			long s_idx;
			s_idx = i * 2048;

			int s_off = 0;
			if (i == s_sec && fp > s_idx) {
				s_off = (int)(fp - s_idx);
			}
			int j;
			for (j = s_off; j < 2048; j++) {
				if ((j + s_idx) >= (fp + len)) {
					break;
				}
				b[readsz + off] = buf[j];
//				System.out.println("copy buf[" + j + "] -> b["
//				                 + (readsz + off)
//				                 + "] : val = " + b[readsz + off]);
				readsz++;
			}
		}
//		System.out.println(" ");
		fp += readsz;
//		System.out.println("readsize = " + readsz + "  fp = " + fp);

		return readsz;
	}

	/**
		データを書く。
	*/
	public int write(byte [] b, int off, int len) throws IOException {
		throw new IOException();
	}

	/**
		ATAPIServer にイメージを書き込む
		@param name ファイル名
	*/
	public long write_image(String name) throws FileNotFoundException
	{
		RandomAccessFile in;
		try {
			in = new RandomAccessFile(name, "r");
		}
		catch (FileNotFoundException e) {
			System.out.println("File not open");
			throw e;
		}

		long wsz = 0;
		try {
			init_writeimage(in.length());
			Thread self_thread = Thread.currentThread();
			self_thread.sleep(5000);

			long bno = in.length() + (2048 * 16 - 1);
			bno -= bno % (2048 * 16);
			bno /= (2048 * 16);

			long i;
			for (i = 0; i < bno; i++) {
				byte buf[] = new byte [2048 * 16];
				int sz = in.read(buf, 0, (2048 * 16));
				if (sz == -1) {
					break;
				}

				send_buf(buf, sz);
				wsz += sz;
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wsz;
	}


	public long seek(long off, int whence) throws IOException {
//		System.out.println("off = " + off + " whence = " + whence);
		if (SEEK_SET == whence) {
			if (off < 0) {
				throw new IOException();
			}

			fp = off;
		}

		else if (SEEK_CUR == whence) {
			if ((fp + off) < 0) {
				throw new IOException();
			}

			fp = fp + off;
		}

		else if (SEEK_END == whence) {
//			System.out.println("fp = " + fp + "  off = " + off + " whence = " + whence);
//			System.out.println((disc_sz + off));
//			if ((disc_sz + fp) > disc_sz || (disc_sz + fp) < 0) {
			if ((disc_sz + off) < 0) {
				throw new IOException();
			}

			fp = disc_sz + off;
//			System.out.println("fp = " + fp);

		}

		//System.out.println("fp = " + fp);
//		if (fp == disc_sz) {
//			eof_f = true;
//		}
//		else {
//			eof_f = false;
//		}

		return fp;
	}

	public long getPointer() {
//		System.out.println("UDF_RandoamAccessATAPI#getPointer() = " + fp);
		return fp;
	}

	public long getAbsPointer() throws IOException {
		return fp;
//		throw new IOException();
	}

	public long getPartPointer() throws IOException {
		return fp;
	}


	public void close() throws IOException {
		conn.close();
	}

	public boolean eof() {
		if (fp >= disc_sz) {
			return true;
		}
		else {
			return false;
		}
	}

	public long length() {
//		System.out.println("length = " + disc_sz);
		return disc_sz;
	}

	public int getPartRefNo() {
		return -1;
	}

    /*
	public boolean isMirror() throws IOException {
		return false;
	}
    */
	public int getPartSubno() throws IOException {
		return 0;
	}

	/**
		指定されたセクタを読み込む
		@param b バッファ
		@param sector セクタ番号
		@return 実際に読み込まれたバイス数
			@retval 0以上 成功
			@retval -1    EOF
	*/
	private int read_sector(byte [] b, int sector) throws IOException {
		int i;
		if (sector == cache_sector) {
			for (i = 0; i < 2048; i++) {
				b[i] = cache_buf[i];
			}
			return 2048;
		}

		// 転送文字列を作成
		String msg = "READSECTOR ";
		msg += String.valueOf(sector);

//		String ret_str = "hoge"; // = new String();
		StringBuffer ret_str = new StringBuffer("");
		int ret_val = send_message(msg, ret_str);

		//System.out.println("ret_str = " + ret_str);
		if (ret_val == 0) {
//			System.out.println("send_str = " + msg);
//			System.out.println("ret_str = " + ret_str);
			String estr = new String("Can't send packet \"" + msg + "\"");

			throw new IOException(estr);
		}

		if (ret_val == -1) {
			return -1;
		}

		// 受信文字列解析
		int idx = 0;
		int bidx = 0;
//		System.out.println(ret_str.length());
		while (true) {
			int next_idx = ret_str.indexOf(" ", idx + 1);
			if (next_idx == -1) {
				break;
			}
			String tmp = ret_str.substring(idx, next_idx);
			idx = next_idx + 1;
			if (tmp.length() != 2) {
				break;
			}

			int val = Integer.parseInt(tmp, 16);
			cache_buf[bidx++] = (byte)val;
		}

		cache_sector = sector;

		for (i = 0; i < 2048; i++) {
			b[i] = cache_buf[i];
		}

		return bidx;
	}

	/**
		イメージの書き込み準備を行う
		@param bufsz 書き込むイメージのバイト数
	*/
	private void init_writeimage(long bufsz) throws IOException
	{
		// 転送文字列を作成
		String msg = "WRITEIMAGE " + String.valueOf(bufsz);

		StringBuffer ret_str = new StringBuffer("");
		int ret_val = send_message(msg, ret_str);

		if (ret_val == 0) {
			throw new IOException("can't send packet");
		}

		if (!ret_str.toString().equals("OK")) {
			throw new IOException("can't device initialize");
		}
	}

	/**
		イメージ書き込み用のバッファを転送する
		@param buf バッファ
		@param sz  バッファのバイト数
	*/
	private void send_buf(byte [] b, int sz) throws IOException
	{
		String pre = "WRITE ";

		StringBuffer buf_str = new StringBuffer("");
		int i;
		for (i = 0; i < sz;) {
			if (0 == (i % 1000)) {
//				System.out.println(i + " / " + sz);
			}
			int j;
			buf_str.delete(0, buf_str.length());
			for (j = 0; j < 256; j++) {
				if (i >= sz) {
					break;
				}
				StringBuffer valstr = new StringBuffer("00");
				valstr.append(Integer.toHexString((int)b[i++]));
				buf_str.append(valstr.substring(valstr.length() - 2) + " ");
			}

			String msg = pre + buf_str.toString();
//			System.out.println(msg);

			StringBuffer ret_str = new StringBuffer("");
			int ret_val = send_message(msg, ret_str);
//			System.out.println(ret_str.toString());
			if (ret_val == 0 || !ret_str.toString().equals("OK")) {
				throw new IOException("can't send packet");
			}
		}
	}

	/**
		ATAPIServer にコマンドを送り、結果を受信する
		@param in  送信メッセージ
		@param out 受信メッセージ
		@return 受信女セージの文字列長さ
	*/
	private int send_message(String in, StringBuffer out) throws IOException {
		//System.out.println("> send_message " + in);
//		System.out.println(in);

		// 送信文字列を byte 配列に変換
		byte tmp[];
		try {
			tmp = in.getBytes("UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			throw new IOException();
		}

		byte msg[] = new byte [tmp.length + 1];
		int i;
		for (i = 0; i < tmp.length; i++) {
			msg[i] = tmp[i];
		}
		msg[i] = 0;

		// 送信
		out_s.write(msg, 0, msg.length);

		// 受信
//		Vector vecbuf = new Vector(0);
//		StringBuffer sbuf = new StringBuffer(0);
		out.delete(0, out.length());
		boolean eop = false;
		int sumsz = 0;
		while (true) {
			byte buf[] = new byte [256];
			int sz = inp_s.read(buf);
			sumsz += sz;
//			System.out.println(sz + " " + sumsz);
			// EOF?
			if (sz == -1) {
				break;
			}

			for (i = 0; i < sz; i++) {
				if (buf[i] == 0) {
					eop = true;
					break;
				}
				out.append(new char [] {(char)buf[i]});

			}
			if (eop) {
				break;
			}

			if (sz != 0 && i == sz) {
//				System.out.println("continue");
				continue;
			}
		}

		return out.length();
	}
}
