package com.udfv.gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;

import com.udfv.access.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;


/*
リソース構成

Container pane
 +- JPanel center_panel
 |   +- JScrollPane dir_pane
 |   |   +- JComponent dir_text
 |   +- JScrollPane volinfo_pane
 |       +- JPanel volinfo_panel
 +- JPanel btn_panel
     +- JButton save_button
     +- JButton close_button
*/



/**
	ディレクトリ詳細情報ダイアログ
*/
class DirInfoDialog extends JDialog implements ActionListener {
	private Container pane;

//	private DirTreeNode dir_root;
//	private DefaultTreeModel dir_model;
//	private JTree dir_tree;
	private JPanel center_panel;
	private JScrollPane dir_pane;
	private JComponent dir_text;

	private JScrollPane volinfo_pane;
	private JPanel volinfo_panel;
	private JBitmapColorSet volinfo_color;

	private JPanel btn_panel;
	private JButton save_button;
	private JButton close_button;

	private final String str_btn_close = new String("Close");
	private final String str_btn_save = new String("Save");

	private UDF_desc257 element;
	private MainFrame opener;

	/**
		コンストラクタ
	*/
	public DirInfoDialog(Frame frame, String str, boolean flag, UDF_desc257 e) {
		super(frame, str, flag);
		opener = (MainFrame)frame;

		// UDF_Element
		element = (UDF_desc257)e;

		pane = getContentPane();


		// テキストエリア
		if (element != null) {
			dir_text = element.getJInfo2();
		}
		else {
			dir_text = new JTextArea();
		}
		//dir_text.setEditable(false);
		dir_pane = new JScrollPane(dir_text);
		dir_pane.getVerticalScrollBar().setUnitIncrement(40);

		// ボリューム領域情報
		volinfo_color = new JBitmapColorSet();
		volinfo_color.setColor(JBitmapColorSet.FREE, Color.LIGHT_GRAY);
		volinfo_color.setColor(JBitmapColorSet.VOL, Color.GRAY);
		volinfo_color.setColor(JBitmapColorSet.PART_0, Color.GRAY);
		volinfo_color.setColor(JBitmapColorSet.PART_1, Color.GRAY);
		volinfo_color.setColor(JBitmapColorSet.PART_2, Color.GRAY);
		volinfo_color.setColor(JBitmapColorSet.PART_3, Color.GRAY);
		volinfo_color.setColor(JBitmapColorSet.USER_0, Color.RED);

		ClassLoader cl = getClass().getClassLoader();
		volinfo_panel = (JPanel)element.getReferenceTo().createJBitmapPanel(1096, 100, volinfo_color);
		volinfo_pane = new JScrollPane();
		volinfo_pane.setViewportView(volinfo_panel);
		volinfo_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		center_panel = new JPanel();
		center_panel.setLayout(new BorderLayout());
		center_panel.add(dir_pane, BorderLayout.CENTER);
		center_panel.add(volinfo_pane, BorderLayout.SOUTH);

		// ボタン
		save_button = new JButton(str_btn_save);
		save_button.addActionListener(this);
		close_button = new JButton(str_btn_close);
		close_button.addActionListener(this);
		btn_panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btn_panel.add(save_button);
		btn_panel.add(close_button);

		pane.add(center_panel, BorderLayout.CENTER);
		pane.add(btn_panel, BorderLayout.SOUTH);
	}

	/**
		アクションリスナ
		@param ev イベント
	*/
	public void actionPerformed(ActionEvent ev) {
		String cmd = ev.getActionCommand();
//		System.out.println(cmd);

		if (cmd.equals(str_btn_close)) {
			dispose();
		}
		if (cmd.equals(str_btn_save)) {
			try{
				//:以降の文字を取得
				String fid = UDF_Util.cdr(element.getFileId().getStringData(), ':');
				JFileChooser chooser = new JFileChooser();
				chooser.setSelectedFile(new File(fid));
				chooser.showSaveDialog(this);
				File file = chooser.getSelectedFile();
				if(file != null){
					UDF_RandomAccessFile f = new UDF_RandomAccessFile(file, "rw");
					UDF_Element ref = element.getReferenceTo().getReferenceData();

					UDF_ElementList children = ref.getChildList();
					for(int i=0 ; i<children.size() ; ++i){
						children.elementAt(i).writeTo(f);
					}
					f.close();
				}
			}
			catch(Exception e){
				System.out.println(e);
				e.printStackTrace();

				String msg = e.getMessage();
				if (msg == null) {
					msg = new String("ERROR");
				}
				JOptionPane.showMessageDialog(this, msg, "Error!",
				                        JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	/**
		ダイアログを表示する。ノードが設定されていない場合は無視する。
	*/
	public void show() {
		if (element == null) {
			return;
		}

		super.show();
	}

}




