package com.udfv.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;


/**
	オプション設定のためのダイアログボックス
*/
class OptionDialog extends JDialog implements ActionListener {
/*
リソースツリー
+- Container pane
    +- JTabbedPane tab_pane
    |   +- JPanel pnl_file
    |   |   +- JLabel lbl_file_dir
    |   |   +- TextField txtf_file_dir
    |   |   +- JButton btn_file_dirref
    |   +- JPanel pnl_ip
    |   |   +- JLabel lbl_ip
    |   |   +- TextField txtf_ip
    |   |   +- JLabel lbl_drv
    |   |   +- JComboBox cmb_drv
    |   +- JPanel pnl_image
    |       +- JPanel media_type
    |       +- JPanel media_mode
    +- JPanel pnl_south
        +- JButton btn_ok
         +- JButton btn_cancel
*/
	// リソース
	// テキスト
	private static final String STR_OK = "OK";
	private static final String STR_CANCEL = "Cancel";

	private static final String STR_CURRENT_DIR = "Current Directory :";
	private static final String STR_REF = "...";

	private static final String STR_IP = "IP Address :";
	private static final String STR_DRV = "Drive :";
	private static final String drive_letter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private Container pane;
	private JTabbedPane tab_pane;
	private JPanel pnl_south;

	private JPanel pnl_file;
	private JPanel pnl_ip;
	private JPanel pnl_image;

	//
	private final JLabel lbl_file_dir = new JLabel(STR_CURRENT_DIR);
	private JTextField txtf_file_dir;
	private final JButton btn_file_dirref = new JButton(STR_REF);

	//
	private final JLabel lbl_ip = new JLabel(STR_IP);
	private JTextField txtf_ip;
	private final JLabel lbl_drv = new JLabel(STR_DRV);
	private JComboBox cmb_drv;

	//
	private JPanel pnl_media_type;
	private JPanel pnl_media_mode;

	//
	private JRadioButton radio_dvd_rom;
	private JRadioButton radio_dvd_ram;
	private JRadioButton radio_dvd_rw;
	private JRadioButton radio_dvd_r;
	private JRadioButton radio_dvd_plus_r;
	private JRadioButton radio_dvd_plus_rw;
	private JRadioButton radio_bd_rom;
	private JRadioButton radio_bd_r_re;

	private JRadioButton radio_pseude_over_writable;
	private JRadioButton radio_read_only;
	private JRadioButton radio_write_once;
	private JRadioButton radio_rewritable;
	private JRadioButton radio_overwritable;

	private final JButton btn_ok = new JButton(STR_OK);
	private final JButton btn_cancel = new JButton(STR_CANCEL);

	// ドキュメント
	private static String current_dir;
	private static String ip;
	private static String drv;

	/**
		コンストラクタ
		@param owner 親ダイアログ
	*/
	public OptionDialog(Frame owner) {
		super(owner, "Option", true);

		init_resources();
	}

	private static final int FILE_INDEX = 0;
	private static final int IP_INDEX = 1;
	private static final int IMAGE_INDEX = 2;

	/**
		ダイアログボックスのコンポーネントを設定する
	*/
	private void init_resources() {
		init_file_pane();

		pnl_ip = new JPanel();
		init_ip_pane();

		init_image_pane();

		tab_pane = new JTabbedPane(SwingConstants.TOP);
		tab_pane.add(pnl_file, FILE_INDEX);
		tab_pane.add(pnl_ip, IP_INDEX);
		tab_pane.add(pnl_image, IMAGE_INDEX);
		tab_pane.setTitleAt(FILE_INDEX, "File");
		tab_pane.setTitleAt(IP_INDEX, "IP");
		tab_pane.setTitleAt(IMAGE_INDEX, "Image");

		pnl_south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pnl_south.add(btn_ok);
		pnl_south.add(btn_cancel);

		pane = getContentPane();
		pane.add(tab_pane, BorderLayout.CENTER);
		pane.add(pnl_south, BorderLayout.SOUTH);

		// リスナ登録
		btn_ok.addActionListener(this);
		btn_cancel.addActionListener(this);
		btn_file_dirref.addActionListener(this);

	}

	/**
		ファイル設定タブの中身を設定
	*/
	private void init_file_pane() {
		txtf_file_dir = new JTextField(current_dir);

		pnl_file = new JPanel();
		pnl_file.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		lbl_file_dir.setLabelFor(txtf_file_dir);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(1, 10, 1, 5);
		pnl_file.add(lbl_file_dir, c);

		c.gridx = 1;
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 5, 1, 5);
		pnl_file.add(txtf_file_dir, c);

		c.gridx = 2;
		c.gridwidth = 1;
		c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(1, 5, 1, 10);
		pnl_file.add(btn_file_dirref, c);
	}

	/**
	   ATAPI タブの設定
	*/
	private void init_ip_pane() {
		txtf_ip = new JTextField(ip);

		cmb_drv = new JComboBox();
		int i;
		for (i = 0; i < drive_letter.length(); i++) {
			cmb_drv.addItem(drive_letter.substring(i, i+1));
		}
		cmb_drv.setSelectedItem(drv);


		pnl_ip = new JPanel();
		pnl_ip.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;

		lbl_ip.setLabelFor(txtf_ip);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(1, 10, 1, 5);
		pnl_ip.add(lbl_ip, c);

		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(1, 5, 1, 10);
		pnl_ip.add(txtf_ip, c);

		lbl_drv.setLabelFor(cmb_drv);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0.0;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(1, 10, 1, 5);
		pnl_ip.add(lbl_drv, c);

		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(1, 5, 1, 10);
		pnl_ip.add(cmb_drv, c);
	}

	/**
		Image プロパティタブの設定
	*/
	private void init_image_pane() {
		pnl_image = new JPanel();
		//LayoutManager limage = new BoxLayout(pnl_image, BoxLayout.X_AXIS);
		LayoutManager limage = new GridLayout(1, 2);

		pnl_media_type = new JPanel();
		LayoutManager lt = new BoxLayout(pnl_media_type, BoxLayout.Y_AXIS);

		Border bt = new TitledBorder(null, "Media Type",
			TitledBorder.LEFT,
			TitledBorder.TOP);

		pnl_media_type.setBorder(bt);

		radio_dvd_rom = new JRadioButton("DVD-ROM");
		radio_dvd_ram = new JRadioButton("DVD-RAM");
		radio_dvd_rw = new JRadioButton("DVD-RW");
		radio_dvd_r = new JRadioButton("DVD-R");
		radio_dvd_plus_r = new JRadioButton("DVD+R");
		radio_dvd_plus_rw = new JRadioButton("DVD+RW");
		radio_bd_rom = new JRadioButton("BD-ROM");
		radio_bd_r_re = new JRadioButton("BD-RE");

		pnl_media_type.setLayout(lt);
		pnl_media_type.add(radio_dvd_rom);
		pnl_media_type.add(radio_dvd_ram);
		pnl_media_type.add(radio_dvd_rw);
		pnl_media_type.add(radio_dvd_r);
		pnl_media_type.add(radio_dvd_plus_r);
		pnl_media_type.add(radio_dvd_plus_rw);
		pnl_media_type.add(radio_bd_rom);
		pnl_media_type.add(radio_bd_r_re);

		ButtonGroup gp1 = new ButtonGroup();
		gp1.add(radio_dvd_rom);
		gp1.add(radio_dvd_ram);
		gp1.add(radio_dvd_rw);
		gp1.add(radio_dvd_r);
		gp1.add(radio_dvd_plus_r);
		gp1.add(radio_dvd_plus_rw);
		gp1.add(radio_bd_rom);
		gp1.add(radio_bd_r_re);

		pnl_media_mode = new JPanel();
		LayoutManager lm = new BoxLayout(pnl_media_mode, BoxLayout.Y_AXIS);

		Border bm = new TitledBorder(null, "Media Mode",
			TitledBorder.LEFT,
			TitledBorder.TOP);

		pnl_media_mode.setBorder(bm);

		radio_pseude_over_writable = new JRadioButton("Rseude Over Writable");
		radio_read_only = new JRadioButton("Read Only");
		radio_write_once = new JRadioButton("Write Once");
		radio_rewritable = new JRadioButton("Rewritable");
		radio_overwritable = new JRadioButton("Overwritable");

		pnl_media_mode.setLayout(lm);
		pnl_media_mode.add(radio_pseude_over_writable);
		pnl_media_mode.add(radio_read_only);
		pnl_media_mode.add(radio_write_once);
		pnl_media_mode.add(radio_rewritable);
		pnl_media_mode.add(radio_overwritable);

		ButtonGroup gp2 = new ButtonGroup();
		gp2.add(radio_pseude_over_writable);
		gp2.add(radio_read_only);
		gp2.add(radio_write_once);
		gp2.add(radio_rewritable);
		gp2.add(radio_overwritable);

		pnl_image.setLayout(limage);
		pnl_image.add(pnl_media_type);
		pnl_image.add(pnl_media_mode);
	}


	/**
		カレントディレクトリの文字列を取得
	*/
	public static String get_current_dir() {
		return current_dir;
	}

	/**
		カレントディレクトリ文字列を設定
	*/
	public static void set_current_dir(String str) {
		current_dir = str;
	}

	/**
		IP アドレス文字列を取得
	*/
	public static String get_ip() {
		return ip;
	}

	/**
		IP アドレス文字列を設定
	*/
	public static void set_ip(String str) {
		ip = str;
	}

	/**
		ドライブ文字列を取得
	*/
	public static String get_drv() {
		return drv;
	}


	/**
		ドライブ文字列を設定
	*/
	public static void set_drv(String str) {
		drv = str;
	}

	/**
		アクションイベントのリスナ
		@param ev イベント
	*/
	public void actionPerformed(ActionEvent ev) {
		String cmd = ev.getActionCommand();
//		System.out.println(cmd);

		if (cmd.equals(STR_OK)) {
			// File タブ
			String tmp;
			try {
				tmp = txtf_file_dir.getText();
				current_dir = tmp;
			}
			catch (NullPointerException e) {
				e.printStackTrace();
			}

			// IP タブ
			try {
				tmp = txtf_ip.getText();
				ip = tmp;
			}
			catch (NullPointerException e) {
				e.printStackTrace();
			}

			drv = (String)cmb_drv.getSelectedItem();
			dispose();
		}

		else if (cmd.equals(STR_CANCEL)) {
			dispose();
		}

		else if (cmd.equals(STR_REF)) {
//			System.out.println("hoge");
			reference_dir();
//			System.out.println("moge");

		}
	}

	private void reference_dir() {
		JFileChooser dialog = new JFileChooser();
		dialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		dialog.setCurrentDirectory(new File(current_dir));
		dialog.setApproveButtonText("Select");
//		System.out.println("++");
		int state = dialog.showDialog(this, "Select");
		if (JFileChooser.APPROVE_OPTION == state) {
			File d = dialog.getSelectedFile();
//			System.out.println("d = " + d.getAbsolutePath());
			txtf_file_dir.setText(d.getAbsolutePath());
		}

	}




}





