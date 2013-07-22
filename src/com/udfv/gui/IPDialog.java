package com.udfv.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;


/**
	ATAPIServer に接続するための IP アドレスを入力される
	ダイアログボックスクラス
*/
class IPDialog extends JDialog implements ActionListener {
/*
リソースツリー
+- Continer pane
    +- JPanel pnl_center
    |   +- JLabel lbl_ip
    |   +- JTextField txtf_ip
    |   +- JLabel lbl_drv
    |   +- JComboBox cmb_drv
    +- JPanel pnl_south
        +- JButton btn_ok
        +- JButton btn_cancel
*/
	private static final String str_ok = "OK";
	private static final String str_cancel = "Cancel";
	private static final String drive_letter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private Container pane;
	private JPanel pnl_center;
	private JPanel pnl_south;
	private JLabel lbl_ip;
	private JTextField txtf_ip;
	private JLabel lbl_drv;
	private JComboBox cmb_drv;
	private JButton btn_ok;
	private JButton btn_cancel;

	private static String ip_str = null;
	private static String drv_str = null;

	public static final int OK = 1;
	public static final int CANCEL = 0;
	private int result;


	public IPDialog(Frame owner, String title, boolean modal) {
		super(owner, title, modal);

		//
		if (ip_str == null) {
			try {
				ip_str= InetAddress.getLocalHost().getHostAddress();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (drv_str == null) {
		}


		//
		lbl_ip = new JLabel("IP Address :");
		txtf_ip = new JTextField(ip_str, 14);

		lbl_drv = new JLabel("Drive :");
		cmb_drv = new JComboBox();
		int i;
		for (i = 0; i < drive_letter.length(); i++) {
			cmb_drv.addItem(drive_letter.substring(i, i+1));
		}
		cmb_drv.setSelectedItem(drv_str);
		//cmb_drv.setBorder(new MatteBorder(5, 5, 5, 5, Color.BLUE));

		//
		pnl_center = new JPanel();
		pnl_center.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(1, 10, 1, 5);
		pnl_center.add(lbl_ip, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(1, 5, 1, 10);
		pnl_center.add(txtf_ip, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(1, 10, 1, 5);
		pnl_center.add(lbl_drv, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.insets = new Insets(1, 5, 1, 10);
		pnl_center.add(cmb_drv, gbc);

		//
		btn_ok = new JButton(str_ok);
		btn_cancel = new JButton(str_cancel);

		//
		pnl_south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pnl_south.add(btn_ok);
		pnl_south.add(btn_cancel);

		//
		pane = getContentPane();
		pane.setLayout(new BorderLayout());
		pane.add(pnl_center, BorderLayout.CENTER);
		pane.add(pnl_south, BorderLayout.SOUTH);

		// リスナ登録
		btn_ok.addActionListener(this);
		btn_cancel.addActionListener(this);

		setBounds(0, 0, 300, 150);
		setResizable(false);
	}

	/**
	*/
	public void show() {
		result = CANCEL;
		super.show();
	}

	/**
		IP アドレス文字列を取得
		@return IPアドレス
	*/
	public static String get_ip() {
		return ip_str;
	}

	/**
		IP アドレスを設定する
		@param str IPアドレス文字列
	*/
	public static void set_ip(String str) {
		ip_str = str;
	}

	/**
		ドライブレターを取得
		@return ドライブレター
	*/
	public static String get_drv() {
		return drv_str;
	}

	/**
		ドライブレターの設定
		@param str ドライブレター
	*/
	public static void set_drv(String str) {
		drv_str = str.substring(0, 1);
	}

	public int get_result() {
		return result;
	}


	/**
		アクションリスナ
	*/
	public void actionPerformed(ActionEvent ev) {
		String cmd = ev.getActionCommand();
//		System.out.println(cmd);

		if (cmd.equals(str_ok)) {
			ip_str = txtf_ip.getText();
			int idx = cmb_drv.getSelectedIndex();
			drv_str = drive_letter.substring(idx, idx+1);
//			System.out.println("IP end");
//			System.out.println("IP : " + ip_str);
//			System.out.println("Drv : " + drv_str);
//			System.out.println("----");
			result = OK;
			dispose();
		}

		if (cmd.equals(str_cancel)) {
			result = CANCEL;
			dispose();
		}
	}
}

