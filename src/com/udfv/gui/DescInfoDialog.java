package com.udfv.gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

import com.udfv.access.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;

/**
	デスクリプタ詳細情報ダイアログ
*/
class DescInfoDialog extends JDialog implements ActionListener {
	private Container pane;

	private JComponent desc_text;
	private JScrollPane desc_pane;

	private JPanel  btn_panel;
	private JButton close_button;

	private final String str_btn_close = new String("Close");

	private UDF_Element element;


	/**
		コンストラクタ
	*/
	public DescInfoDialog(Frame frame, String str, boolean flag, UDF_Element e) {
		super(frame, str, flag);

		element = e;

		if (element != null) {
			desc_text = element.getJInfo();
		}
		else {
			desc_text = new JTextArea();
		}
		//desc_text.setEditable(false);
//		System.out.println(element);
		//System.out.println("line = " + desc_text.getColumns());
		desc_pane = new JScrollPane(desc_text);
		desc_pane.getVerticalScrollBar().setUnitIncrement(40);


		pane = getContentPane();

		// ボタン
		close_button = new JButton(str_btn_close);
		close_button.addActionListener(this);
		btn_panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btn_panel.add(close_button);

		pane.add(desc_pane, BorderLayout.CENTER);
		pane.add(btn_panel, BorderLayout.SOUTH);
	}

	/**
		アクションリスナ
	*/
	public void actionPerformed(ActionEvent ev) {
		String cmd = ev.getActionCommand();
//		System.out.println(cmd);

		if (cmd.equals(str_btn_close)) {
			dispose();
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




