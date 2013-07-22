package com.udfv.ecma167;

import java.io.*;
import java.util.*;

import com.udfv.exception.UDF_InternalException;
import com.udfv.util.UDF_Util;
import com.udfv.core.*;


/**
  ビットデータ管理を行うクラス。

*/
public class UDF_bitmap extends UDF_bytes {

    //　ON/OFFビット数カウント用　//
    private static final byte [] count_table = {
         0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4, 
         1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 
         1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 
         2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 
         1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 
         2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 
         2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 
         3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 
         1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 
         2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 
         2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 
         3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 
         2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 
         3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 
         3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 
         4, 5, 5, 6, 5, 6, 6, 7, 5, 6, 6, 7, 6, 7, 7, 8, 
    };

    //　unset() 用　//
    private static final byte [] bitmask = {
         -2,  -3,  -5,  -9,    // 0xfe, 0xfd, 0xfb, 0xf7, //
        -17, -33, -65, 127     // 0xef, 0xdf, 0xbf, 0x7f //
    };

    /*  ビット総数  */
    int numOfBits;


    /**
      コンストラクタ。

      @param sz ビット総数。
    */
    public UDF_bitmap(UDF_Element elem, String prefix, String name, int sz){

        super(elem, prefix, name == null ? "UDF_bitmap" : name, (sz + 7) / 8);

        numOfBits = sz;

        for (int i = 0; i < getSize(); i++) {
            my_data[i] = 0;
        }
    }

    /**
       UDF_ElementBase#genElement()が使用するためのコンストラクタ。

       @deprecated replaced by {@link #UDF_bitmap(UDF_Element, String, String, int)}
     */
    public UDF_bitmap(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name);
	numOfBits = 0;
    }

    /**
      管理するビット総数を変更する。

      @param sz ビット数。
      @throws UDF_InternalException 負の値が指定された。
    */
    public void setBitSize(int sz) throws UDF_InternalException {

        if (sz < 0) {
            throw new UDF_InternalException(this, "Negative : " + sz);
        }

        numOfBits = sz;
        int size = (sz + 7) / 8;
        super.setSize(size);
    }

    /**
      管理しているビット総数を取得する。

      @return ビット総数。
    */
    public int getBitSize( ) {
        return numOfBits;
    }

    /**
      ビット管理のために必要なバイト配列のサイズを設定する。

      @param sz サイズ（バイト単位）
    */
    public void setSize(int sz) {

        super.setSize(sz);
        numOfBits = getSize() * 8;
    }

    /**
      ビット情報をバイト配列の形式で受け取る。

      <p>
      バイト配列は内部で複写される。
      </p>

      @param b ビット情報のバイト配列。
    */
    public void setData(byte[] b) {

        super.setData(b);
        numOfBits = getSize() * 8;
    }


    /**
      範囲チェック。

      @param n ビットの番目
      @throws UDF_InternalException 管理されているビットの範囲外の位置が指定された。
    */
    private void checkRange(int n) throws UDF_InternalException {

        if (n < 0) {
            throw new UDF_InternalException(this, "Negative : " + n);
        }
        if (n >= numOfBits) {
            throw new UDF_InternalException(this, "Out of range : " + n + " > " + numOfBits);
        }
    }

    /**
      指定番目のビットがONかどうか。

      @param n ビットの番目。
      @return ビットがONだったらtrue、OFFだったらfalse。
    */
    public boolean isSet(int n) throws UDF_InternalException {

        checkRange(n);

        return isSetNoCheck(n);
    }

    private boolean isSetNoCheck(int n) {
        return ((my_data[n / 8] & (1 << (n % 8))) != 0);
    }

    /**
       n番目のビットから len個ビットが ONかどうか。

      @param n ビットの番目。
      @return ビットがONだったらtrue、OFFだったらfalse。
    */
    public boolean isSet(int n, int len) throws UDF_InternalException {

//        checkRange(n);
//        checkRange(n + len - 1);

        while(len > 0) {

            if(!isSet(n++))
                return false;
            --len;
        }
        return true;
    }

    /**
      指定番目のビットをONにする。

      @param n ONにしたいビットの番目。
      @throws UDF_InternalException 指定の番目が管理外。
    */
    public void set(int n) throws UDF_InternalException {

        checkRange(n);

        my_data[n / 8] |= 1 << (n % 8);
    }


    /**
      指定番目のビットから指定の個数のビットをONにする。

      lenが0のときは何もしない。

      @param n ONにしたい最初のビットの番目
      @param len ONにしたいビットの個数
      @throws UDF_InternalException 指定の番目が管理外。
    */
    public void set(int n, int len) throws UDF_InternalException {

        checkRange(n);
        checkRange(n + len - 1);

        while(len > 0) {

            int nn = n % 8;
            if(nn == 0) {
                break;
            }

            my_data[n / 8] |= (1 << nn);
            --len;
            ++n;
        }

        //　バイト単位に処理できるところ　//
        int count = len / 8;
        for ( ; count > 0; count--) {

            my_data[n / 8] = -1;
            len -= 8;
            n += 8;
        }

        for ( ; len > 0; ) {

            int nn = n % 8;

            my_data[n / 8] |= (1 << nn);
            --len;
            n++;
        }
    }

    /**
      指定番目のビットをOFFにする。

      @param n ONにしたいビットの番目。
      @throws UDF_InternalException 指定の番目が管理外。
    */
    public void unset(int n) throws UDF_InternalException {

        checkRange(n);

        my_data[n / 8] &= bitmask[n % 8];
    }

    /**
      指定番目のビットから指定の個数のビットをOFFにする。

      lenが0のときは何もしない。

      @param n ONにしたいビットの番目。
      @throws UDF_InternalException 指定の番目が管理外。
    */
    public void unset(int n, int len) throws UDF_InternalException {

	if(len == 0)
	    return;

        checkRange(n);
        checkRange(n + len - 1);

        while(len > 0) {

            int nn = n % 8;
            if(nn == 0) {
                break;
            }

            my_data[n / 8] &= bitmask[nn];
            --len;
            ++n;
        }

        //　バイト単位に処理できるところ　//
        int count = len / 8;
        for ( ; count > 0; count--) {

            my_data[n / 8] = 0;
            len -= 8;
            n += 8;
        }

        for ( ; len > 0; ) {

            my_data[n / 8] &= bitmask[n % 8];
            --len;
            n++;
        }
    }

    /**
       ExtentElemで示される領域を1にする。

       EEの loc および lenのみを見る。どこのパーティションに属しているかは、このメソッドではケアしない。
     */
    public void set(UDF_ExtentElem ee) throws UDF_InternalException{
	set(ee.getLoc(), (int)(UDF_Util.align(ee.getLen(), env.LBS) / env.LBS));
    }

    /**
       ExtentElemで示される領域を0にする。

       EEの loc および lenのみを見る。どこのパーティションに属しているかは、このメソッドではケアしない。
     */
    public void unset(UDF_ExtentElem ee) throws UDF_InternalException{
	unset(ee.getLoc(), (int)(UDF_Util.align(ee.getLen(), env.LBS) / env.LBS));
    }

    /**
      指定番目のビットから指定の個数のビットをONにする。

      @param n ONにしたい最初のビットの番目
      @param len ONにしたいビットの個数
      @throws UDF_InternalException 指定の番目が管理外。
    */
    public void set(int n, long len) throws UDF_InternalException {
	set(n, (int)len);
    }

    /**
      指定番目のビットから指定の個数のビットをONにする。

      @param n ONにしたい最初のビットの番目
      @param len ONにしたいビットの個数
      @throws UDF_InternalException 指定の番目が管理外。
    */
    public void unset(int n, long len) throws UDF_InternalException {
	unset(n, (int)len);
    }

    /**
      nビット以降でOFFであるビットを検索する。

      @param n ビット番号

      @throws UDF_InternalException 指定の番目が管理外。
    */
    public int searchBitOff(int n) throws UDF_InternalException {

        checkRange(n);

        for ( ; n < numOfBits; n++) {

            if ((n % 8) == 0) {
                break;
            }

            if (!isSetNoCheck(n)) {
                return n;
            }
        }

        for ( ; n < numOfBits; n += 8) {
            if (my_data[n / 8] != -1) {
                break;
            }
        }

        for ( ; n < numOfBits; n++) {

            if (!isSetNoCheck(n)) {
                return n;
            }
        }

        throw new UDF_InternalException(this, "NOT FOUND OFF-BIT");
    }

    /**
      nビット以降でONであるビットを検索する。

      @param n ビット番号

      @throws UDF_InternalException 指定の番目が管理外。
    */
    public int searchBitOn(int n) throws UDF_InternalException {

        checkRange(n);

        for ( ; n < numOfBits; n++) {

            if ((n % 8) == 0) {
                break;
            }

            if (isSetNoCheck(n)) {
                return n;
            }
        }

        for ( ; n < numOfBits; n += 8) {
            if (my_data[n / 8] != 0) {
                break;
            }
        }

        for ( ; n < numOfBits; n++) {

            if (isSetNoCheck(n)) {
                return n;
            }
        }

        throw new UDF_InternalException(this, "NOT FOUND ON-BIT");
    }

    /**
      nビット以前でOFFであるビットを検索する。

      @param n ビット番号

      @throws UDF_InternalException 指定の番目が管理外。
    */
    public int searchBackBitOff(int n) throws UDF_InternalException {

        checkRange(n);

        for ( ; n >= 8; n--) {

            if ((n % 8) == 7) {
                break;
            }

            if (!isSetNoCheck(n)) {
                return n;
            }
        }

        for ( ; n >= 8; n -= 8) {
            if (my_data[n / 8] != -1) {
                break;
            }
        }

        for ( ; n >= 0; n--) {

            if (!isSetNoCheck(n)) {
                return n;
            }
        }

        throw new UDF_InternalException(this, "NOT FOUND OFF-BIT");
    }

    /**
      nビット以前でONであるビットを検索する。

      @param n ビット番号

      @throws UDF_InternalException 指定の番目が管理外。
    */
    public int searchBackBitOn(int n) throws UDF_InternalException {

        checkRange(n);

        for ( ; n >= 8; n--) {

            if ((n % 8) == 7) {
                break;
            }

            if (isSetNoCheck(n)) {
                return n;
            }
        }

        for ( ; n >= 8; n -= 8) {
            if (my_data[n / 8] != 0) {
                break;
            }
        }

        for ( ; n >= 0; n--) {

            if (isSetNoCheck(n)) {
                return n;
            }
        }

        throw new UDF_InternalException(this, "NOT FOUND ON-BIT");
    }

    /**
      指定の位置からOFF ビット領域を探す。

      @param n ビット番号
      @return OFFビット領域の先頭ビット番号

      - 指定番号のビットがOFF ビット領域に存在するときは、その領域の先頭を返す。<br>
      - 指定番号のビットのOFF ビット領域に存在しないときは、指定番号のビットより後方に存在するOFF ビット領域の先頭を返す。<br>
      - 指定番号のビットのOFF ビット領域に存在せず、指定番号のビットより後方にOFF ビット領域が存在しないときは最終ビット番号の次の数値を返す。<br>
    */
    public int searchStartBitOff(int n) {

        try {
            n = searchBitOff(n);
        }
        catch(UDF_InternalException e) {
            n = getBitSize();
        }
        try {
            n = searchBackBitOn(n - 1);
            return (n + 1);
        }
        catch(UDF_InternalException e) {
            return 0;
        }
    }

    /**
      指定の位置からONビット領域を探す。

      @param n ビット番号
      @return ONビット領域の先頭ビット番号

      - 指定番号のビットがONビット領域に存在するときは、その領域の先頭を返す。<br>
      - 指定番号のビットのONビット領域に存在しないときは、指定番号のビットより後方に存在するONビット領域の先頭を返す。<br>
      - 指定番号のビットのONビット領域に存在せず、指定番号のビットより後方にONビット領域が存在しないときは最終ビット番号の次の数値を返す。<br>
    */
    public int searchStartBitOn(int n) {

        try {
            n = searchBitOn(n);
        }
        catch(UDF_InternalException e) {
            n = getBitSize();
        }
        try {
            n = searchBackBitOff(n - 1);
            return (n + 1);
        }
        catch(UDF_InternalException e) {
            return 0;
        }
    }

    /**
       以下の条件に合う、ビット位置を探す。

       - nビット以降
       - 位置が alignで揃っている
       - その位置から num個連続 OFF

      @param n ビット番号
      @param align アライメント
      @param num 個数

      @throws UDF_InternalException 指定の番目が管理外。
    */
    public int searchBitOff(int n, int align, int num) throws UDF_InternalException {

        checkRange(n);

        while(n < numOfBits) {

            int end;

            //　指定の位置以降に存在する連続するOFF ビット領域を見つける　//
            n = UDF_Util.align(n, align);
            n = searchBitOff(n);
            try {
                end = searchBitOn(n);
            }
            catch(UDF_InternalException e) {
                end = numOfBits;
            }
            //　OFF ビット領域の先頭を指定の境界に合わせないといけないが、合わせたところが　//
            //　OFF ビット領域の範囲外だったらまったく意味が無い　//
            int len = end - n;
            int n2 = UDF_Util.align(n, align);
            if (n2 >= (n + len)) {
                n = n2;
                continue;
            }

            //　境界に合わせた後のOFF ビット領域の開始位置、サイズを計算　//
            len -= (n2 -n);
            n = n2;

            //　要求するサイズを満たしたら終了　//
            if (len >= num) {
                return n;
            }

            //　満たせなかったら処理継続　//
            n += len;
        }

        throw new UDF_InternalException(this, "NOT FOUND OFF-BIT");
    }


    /**
       以下の条件に合う、ビット位置を探す。

       - nビット以降
       - 位置が alignで揃っている
       - その位置から num個連続 ON

      @throws UDF_InternalException 指定の番目が管理外。
    */
    public int searchBitOn(int n, int align, int num) throws UDF_InternalException {

        checkRange(n);

        while(n < numOfBits) {

            int end;

            //　指定の位置以降に存在する連続するONビット領域を見つける　//
            n = UDF_Util.align(n, align);
            n = searchBitOn(n);
            try {
                end = searchBitOff(n);
            }
            catch(UDF_InternalException e) {
                end = numOfBits;
            }
            //　ONビット領域の先頭を指定の境界に合わせないといけないが、合わせたところが　//
            //　ONビット領域の範囲外だったらまったく意味が無い　//
            int len = end - n;
            int n2 = UDF_Util.align(n, align);
            if (n2 >= (n + len)) {
                n = n2;
                continue;
            }

            //　境界に合わせた後のONビット領域の開始位置、サイズを計算　//
            len -= (n2 -n);
            n = n2;

            //　要求するサイズを満たしたら終了　//
            if (len >= num) {
                return n;
            }

            //　満たせなかったら処理継続　//
            n += len;
        }

        throw new UDF_InternalException(this, "NOT FOUND ON-BIT");
    }

    /**
       以下の条件に合う、ビット位置を探す。

       - nビット以前
       - 位置が alignで揃っている
       - その位置から num個連続 OFF

      @param n ビット番号
      @param align アライメント
      @param num 個数

      @throws UDF_InternalException 指定の番目が管理外。
    */
    public int searchBackBitOff(int n, int align, int num) throws UDF_InternalException {

        checkRange(n);

        n -= n % align;

        while(n >= 0) {

            int start, end, len;

            //　OFF ビット領域の終端を探す　//
            end = searchBackBitOff(n); //OFF が一つも発見できなかったら Exception でよい。

            //　OFF ビット領域の先頭を探す　//
            try {
                start = 1 + searchBackBitOn(end);
            }
            catch(UDF_InternalException e) {
                start = 0;
            }
            end++;

            //　開始セクタの境界を合わせる　//
            int n2 = com.udfv.util.UDF_Util.align(start, align);

            //　要求するビット個数を確保できているか　//
            len = end - n2;
            if (len >= num) {
                n2 = end - num;
                n2 -= n2 % align;
                return n2;
            }

            n = start;
            n -= n % align;
            n -= align;
        }

        throw new UDF_InternalException(this, "NOT FOUND OFF-BIT");
    }

    /**
       以下の条件に合う、ビット位置を探す。

       - nビット以前
       - 位置が alignで揃っている
       - その位置から num個連続 ON

      @param n ビット番号
      @param align アライメント
      @param num 個数


      @throws UDF_InternalException 指定の番目が管理外。
    */
    public int searchBackBitOn(int n, int align, int num) throws UDF_InternalException {

        checkRange(n);

        n -= n % align;

        while(n >= 0) {

            int start, end, len;

            //　ONビット領域の終端を探す　//
            end = searchBackBitOn(n); //ONが一つも発見できなかったら Exception でよい。

            //　ONビット領域の先頭を探す　//
            try {
                start = searchBackBitOff(end) + 1;
            }
            catch(UDF_InternalException e) {
                start = 0;
            }
            end ++;

            //　開始セクタの境界を合わせる　//
            int n2 = com.udfv.util.UDF_Util.align(start, align);

            //　要求するビット個数を確保できているか　//
            len = end - n2;
            if (len >= num) {
                n2 = end - num;
                n2 -= n2 % align;
                return n2;
            }

            n = start;
            n -= n % align;
            n -= align;
        }

        throw new UDF_InternalException(this, "NOT FOUND ON-BIT");
    }

    /**
       以下の条件に合う、ビット位置を探す。

       - nビット以降
       - 位置が alignで揃っている
       - その位置から align個連続 OFF

      @param n ビット番号
      @param align アライメント

      @throws UDF_InternalException 指定の番目が管理外。
    */
    public int searchBitOff(int n, int align) throws UDF_InternalException {

        return searchBitOff(n, align, align);
    }

    /**
       以下の条件に合う、ビット位置を探す。

       - nビット以降
       - 位置が alignで揃っている
       - その位置から align個連続 OFF

      @param n ビット番号
      @param align アライメント

      @throws UDF_InternalException 指定の番目が管理外。
    */
    public int searchBitOn(int n, int align) throws UDF_InternalException {

        return searchBitOn(n, align, align);
    }

    /**
       以下の条件に合う、ビット位置を探す。

       - nビット以前
       - 位置が alignで揃っている
       - その位置から align個連続 OFF

      @param n ビット番号
      @param align アライメント

      @throws UDF_InternalException 指定の番目が管理外。
    */
    public int searchBackBitOff(int n, int align) throws UDF_InternalException {
        return searchBackBitOff(n, align, align);
    }

    /**
      nビット以前でONであるビットを検索する。

      @param n	ビット
      @param align	アライメント

      @throws UDF_InternalException 指定の番目が管理外。
    */
    public int searchBackBitOn(int n, int align) throws UDF_InternalException {
        return searchBackBitOn(n, align, align);
    }

    /**
       2つの bitmapを比較し、offビット目以降で最初に値が異なるbit位置を求める。

       2つの bitmapが等しい場合は -1を返す。

       2つの bitmapの長さが違う場合は、短いほうに合わせ比較する。
     */
    public int diff(UDF_bitmap b, int off) throws UDF_InternalException{
	long sz = getBitSize();
	if(b.getBitSize() < sz)
	    sz = b.getBitSize();

	while(off < sz && off % 8 != 0){
	    if(isSet(off) != b.isSet(off))
		return off;
	    ++off;
	}

	while(off < sz && my_data[off/8] == b.my_data[off/8])
	    off += 8;

	while(off < sz){
	    if(isSet(off) != b.isSet(off))
		return off;
	    ++off;
	}

	return -1;
    }

    /**
       2つの bitmapを比較し、offビット目以降で最初に値が等しい bit位置を求める。
       2つの bitmapが等しい場合は -1を返す。

       2つの bitmapの長さが違う場合は、短いほうに合わせ比較する。
     */
    public int same(UDF_bitmap b, int off) throws UDF_InternalException{
	long sz = getBitSize();
	if(b.getBitSize() < sz)
	    sz = b.getBitSize();

	while(off < sz && off % 8 != 0){
	    if(isSet(off) == b.isSet(off))
		return off;
	    ++off;
	}
	while(off < sz && (my_data[off/8] ^ b.my_data[off/8]) == 0xff)
	    off += 8;

	while(off < sz){
	    if(isSet(off) == b.isSet(off))
		return off;
	    ++off;
	}

	return -1;
    }

    /**
      バイト値を無符号の１バイト値としてint に置きなおす。

      @return 変換後の数値。
    */
    private int b2ui(byte b) {
        return ((b < 0) ? (256 + b): b);
    }

    /**
      ONであるビットの個数をカウントする。

      @return ONであるビットの個数。
    */
    public int countBitOn() {

        int sum = 0;

        for (int i = 0; i < getSize(); i++) {
            sum += count_table[b2ui(my_data[i])];
        }

        return sum;
    }

    /**
      OFFであるビットの個数をカウントする。

      @return OFFであるビットの個数。
    */
    public int countBitOff() {

        int sum = numOfBits;

        for (int i = 0; i < getSize(); i++) {
            sum -= count_table[b2ui(my_data[i])];
        }

        return sum;
    }
    /**
       nビット以上 bitがonの領域があるか？
       (nセクタ以上割り当てができるか？)
     */
    public int isAlloc(int n){
        int sum = 0;

        for (int i = 0; i < getSize(); i++) {
            sum += count_table[b2ui(my_data[i])];
	    if(sum > n)
		return n;
        }

        return 0;
    }

    /**
      ADで指定される領域に適合するビットをONにする。

      <p>
      パーティション情報は無視される。
      </p>
      @deprecated replaced by {@link setFromADS(UDF_ElementList}
    */
    public void setFromADs(UDF_AD [] ads) throws UDF_InternalException {

        for (int i = 0, max = ads.length; i < max; i++) {
            set(ads[i].getLbn(), (int)((ads[i].getLen() + env.LBS - 1) / env.LBS));
        }
    }

    /**
      ADで指定される領域に適合するビットをOFFにする。

      <p>
      パーティション情報は無視される。
      </p>

      @deprecated replaced by {@link unsetFromADS(UDF_ElementList}
    */
    public void unsetFromADs(UDF_AD [] ads) throws UDF_InternalException {

        for (int i = 0, max = ads.length; i < max; i++) {
            unset(ads[i].getLbn(), (int)((ads[i].getLen() + env.LBS - 1) / env.LBS));
        }
    }

    /**
      ADで指定される領域に適合するビットをOFFにする。

      <p>
      パーティション情報は無視される。
      </p>
    */
    public void unsetFromADs(UDF_ADList ads) throws UDF_InternalException {
        for (int i = 0, max = ads.size(); i < max; i++) {
	    UDF_AD ad = (UDF_AD)ads.elementAt(i);
            unset(ad.getLbn(), (int)((ad.getLen() + env.LBS - 1) / env.LBS));
        }
    }

    /**
      ADで指定される領域に適合するビットをOFFにする。

      <p>
      パーティション情報は無視される。
      </p>
    */
    public void setFromADs(UDF_ADList ads) throws UDF_InternalException {
        for (int i = 0, max = ads.size(); i < max; i++) {
	    UDF_AD ad = (UDF_AD)ads.elementAt(i);
            set(ad.getLbn(), (int)((ad.getLen() + env.LBS - 1) / env.LBS));
        }
    }

    /**
      ビットマップの情報を０と１の文字で構成した文字列を返す。

      @param indent インデント用文字列
      @param lbn 開始ビット番号
      @param len ビット数
      @throws UDF_InternalException ビットの指定が範囲外。
    */
    public String toBitmapString(String indent, int lbn, int len) throws UDF_InternalException {
        return toBitmapString(indent, lbn, len, 8);
    }

    /**
      ビットマップの情報を０と１の文字で構成した文字列を返す。

      @param indent インデント用文字列
      @param lbn 開始ビット番号
      @param len ビット数
      @param align 一行に表示するビット数（行を構成するビットの境界値としても使用される）
      @throws UDF_InternalException ビットの指定が範囲外。
    */
    public String toBitmapString(String indent, int lbn, int len, int align) throws UDF_InternalException {

        checkRange(lbn);
        checkRange(lbn + len - 1);

        String nl = com.udfv.util.UDF_Util.getSystemNewLine();
        String bitstr;
        String pad = "                ";
        String tmp;
        int count, max, last;

        last = lbn + len;

        tmp = pad + (lbn - (lbn % align));
        tmp = tmp.substring(tmp.length() - 12);
        bitstr = indent + "*" + tmp + ":";

        count = 0;
        max = lbn % align;
        for (count = 0; count < max; count++) {
            bitstr += " ";
        }
        max = lbn + len;
        max = max < align ? max: align;
        for (; count < max; count++) {
            bitstr += isSetNoCheck(lbn++) ? "1": "0";
        }
        bitstr += nl;

        loop: while(lbn < last) {

            tmp = pad + (lbn - (lbn % align));
            tmp = tmp.substring(tmp.length() - 12);
            bitstr += indent + "*" + tmp + ":";

            for (count = 0; count < align; count++) {

                if (lbn >= last) {
                    bitstr += nl;
                    break loop;
                }

                bitstr += isSetNoCheck(lbn++) ? "1": "0";

            }
            bitstr += nl;
        }

        return bitstr;
    }

    public void debug(int indent) {
        super.debug(indent);
/*
        try {
            com.udfv.ecma167.UDF_bitmap bm = new com.udfv.ecma167.UDF_bitmap(image, null, "UDF_bitmap", 16);
            System.err.println("getSize() = " + bm.getSize() + ", getBitSize() = " + bm.getBitSize());

            bm.set(15);

            bm.set(0);
            System.err.println("set(0)   : " + bm.getData()[0] + ", countBitOn(
) = " + bm.countBitOn() + ", countBitOff() = " + bm.countBitOff() + ", bm.searchBitOn(1) = " + bm.searchBitOn(1) + ", searchBitOff(1) = " + bm.searchBitOff(1));
            bm.unset(0);
            System.err.println("unset(0) : " + bm.getData()[0] + ", countBitOn() = " + bm.countBitOn() + ", countBitOff() = " + bm.countBitOff() + ", bm.searchBitOn(1) = " + bm.searchBitOn(1) + ", searchBitOff(1) = " + bm.searchBitOff(1));

            bm.set(1);
            System.err.println("set(1)   : " + bm.getData()[0] + ", countBitOn() = " + bm.countBitOn() + ", countBitOff() = " + bm.countBitOff() + ", bm.searchBitOn(1) = " + bm.searchBitOn(1) + ", searchBitOff(1) = " + bm.searchBitOff(1));
            bm.unset(1);
            System.err.println("unset(1) : " + bm.getData()[0] + ", countBitOn() = " + bm.countBitOn() + ", countBitOff() = " + bm.countBitOff() + ", bm.searchBitOn(1) = " + bm.searchBitOn(1) + ", searchBitOff(1) = " + bm.searchBitOff(1));

            bm.set(7);
            System.err.println("set(7)   : " + bm.getData()[0] + ", countBitOn() = " + bm.countBitOn() + ", countBitOff() = " + bm.countBitOff() + ", bm.searchBitOn(1) = " + bm.searchBitOn(1) + ", searchBitOff(1) = " + bm.searchBitOff(1));
            bm.unset(7);
            System.err.println("unset(7) : " + bm.getData()[0] + ", countBitOn() = " + bm.countBitOn() + ", countBitOff() = " + bm.countBitOff() + ", bm.searchBitOn(1) = " + bm.searchBitOn(1) + ", searchBitOff(1) = " + bm.searchBitOff(1));

            bm.set(4);
            System.err.println("set(0)   : " + bm.getData()[0] + ", countBitOn() = " + bm.countBitOn() + ", countBitOff() = " + bm.countBitOff() + ", bm.searchBitOn(1) = " + bm.searchBitOn(1) + ", searchBitOff(1) = " + bm.searchBitOff(1));
            bm.set(5);
            System.err.println("set(1)   : " + bm.getData()[0] + ", countBitOn() = " + bm.countBitOn() + ", countBitOff() = " + bm.countBitOff() + ", bm.searchBitOn(1) = " + bm.searchBitOn(1) + ", searchBitOff(1) = " + bm.searchBitOff(1));
            bm.unset(4);
            System.err.println("unset(0) : " + bm.getData()[0] + ", countBitOn() = " + bm.countBitOn() + ", countBitOff() = " + bm.countBitOff() + ", bm.searchBitOn(1) = " + bm.searchBitOn(1) + ", searchBitOff(1) = " + bm.searchBitOff(1));
            bm.unset(5);
            System.err.println("unset(1) : " + bm.getData()[0] + ", countBitOn() = " + bm.countBitOn() + ", countBitOff() = " + bm.countBitOff() + ", bm.searchBitOn(1) = " + bm.searchBitOn(1) + ", searchBitOff(1) = " + bm.searchBitOff(1));

            int size = 32;
            System.err.print("setSize(" + size + ") : ");
            bm.setSize(size);
            System.err.println("getSize() = " + bm.getSize() + ", getBitSize() = " + bm.getBitSize());

            size *= 8;
            size += 4;
            System.err.print("setBitSize(" + size + ") : ");
            bm.setBitSize(size);
            System.err.println("getSize() = " + bm.getSize() + ", getBitSize() = " + bm.getBitSize());

            bm.set(8);
            System.err.println("set(8)   : " + bm.getData()[1]);
            bm.unset(8);
            System.err.println("unset(8) : " + bm.getData()[1]);

            bm.set(17);
            System.err.println("set(17)   : " + bm.getData()[2]);
            bm.unset(17);
            System.err.println("unset(17) : " + bm.getData()[2]);

            bm.set(8);
            System.err.println("set(8)   : " + bm.getData()[1] + ", countBitOn() = " + bm.countBitOn() + ", countBitOff() = " + bm.countBitOff() + ", bm.searchBitOn(1) = " + bm.searchBitOn(1) + ", searchBitOff(1) = " + bm.searchBitOff(1));
            bm.set(11);
            System.err.println("set(11)  : " + bm.getData()[1] + ", countBitOn() = " + bm.countBitOn() + ", countBitOff() = " + bm.countBitOff() + ", bm.searchBitOn(1) = " + bm.searchBitOn(1) + ", searchBitOff(1) = " + bm.searchBitOff(1));
            bm.set(17);
            System.err.println("set(17)  : " + bm.getData()[2] + ", countBitOn() = " + bm.countBitOn() + ", countBitOff() = " + bm.countBitOff() + ", bm.searchBitOn(1) = " + bm.searchBitOn(1) + ", searchBitOff(1) = " + bm.searchBitOff(1));
            
            bm.unset(8);
            System.err.println("unset(8) : " + bm.getData()[1] + ", countBitOn() = " + bm.countBitOn() + ", countBitOff() = " + bm.countBitOff() + ", bm.searchBitOn(1) = " + bm.searchBitOn(1) + ", searchBitOff(1) = " + bm.searchBitOff(1));
            bm.unset(11);
            System.err.println("unset(11): " + bm.getData()[1] + ", countBitOn() = " + bm.countBitOn() + ", countBitOff() = " + bm.countBitOff() + ", bm.searchBitOn(1) = " + bm.searchBitOn(1) + ", searchBitOff(1) = " + bm.searchBitOff(1));
            bm.unset(17);
            System.err.println("unset(17): " + bm.getData()[2] + ", countBitOn() = " + bm.countBitOn() + ", countBitOff() = " + bm.countBitOff() + ", bm.searchBitOn(1) = " + bm.searchBitOn(1) + ", searchBitOff(1) = " + bm.searchBitOff(1));
        }
        catch(com.udfv.exception.UDF_InternalException e) {
            System.err.println(e.toString());
        }
*/
    }

}


