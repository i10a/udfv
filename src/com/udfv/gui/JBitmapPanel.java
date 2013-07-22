/*

*/
package com.udfv.gui;

/**
  ビットマップ情報が描画されたJPanelのための抽象クラス。
*/
abstract public class JBitmapPanel extends javax.swing.JPanel {

    //　ダブルバッファ　//
    protected java.awt.Image [] db;
    protected int idb;

    //　paint() 処理を始めていいかフラグ　//
    boolean init = false;

    //　ウィンドウの幅、高さ　//
    protected int w, h;
    //　ビットマップ画像のJPanel上の開始位置、幅　//
    protected int offset, wmax;
    //　ビット総数　//
    protected int total;

    //　最小、最大、適切サイズをコンポーネントに反映させる　//
    protected void packSize( ) {
        java.awt.Dimension dm = new java.awt.Dimension(w, h);
        setSize(w, h);
        setMaximumSize(dm);
        setMinimumSize(dm);
        setPreferredSize(dm);
    }

    //　　//
    protected void updateDoubleBuffer() {

        if (db[idb] != null) {
            return;
        }
        db[idb] = createImage(w, h);
        drawSpaceBitmap();
    }

    //　指定の領域情報に合わせて矩形をビットマップの使用状況を示す描画する　//
    public void drawSpaceBit(java.awt.Graphics gr, int lsn, int sec_size) {

        int x1 = (int)((double)wmax * (double)lsn / (double)total) + offset;
        int w1 = (int)((double)wmax * (double)sec_size / (double)total);

        if (w1 < 2) {
            gr.drawLine(x1, 0, x1, h - 1);
        }
        else {
            gr.fillRect(x1, 0, w1, h);
        }

        return;
    }

    /** JPanel（自身）にビット情報を描画する */
    abstract public void drawSpaceBitmap( );

    //　描画　//
    public synchronized void paint(java.awt.Graphics gr) {

        super.paint(gr);
        if ( !init ) {
            return;
        }
        updateDoubleBuffer();
        gr.drawImage(db[idb], 0, 0, this);
        idb = idb == 1 ? 0: 1;
    }

    //　画像更新　//
    public void update(java.awt.Graphics gr) {

        this.paint(gr);
    }
}


