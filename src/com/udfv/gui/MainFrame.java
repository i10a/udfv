package com.udfv.gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.prefs.Preferences;

import com.udfv.access.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;

import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import org.apache.xml.serialize.*;


/**
	GUI ツール メインフレーム
*/
public class MainFrame extends JFrame
	implements SwingConstants, ActionListener, MouseListener
{
// リソース構造
//	Continer content_pane
//	 +- JMenuBar menubar
//	 |   +- JMenu menu_file
//	 |   |   +- JMenuItem mi_file_read_file
//	 |   |   +- JMenuItem mi_*
//	 |   +- JMenu menu_*
//	 +- JToolBar toolbar
//	 |   +- JButton btn_read_file
//	 |   +- JButton btn_*
//	 +- JTabbedPane tab_pane
//	 |   +- JScrollPane dir_pane
//	 |   |   +- JTree dir_tree
//	 |   |       +- DefaultTreeModel dir_model
//	 |   |           +- DirTreeNode dir_root
//	 |   +- JScrollPane desc_pane
//	 |   |   +- JTree desc_tree
//	 |   |       +- DefaultTreeModel desc_model
//	 |   |           +- DescTreeNode desc_root (extends DefaultMutableTreeNode)
//	 |   +- JScrollPane verify_pane
//	 |       +- JTextArea verify_text
//	 +- JScrollPane volinfo_pane
//	     +- JPanel volinfo_panel


	//リソース
	/// アイコン
	private Icon icon_read_file;
	private Icon icon_read_xml;
	private Icon icon_read_disc;
	private Icon icon_write_file;
	private Icon icon_write_xml;
	private Icon icon_write_disc;
	private Icon icon_verify;

	/// テキスト
	//// Title
	private final static String STR_TITLE = "UDFV GUI";

	//// File
	private final static String STR_FILE_READ_FILE  = "Read from File ...";
	private final static String STR_FILE_READ_XML   = "Read from XML ...";
	private final static String STR_FILE_READ_DISC  = "Read from Disc ...";
	private final static String STR_FILE_WRITE_FILE = "Write File ...";
	private final static String STR_FILE_WRITE_XML  = "Write XML ...";
	private final static String STR_FILE_WRITE_DISC = "Write Disc ...";
	private final static String STR_FILE_EXIT       = "Exit";

	//// View
	private final static String STR_VIEW_DIR  = "Directory View";
	private final static String STR_VIEW_DESC = "Descriptor View";
	private final static String STR_VIEW_VERIFY = "Verify View";

	//// Tool
	private final static String STR_TOOL_VERIFY = "Verify";
	private final static String STR_TOOL_OPTION = "Option ...";

	//// Help
//	private final static String str_help_help  = "UDFV Help";
	private final static String STR_HELP_ABOUT = "About " + STR_TITLE;

	/// ツールチップ
	//// ボタン
	private final static String STR_TIP_READ_FILE = "Read Image File";
	private final static String STR_TIP_READ_XML  = "Read XML File";
	private final static String STR_TIP_READ_DISC = "Read from Disc";
	private final static String STR_TIP_WRIT_FILE = "Write Image File";
	private final static String STR_TIP_WRITE_XML  = "Write XML File";
	private final static String STR_TIP_WRITE_DISC = "Write to Disc";
	private final static String STR_TIP_VERIFY     = "Verify UDF Format";
	private final static String STR_TIP_TAB_DESC   = "Descriptor View";
	private final static String STR_TIP_TAB_DIR    = "Directory View";
	private final static String STR_TIP_TAB_VERIFY = "Verify View";

	/// ボタン用アクションコマンド
	private final static String STR_BTN_READ_FILE  = "btn_read_file";
	private final static String STR_BTN_READ_XML   = "btn_read_xml";
	private final static String STR_BTN_READ_DISC  = "btn_read_disc";
	private final static String STR_BTN_WRITE_FILE = "btn_write_file";
	private final static String STR_BTN_WRITE_XML  = "btn_write_xml";
	private final static String STR_BTN_WRITE_DISC = "btn_write_disc";
	private final static String STR_BTN_VERIFY     = "btn_verify";

	// GUI コンポーネント
	/// メニュー
	private JMenuBar menubar = new JMenuBar();

	//// File
	private JMenu menu_file = new JMenu("File");
	private final JMenuItem mi_file_read_file  = new JMenuItem(STR_FILE_READ_FILE);
	private final JMenuItem mi_file_read_xml   = new JMenuItem(STR_FILE_READ_XML);
	private final JMenuItem mi_file_read_disc  = new JMenuItem(STR_FILE_READ_DISC);
	private final JMenuItem mi_file_write_file = new JMenuItem(STR_FILE_WRITE_FILE);
	private final JMenuItem mi_file_write_xml  = new JMenuItem(STR_FILE_WRITE_XML);
	private final JMenuItem mi_file_write_disc = new JMenuItem(STR_FILE_WRITE_DISC);
	private final JMenuItem mi_file_exit       = new JMenuItem(STR_FILE_EXIT);

	//// View
	private JMenu menu_view = new JMenu("View");
	private final JMenuItem mi_view_dir  = new JMenuItem(STR_VIEW_DIR);
	private final JMenuItem mi_view_desc = new JMenuItem(STR_VIEW_DESC);
	private final JMenuItem mi_view_verify = new JMenuItem(STR_VIEW_VERIFY);

	//// Tool
	private JMenu menu_tool = new JMenu("Tool");
	private final JMenuItem mi_tool_verify = new JMenuItem(STR_TOOL_VERIFY);
	private final JMenuItem mi_tool_option = new JMenuItem(STR_TOOL_OPTION);

	//// Help
	private JMenu menu_help = new JMenu("Help");
//	private final JMenuItem mi_help_help  = new JMenuItem(str_help_help);
	private final JMenuItem mi_help_about = new JMenuItem(STR_HELP_ABOUT);

	/// ツールバー
	private JToolBar toolbar = new JToolBar();

	//// ボタン
	private JButton btn_read_file;
	private JButton btn_read_xml;
	private JButton btn_read_disc;
	private JButton btn_write_file;
	private JButton btn_write_xml;
	private JButton btn_write_disc;
	private JButton btn_verify;

	//// コンボボックス
//	JComboBox cmb_select_view = new JComboBox();

	/// ツリービュー
	private JTree desc_tree;
	private JTree dir_tree;
	private DescTreeNode desc_root;
	private DirTreeNode dir_root;
	private DefaultTreeModel desc_model;
	private DefaultTreeModel dir_model;

	// テキストビュー
	private JTextArea verify_text;

	/// タブコントロール
	private JTabbedPane tab_pane;
	private Container content_pane;
	private JScrollPane desc_pane;
	private JScrollPane dir_pane;
	private JScrollPane verify_pane;

	/// ボリューム領域情報
	private JScrollPane volinfo_pane;
	private JPanel volinfo_panel;
	private JBitmapColorSet volinfo_color;
//	private byte voldata[];

//	// ファイルダイアログのカレントディレクトリ
//	private String current_dir = null;

	// UDF オブジェクト
	private UDF_Env env;
	private UDF_Image image;

	// 読み込み進捗表示用
	private JDialog progress_dialog;
	private JProgressBar progress_bar;

	// Options
	private String current_dir;
	private String default_ip;
	private String default_drv;


	// 定数
	private final static int LOAD = 0;
	private final static int SAVE = 1;

	public final static int FT_IMAGE = 0;
	public final static int FT_XML = 1;

	// 以下の数値を変更することで順番を変えられる
	private final static int DIR_INDEX = 0;
	private final static int DESC_INDEX = 1;
	private final static int VERIFY_INDEX = 2;

	// 設定データ
	private Preferences pref = null;

	// バックストアのキー
	private final static String KEY_IP = "DefaultIp";
	private final static String KEY_DRV = "DefaultDrive";
	private final static String KEY_CURRENT_DIR = "CurrentDri";


	/**
		コンストラクタ
	*/
	public MainFrame() {
		// デフォルト値の取得
		init_default_values();

		load_resource();

		tab_pane = new JTabbedPane(SwingConstants.TOP);
		content_pane = getContentPane();

		init_menu();
		init_toolbar();
		init_tab_pane();
		init_volume_info_panel();

		set_need_data_items_visible(false);
		setTitle(STR_TITLE);

	}

	/**
		アプリケーションのデフォルト値をバックストアから取得<br>
		無ければ適当な値を設定する。
	*/
	private void init_default_values() {
		// バックストアに無かった場合のデフォルト値を設定
//		try {
//			// ローカルマシンのアドレスを取得
//			default_ip = InetAddress.getLocalHost().getHostAddress();
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//			default_ip = "127.0.0.1"; // とりあえずローカルホスト
//		}
		default_ip = "127.0.0.1";
		default_drv = "D"; // Windows では D が一般的?

		// ファイル選択ダイアログのカレントを取得
		JFileChooser f = new JFileChooser();
		current_dir = f.getCurrentDirectory().getAbsolutePath();

		// バックストアからデフォルト値を取得
		pref = Preferences.userNodeForPackage(this.getClass());
		try {
			default_ip = pref.get(KEY_IP, default_ip);
			default_drv = pref.get(KEY_DRV, default_drv);
			current_dir = pref.get(KEY_CURRENT_DIR, current_dir);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

//		System.out.println(default_ip);
//		System.out.println(default_drv);
//		System.out.println(current_dir);

		OptionDialog.set_current_dir(current_dir);
		OptionDialog.set_ip(default_ip);
		OptionDialog.set_drv(default_drv);
	}


	/**
		アイコンなどリソースを読み込む
	*/
	private void load_resource() {
		ClassLoader cl = getClass().getClassLoader();
		icon_read_file = new ImageIcon(cl.getResource("com/udfv/gui/resource/hd2.gif"));
		icon_read_xml = new ImageIcon(cl.getResource("com/udfv/gui/resource/xml2.gif"));
		icon_read_disc = new ImageIcon(cl.getResource("com/udfv/gui/resource/disc2.gif"));
		icon_write_file = new ImageIcon(cl.getResource("com/udfv/gui/resource/hd1.gif"));
		icon_write_xml = new ImageIcon(cl.getResource("com/udfv/gui/resource/xml1.gif"));
		icon_write_disc = new ImageIcon(cl.getResource("com/udfv/gui/resource/disc1.gif"));
		icon_verify = new ImageIcon(cl.getResource("com/udfv/gui/resource/verify.gif"));
	}

	/**
		メニューバーの初期化
	*/
	private void init_menu() {
		// File
		menu_file.add(mi_file_read_file);
		menu_file.add(mi_file_read_xml);
		menu_file.add(mi_file_read_disc);
		menu_file.addSeparator();
		menu_file.add(mi_file_write_file);
		menu_file.add(mi_file_write_xml);
		menu_file.add(mi_file_write_disc);
		menu_file.addSeparator();
		menu_file.add(mi_file_exit);

		// View
		menu_view.add(mi_view_dir);
		menu_view.add(mi_view_desc);
		menu_view.add(mi_view_verify);

		// Tool
		menu_tool.add(mi_tool_verify);
		menu_tool.addSeparator();
		menu_tool.add(mi_tool_option);

		// Help
// 		menu_help.add(mi_help_help);
//		menu_help.addSeparator();
		menu_help.add(mi_help_about);

		// 登録
		menubar.add(menu_file);
		menubar.add(menu_view);
		menubar.add(menu_tool);
		menubar.add(menu_help);
		setJMenuBar(menubar);

		// リスナ
		mi_file_read_file.addActionListener(this);
		mi_file_read_xml.addActionListener(this);
		mi_file_read_disc.addActionListener(this);
		mi_file_write_file.addActionListener(this);
		mi_file_write_xml.addActionListener(this);
		mi_file_write_disc.addActionListener(this);
		mi_file_exit.addActionListener(this);
		mi_view_dir.addActionListener(this);
		mi_view_desc.addActionListener(this);
		mi_view_verify.addActionListener(this);
		mi_tool_verify.addActionListener(this);
		mi_tool_option.addActionListener(this);
//		mi_help_help.addActionListener(this);
		mi_help_about.addActionListener(this);
	}

	/**
		ツールバーの初期化
	*/
	private void init_toolbar() {
		// コンポーネントの準備
		/// ボタン
		btn_read_file = new JButton(icon_read_file);
		btn_read_xml = new JButton(icon_read_xml);
		btn_read_disc = new JButton(icon_read_disc);
		btn_write_file = new JButton(icon_write_file);
		btn_write_xml = new JButton(icon_write_xml);
		btn_write_disc = new JButton(icon_write_disc);
		btn_verify = new JButton(icon_verify);

		//// アクションコマンドとリスナを登録
		btn_read_file.setActionCommand(STR_BTN_READ_FILE);
		btn_read_xml.setActionCommand(STR_BTN_READ_XML);
		btn_read_disc.setActionCommand(STR_BTN_READ_DISC);
		btn_write_file.setActionCommand(STR_BTN_WRITE_FILE);
		btn_write_xml.setActionCommand(STR_BTN_WRITE_XML);
		btn_write_disc.setActionCommand(STR_BTN_WRITE_DISC);
		btn_verify.setActionCommand(STR_BTN_VERIFY);

		btn_read_file.addActionListener(this);
		btn_read_xml.addActionListener(this);
		btn_read_disc.addActionListener(this);
		btn_write_file.addActionListener(this);
		btn_write_xml.addActionListener(this);
		btn_write_disc.addActionListener(this);
		btn_verify.addActionListener(this);

		// ツールバーに登録
		toolbar.setFloatable(false);
		toolbar.add(btn_read_file);
		toolbar.add(btn_read_xml);
		toolbar.add(btn_read_disc);
		toolbar.addSeparator();
		toolbar.add(btn_write_file);
		toolbar.add(btn_write_xml);
		toolbar.add(btn_write_disc);
		toolbar.addSeparator();
		toolbar.add(btn_verify);
		toolbar.addSeparator();
//		toolbar.add(cmb_select_view);

		// ツールチップ設定
		btn_read_file.setToolTipText(STR_TIP_READ_FILE);
		btn_read_xml.setToolTipText(STR_TIP_READ_XML);
		btn_read_disc.setToolTipText(STR_TIP_READ_DISC);
		btn_write_file.setToolTipText(STR_TIP_WRIT_FILE);
		btn_write_xml.setToolTipText(STR_TIP_WRITE_XML);
		btn_write_disc.setToolTipText(STR_TIP_WRITE_DISC);
		btn_verify.setToolTipText(STR_TIP_VERIFY);

		content_pane.setLayout(new BorderLayout());
		content_pane.add(toolbar, BorderLayout.NORTH);
	}

	/**
		タブペインの初期化
	*/
	private void init_tab_pane() {
		// デスクリプタ
		desc_root = new DescTreeNode("root", null);
		desc_model = new DefaultTreeModel(desc_root);
		desc_tree = new JTree(desc_model);
		desc_tree.setShowsRootHandles(true);
		desc_pane = new JScrollPane(desc_tree);

		// ディレクトリ
		dir_root = new DirTreeNode("root", null, null);
		dir_model = new DefaultTreeModel(dir_root);
		dir_tree = new JTree(dir_model);
		dir_tree.setShowsRootHandles(true);
		dir_pane = new JScrollPane(dir_tree);

		// ベリファイ
		verify_text = new JTextArea();
		verify_text.setEditable(false);
		verify_pane = new JScrollPane(verify_text);

		// タブに追加
		tab_pane.add(dir_pane, DIR_INDEX);
		tab_pane.add(desc_pane, DESC_INDEX);
		tab_pane.add(verify_pane, VERIFY_INDEX);

		// 各タブのタイトル
		tab_pane.setTitleAt(DIR_INDEX, "Directory");
		tab_pane.setTitleAt(DESC_INDEX, "Descriptor");
		tab_pane.setTitleAt(VERIFY_INDEX, "Verify");

		content_pane.add(tab_pane, BorderLayout.CENTER);

		// ツールチップ登録
		tab_pane.setToolTipTextAt(DIR_INDEX, STR_TIP_TAB_DIR);
		tab_pane.setToolTipTextAt(DESC_INDEX, STR_TIP_TAB_DESC);
		tab_pane.setToolTipTextAt(VERIFY_INDEX, STR_TIP_TAB_VERIFY);

		// リスナ登録
		desc_tree.addMouseListener(this);
		dir_tree.addMouseListener(this);

		// ダブルクリックによるツリーの開閉をできなくする
		desc_tree.setToggleClickCount(0);
		dir_tree.setToggleClickCount(0);
	}

	/**
		ボリューム領域情報の初期化
	*/
	private void init_volume_info_panel() {
		volinfo_panel = new JPanel();
		volinfo_panel.setPreferredSize(new Dimension(1000, 100));

		volinfo_pane = new JScrollPane();
		volinfo_pane.setViewportView(volinfo_panel);
		volinfo_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		content_pane.add(volinfo_pane, BorderLayout.SOUTH);
//		System.out.println("volinfo_pane.size = " + volinfo_pane.getBounds());

		volinfo_color = new JBitmapColorSet();
		volinfo_color.setColor(JBitmapColorSet.FREE, Color.LIGHT_GRAY);
	}


	/**
		メニュー、ボタンのアクションを受け取るリスナ
		@param ev アクションイベント
	*/
	public void actionPerformed(ActionEvent ev) {
		String cmd = ev.getActionCommand();
//		System.out.println("!" + cmd + "!");


		// イメージファイルから読み込み
		if (cmd.equals(STR_FILE_READ_FILE) || cmd.equals(STR_BTN_READ_FILE)) {
			if (read_file()) {
				make_desc_tree();
				make_dir_tree();
				make_volinfo();

				volinfo_pane.setViewportView(volinfo_panel);
				volinfo_pane.getHorizontalScrollBar().setValue(0);

				javax.swing.text.Document doc =  (javax.swing.text.Document)verify_text.getDocument();
				try {
					doc.remove(0, doc.getLength());
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// XML の読み込み
		else if (cmd.equals(STR_FILE_READ_XML) || cmd.equals(STR_BTN_READ_XML)) {
			if (read_xml()) {
				make_desc_tree();
				make_dir_tree();
				make_volinfo();
				volinfo_pane.setViewportView(volinfo_panel);
				volinfo_pane.getHorizontalScrollBar().setValue(0);

				javax.swing.text.Document doc =  (javax.swing.text.Document)verify_text.getDocument();
				try {
					doc.remove(0, doc.getLength());
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// DISC から読み込み
		else if (cmd.equals(STR_FILE_READ_DISC) || cmd.equals(STR_BTN_READ_DISC)) {
			if (read_disc()) {
				make_desc_tree();
				make_dir_tree();
				make_volinfo();

				volinfo_pane.setViewportView(volinfo_panel);
				volinfo_pane.getHorizontalScrollBar().setValue(0);

				javax.swing.text.Document doc =  (javax.swing.text.Document)verify_text.getDocument();
				try {
					doc.remove(0, doc.getLength());
				}
				catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

		// イメージファイルの書き込み
		else if (cmd.equals(STR_FILE_WRITE_FILE) || cmd.equals(STR_BTN_WRITE_FILE)) {
			write_file();
		}

		// XML の書き込み
		else if (cmd.equals(STR_FILE_WRITE_XML) || cmd.equals(STR_BTN_WRITE_XML)) {
			save_xml();
		}

		// DISC へ書き込み
		else if (cmd.equals(STR_FILE_WRITE_DISC) || cmd.equals(STR_BTN_WRITE_DISC)) {
			write_disc();
		}

		// ベリファイ
		else if (cmd.equals(STR_TOOL_VERIFY) || cmd.equals(STR_BTN_VERIFY)) {
			verify();
		}

		// Exit
		else if (cmd.equals(STR_FILE_EXIT)) {
			dispose();
		}

		// タブ変更 ディレクトリ表示
		else if (cmd.equals(STR_VIEW_DIR)) {
			tab_pane.setSelectedIndex(DIR_INDEX);
		}

		// タブ変更 デスクリプタ表示
		else if (cmd.equals(STR_VIEW_DESC)) {
			tab_pane.setSelectedIndex(DESC_INDEX);
		}

		// タブ変更 ベリファイ表示
		else if (cmd.equals(STR_VIEW_VERIFY)) {
//			System.out.println("hoge");
			tab_pane.setSelectedIndex(VERIFY_INDEX);
		}

		else if (cmd.equals(STR_TOOL_OPTION)) {
			//OptionDialog opt = new OptionDialog(this);
			//opt.show();
			option_dialog();
		}


		// about
		else if (cmd.equals(STR_HELP_ABOUT)) {
			about_dialog();
		}

		else {
//			System.out.println("??" + cmd);
		}
	}

	/**
		クリックイベント
		@param ev マウスイベント
	*/
	public void mouseClicked(MouseEvent ev) {
		int btn_no = ev.getButton();
		int count = ev.getClickCount();
		String str = ev.paramString();
		//System.out.println("[MOUSE] btn : " + btn_no + "  / cnt = " + count + "  / str = " + str);

		// ダブルクリックなら
		if (count == 2) {
			int x, y;
			x = ev.getX();
			y = ev.getY();

			Component current_pane = tab_pane.getSelectedComponent();

			// デスクリプタを表示中なら
			if (current_pane == desc_pane) {
				TreePath tp = desc_tree.getClosestPathForLocation(x, y);
				Rectangle rc = desc_tree.getPathBounds(tp);

				if (rc.contains(x, y)) {
					DescTreeNode node = (DescTreeNode)tp.getLastPathComponent();

					if (node == desc_root) {
						return;
					}

					String title_str = node.getName();
					DescInfoDialog info = new DescInfoDialog(
						(Frame)this, title_str, true, node.getElement());
					info.setBounds(getCenterRect(500, 400));
					info.show();
				}
			}

			// ディレクトリを表示中なら
			else {
				TreePath tp = dir_tree.getClosestPathForLocation(x, y);
				Rectangle rc = dir_tree.getPathBounds(tp);

				if (rc.contains(x, y)) {
					DirTreeNode node = (DirTreeNode)tp.getLastPathComponent();
					if (node == dir_root) {
						return;
					}

					String title_str = node.getName();
					DirInfoDialog info = new DirInfoDialog(
						(Frame)this, title_str, true, node.getElement257());
					info.setBounds(getCenterRect(500, 400));
					//info.set_top_node(node);
					info.show();
				}
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mousePressed(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}

	/**
		イメージファイルから UDF の読み込み
		@return 実行結果
			@retval true  成功
			@retval false 失敗あるいは中断
	*/
	private boolean read_file() {
		Vector filters = new Vector();
		filters.add(new IsoImageFileFilter());

		File file = file_dialog(LOAD, filters);
		if (file == null) {
			return false;
		}
		//String name = file.getAbsolutePath();

		return read_file2(file);
	}

	private boolean read_file2(File file) {
		//ReadFile thread = new ReadFile(file);
		FileThread thread = new FileThread(file, FileThread.READ_FILE);
		Inspector inspector = new Inspector();
		inspector.set_object(thread);

		inspector.start();
		thread.start();

		start_progress(LOAD);

		return check_read_error(thread);
	}

	/**
		XML ファイルの読み込み
		@return 実行結果
			@retval true  成功
			@retval false 失敗あるいは中断
	*/
	private boolean read_xml() {
		Vector filters = new Vector();
		filters.add(new XMLFileFilter());

		File file = file_dialog(LOAD, filters);
		if (null == file) {
			return false;
		}

		return read_xml2(file);
	}

	private boolean read_xml2(File file) {
		FileThread thread = new FileThread(file, FileThread.READ_XML);
		Inspector inspector = new Inspector();
		inspector.set_object(thread);

		inspector.start();
		thread.start();

		start_progress(LOAD);
//		System.out.println("read thread end?");

		return check_read_error(thread);
	}

	/**

	*/
	private boolean read_disc() {
		IPDialog.set_ip(default_ip);
		IPDialog.set_drv(default_drv);
		IPDialog d = new IPDialog(this, "Read Disc", true);
		Dimension sz = d.getSize();
		d.setBounds(getCenterRect(sz.width, sz.height));
		d.show();

		int result = d.get_result();

		if (IPDialog.OK == result) {
			String ip = d.get_ip();
			String drv = d.get_drv();

//			System.out.println("IP Dialog result");
//			System.out.println("IP = " + ip);
//			System.out.println("Drive = " + drv);

			return read_disc2(ip, drv);
		}

		return false;
	}

	/**
	*/
	private boolean read_disc2(String ip, String drv) {
		FileThread thread = new FileThread(ip, drv, FileThread.READ_DISC);
		Inspector inspector = new Inspector();
		inspector.set_object(thread);

		inspector.start();
		thread.start();

		start_progress(LOAD);
//		System.out.println("read disc end?");

		return check_read_error(thread);
	}


	private boolean check_read_error(FileThread thread) {
		if (thread.error) {
			String msg = null;
			if (thread.exception != null) {
				msg = thread.exception.getMessage();
				if (msg == null) {
					msg = new String("ERROR");
				}
				System.out.println(thread.exception);
				thread.exception.printStackTrace();
			}

			JOptionPane.showMessageDialog(this, msg, "Error",
					JOptionPane.ERROR_MESSAGE);

			return false;
		}

		return true;
	}

	/**
		イメージファイルの書き込み
	*/
	private boolean write_file() {
		if (null == image) {
			return false;
		}

		Vector filters = new Vector();
		filters.add(new IsoImageFileFilter());

		File file = file_dialog(SAVE, filters);
		if (null == file) {
			return false;
		}

		//SaveFile thread = new SaveFile(file);
		FileThread thread = new FileThread(file, FileThread.WRITE_FILE);
		Inspector inspector = new Inspector();
		inspector.set_object(thread);

		thread.start();
		inspector.start();

		start_progress(SAVE);

		if (thread.error) {
			String msg = null;
			if (thread.exception != null) {
				msg = thread.exception.getMessage();
				System.out.println(thread.exception);
				thread.exception.printStackTrace();
			}
			if (msg == null) {
				msg = new String("ERROR");
			}
			JOptionPane.showMessageDialog(this, msg, "Error",
					JOptionPane.ERROR_MESSAGE);

			return false;
		}

		return true;
	}


	/**
		XML ファイルの保存
		@return 実行結果
			@retval true 成功
			@retval false 失敗あるいは中断
	*/
	private boolean save_xml() {
		if (null == image) {
			return false;
		}

		Vector filters = new Vector();
		filters.add(new XMLFileFilter());

		File file = file_dialog(SAVE, filters);
		if (null == file) {
			return false;
		}

		try {
			image.outputXML(new FileOutputStream(file));
		}
		catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();

			String msg = e.getMessage();
			if (msg == null) {
				msg = new String("ERROR");
			}
			JOptionPane.showMessageDialog(this, msg, "Error!",
					JOptionPane.ERROR_MESSAGE);

			return false;
		}
		JOptionPane.showMessageDialog(this, "Complete", "Save",
				JOptionPane.INFORMATION_MESSAGE);

		return true;
	}

	private boolean write_disc() {
		if (null == image) {
			return false;
		}

		IPDialog.set_ip(default_ip);
		IPDialog.set_drv(default_drv);
		IPDialog d = new IPDialog(this, "Write Disc", true);
		Dimension sz = d.getSize();
		d.setBounds(getCenterRect(sz.width, sz.height));
		d.show();

		int result = d.get_result();

		if (IPDialog.OK != result) {
			return false;
		}

		String ip = d.get_ip();
		String drv = d.get_drv();

		return write_disc2(ip, drv);
	}


	private boolean write_disc2(String ip, String drv) {
		// イメージファイルのためにテンポラリファイルを作成
		File tmpfile;
		try {
			tmpfile = File.createTempFile("udfv", "img");
		}
		catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			String msg = new String("Can't open temporary file");
			JOptionPane.showMessageDialog(this, msg, "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		FileThread thread = new FileThread(ip, drv, tmpfile, FileThread.WRITE_DISC);
		Inspector inspector = new Inspector();
		inspector.set_object(thread);

		thread.start();
		inspector.start();

		start_progress(SAVE);

		if (thread.error) {
			String msg = null;
			if (thread.exception != null) {
				msg = thread.exception.getMessage();
				System.out.println(thread.exception);
				thread.exception.printStackTrace();
			}
			if (msg == null) {
				msg = new String("ERROR");
			}
			JOptionPane.showMessageDialog(this, msg, "Error",
					JOptionPane.ERROR_MESSAGE);

			return false;
		}

		return true;
	}


	private void verify() {
		try {
			UDF_ErrorList el = image.verify();
			javax.swing.text.Document doc =  verify_text.getDocument();
			doc.remove(0, doc.getLength());

			int sz = el.getSize();
			int i;
			for (i = 0; i < sz; i++) {
				UDF_Error err = el.getError(i);
				verify_text.append(err.toString());
				verify_text.append("\n");
			}

			//VerifyDialog dialog = new VerifyDialog(this, "Verify", true, javax.swing.text.Document);
			//dialog.setBounds(getCenterRect(500, 400));
			//dialog.show();
		}
		catch (Exception e) {
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

	/**
		オプションダイアログの処理
	*/
	private void option_dialog() {
		OptionDialog o = new OptionDialog(this);
		o.setBounds(getCenterRect(500, 400));
		o.show();

		String file_dir = OptionDialog.get_current_dir();
		String ip_ip = OptionDialog.get_ip();
		String ip_drv = OptionDialog.get_drv();

		if (!file_dir.equals(current_dir)) {
			current_dir = file_dir;
			pref.put(KEY_CURRENT_DIR, file_dir);
		}

		if (!ip_ip.equals(default_ip)) {
			default_ip = ip_ip;
			pref.put(KEY_IP, ip_ip);
		}

		if (!ip_drv.equals(default_drv)) {
			default_drv = ip_drv;
			pref.put(KEY_DRV, ip_drv);
		}
	}



	/**
		about ダイアログを表示
	*/
	private void about_dialog() {
		// "$Rev: xxx $" -> "xxx"
		String version = UDF_Env.system_version;
		version = version.substring(6, version.length() - 2);
		String msg = STR_TITLE + "\nCopyright 2005 Heart Solutions, Inc.\nVersion "
		             + version;

		JOptionPane.showMessageDialog(
			this,
			msg,
			STR_HELP_ABOUT,
			JOptionPane.PLAIN_MESSAGE
		);
	}



	/**
		ファイルの開く/保存ダイアログボックス処理
		@param flag LOAD 開くダイアログボックス / SAVE 保存ダイアログボックス
		@return ユーザーが選択したファイルの File オブジェクト
	*/
	public File file_dialog(int flag, Vector filters) {
		File file = null;

		JFileChooser dialog = new JFileChooser();
		if (null != filters) {
			int i;
			for (i = 0; i < filters.size(); i++) {
				javax.swing.filechooser.FileFilter ff
				        = (javax.swing.filechooser.FileFilter)filters.get(i);
				dialog.addChoosableFileFilter(ff);
			}
		}

		// ディレクトリ設定
		if (current_dir != null) {
			dialog.setCurrentDirectory(new File(current_dir));
		}

		if (flag == LOAD) {
			int state = dialog.showOpenDialog(this);
			if (JFileChooser.APPROVE_OPTION == state) {
				file = dialog.getSelectedFile();
				current_dir = file.getParent();
				return file;
			}
		}
		else {
			int state = dialog.showSaveDialog(this);
			if (JFileChooser.APPROVE_OPTION == state) {
				file = dialog.getSelectedFile();
				current_dir = file.getParent();
				return file;
			}
		}

		return null;
	}

	/**
		UDF_Element のツリーリストを作成する
	*/
	private void make_desc_tree() {
		if (null != image) {
			desc_root.removeAllChildren();

			int i;
			UDF_Element children[] = image.env.root.getChildren();
			for (i = 0; i < children.length; i++) {
				desc_root.add(children[i]);
			}

			desc_tree.expandRow(0);

			set_need_data_items_visible(true);

		}
		else {
			desc_root.removeAllChildren();
//			System.out.println("image is null");
			set_need_data_items_visible(false);
		}
		desc_model.reload();
	}

	/**
		ディレクトリツリーを作成する
	*/
	private void make_dir_tree() {
		if (null != image) {

			// root 直下を削除
			dir_root.removeAllChildren();

			UDF_ElementList list = image.env.getRootFE(0).getDirectoryList();

			// ルートの下を登録する
			int i;
			for (i = 0; i < list.size(); i++) {
				com.udfv.ecma167.UDF_desc257 element;
				element = (com.udfv.ecma167.UDF_desc257)list.elementAt(i);

				// 親ディレクトリ(../)はとばす
				if (UDF_desc257.PARENT
					  == (element.getFileChar().getIntValue() & UDF_desc257.PARENT)) {
					continue;
				}
/*				// 削除されたファイルを表示しないならこうする。
				if (UDF_desc257.DELETED
					  == (element.getFileChar().getIntValue() & UDF_desc257.DELETED)) {
					continue;
				}
*/
				String name = element.getFileId().getStringData();
				int idx = 1 + name.indexOf(":");
				name = name.substring(idx);

				// ファイル or ディレクトリを追加
				dir_root.add(name, element.getReferenceTo(), element);
			}

			// ルート直下だけ展開しておく
			dir_tree.expandRow(0);
		}
		else {
//			System.out.println("image is null");
			dir_root.removeAllChildren();
		}
		dir_model.reload();
	}

	/**
		ボリューム表示領域を作成
	*/
	private void make_volinfo() {
		int sec = 0;
		try {
			sec = (int)(image.getLongSize() / image.env.LBS);
//			System.out.println("sec size = " + sec);
		}
		catch (Exception e) {
			sec = 0;
			System.out.println(e);
			e.printStackTrace();
		}
//		System.out.println("!!" + sec);
		if (sec == 0) {
//			System.out.println("UDF_Env.image_total_sec is 0");
		}

		try {
			int max = 2048; /* 4096; */
			int width = (sec < max) ? sec: max;
//			System.out.println("width = " + width);
			JPanel tmp = (JPanel)image.createJBitmapPanel(width, 100, volinfo_color);
			if (tmp != null) {
				volinfo_panel = tmp;
			}

		}
		catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	/**
		保存やベリファイなどデータを読み込んでいないと操作できない
		メニューやボタンの表示切り替え
		@param flag true:操作可能 false:操作不可
	*/
	private void set_need_data_items_visible(boolean flag) {
		mi_file_write_file.setEnabled(flag);
		mi_file_write_xml.setEnabled(flag);
		mi_file_write_disc.setEnabled(flag);
//		mi_view_dir.setEnabled(flag);
//		mi_view_desc.setEnabled(flag);
		mi_tool_verify.setEnabled(flag);

		btn_write_file.setEnabled(flag);
		btn_write_xml.setEnabled(flag);
		btn_write_disc.setEnabled(flag);
		btn_verify.setEnabled(flag);
	}


	/**
		プログレスダイアログの表示
	*/
	private void start_progress(int flag) {
		String title_str;
		if (LOAD == flag) {
			title_str = new String("Reading");
		}
		else {
			title_str = new String("Saveing");
		}

		// モーダルダイアログ、クローズボタンの無効
		progress_dialog = new JDialog(this, title_str, true);
		progress_dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		Container pane = progress_dialog.getContentPane();
		pane.setLayout(new FlowLayout());

		// プログレスバーの配置
		progress_bar = new JProgressBar();
		progress_bar.setForeground(Color.blue);
		progress_bar.setValue(0);
		progress_bar.setStringPainted(true);
		pane.add(progress_bar, BorderLayout.CENTER);

		progress_dialog.setBounds(getCenterRect(200, 100));
		progress_dialog.show();
	}

	/**
		このウィンドウの中心に表示されるウィンドウの矩形を求める
		@param w 表示したいウィンドウの幅
		@param h 表示したいウィンドウの高さ
		@return 求められた矩形
	*/
	private Rectangle getCenterRect(int w, int h) {
		Rectangle rect = getBounds(null);
		int x, y;
		x = rect.x + rect.width / 2;
		y = rect.y + rect.height / 2;

		Rectangle out = new Rectangle(x - w/2, y - h/2, w, h);
		return out;
	}

	/**
		指定されたファイルを読み込む
		@param fname ファイル名
		@param ftype ファイルの種類 FT_IMAGE : イメージファイル, FT_XML : XML ファイル
	*/
	public void setFilename(String fname, int ftype) {
		if (null == fname) {
			return;
		}

		File file = new File(fname);

		if (ftype == FT_IMAGE) {
			if (read_file2(file)) {
				make_desc_tree();
				make_dir_tree();
				make_volinfo();

				volinfo_pane.setViewportView(volinfo_panel);
				volinfo_pane.getHorizontalScrollBar().setValue(0);
			}
		}
		else if (ftype == FT_XML) {
			if (read_xml2(file)) {
				make_desc_tree();
				make_dir_tree();
				make_volinfo();
				volinfo_pane.setViewportView(volinfo_panel);
				volinfo_pane.getHorizontalScrollBar().setValue(0);
			}
		}
	}


	/**
		スレッドで UDF の読み書きを行うクラス
	*/
	class FileThread extends Thread {
		private File file;
		public boolean error;
		public Exception exception;
		private int access_type;
		private int progress_status;

		private String ip;
		private String drv;

		// アクセスタイプ
		public final static int READ_FILE  = 1;
		public final static int READ_XML   = 2;
		public final static int READ_DISC  = 3;
		public final static int WRITE_FILE = 4;
		public final static int WRITE_XML  = 5;
		public final static int WRITE_DISC = 6;

		/**
			コンストラクタ
			@param f    アクセスするファイル
			@param type アクセスタイプ
		*/
		public FileThread(File f, int type) {
			file = f;
			access_type = type;
			error = false;
			exception = null;
		}

		public FileThread(String i, String d, int type) {
			ip = i;
			drv = d;
			access_type = type;
			exception = null;
		}

		public FileThread(String i, String d, File f, int type) {
			file = f;
			ip = i;
			drv = d;
			access_type = type;
			exception = null;
		}

		/**
			スレッド起動
		*/
		public void run() {
			progress_status = 0;

			if (READ_FILE == access_type) {
				this.read_file();
			}
			else if (READ_XML == access_type) {
				this.read_xml();
			}
			else if (READ_DISC == access_type) {
				this.read_disc();
			}
			else if (WRITE_FILE == access_type) {
				progress_status = 10;
				this.write_file();
			}
			else if (WRITE_XML == access_type) {
//				this.write_xml();
			}
			else if (WRITE_DISC == access_type) {
				this.write_disc();
			}

//			System.out.println("[ThreadFile#run() end");

			return;
		}

		/**
			ファイルから読み込み
		*/
		private void read_file() {
			error = false;
			exception = null;
			String fname = file.getAbsolutePath();
			if (fname.length() == 0) {
//				System.out.println("file length = 0");
				error = true;
				return;
			}

			try {
				image = null;
				env = new UDF_Env();
				env.image_file = new String(fname);
				env.f = new UDF_RandomAccessFile(fname, "r");
				image = UDF_ImageFactory.genImage(env, env.f);
				if (image == null) {
//					System.out.println("genImage() failed");
					error = true;
					return;
				}
				image.setUDFDocument(UDF_Util.genDocument());
				image.readFrom(env.f);


//				System.out.println("read end");
			}
			catch (Exception e) {
//				System.out.println("catch in read file thread");
				System.out.println(e);
				e.printStackTrace();
				exception = e;
				error = true;
				image = null;
				env = null;
				return;
			}
			//System.out.println("C");

			return;
		}

		/**
			XML 読み込み
		*/
		private void read_xml() {
			error = false;
			exception = null;
			String fname = file.getAbsolutePath();
			if (fname.length() == 0) {
				error = true;
				return;
			}

			try {
				image = null;
				env = new UDF_Env();
				DOMParser parser = new DOMParser();
				parser.parse(fname);
				org.w3c.dom.Document input_doc = parser.getDocument();
				image = UDF_ImageFactory.genImage(env, 0x250); // ★

				if (image == null) {
					error = true;
					return;
				}

				env.f = new UDF_RandomAccessZero(env.image_size);
				image.setEnv(env);
				image.readFromXML(input_doc);

				//XMLから読むだけでは gp, offset, partno, mirrorがないので、再計算する
				image.recalc(UDF_Element.RECALC_GP, env.f);
				//XMLから読むだけでは Envが正しく設定されないので、再計算する。
				image.recalc(UDF_Element.RECALC_ENV, env.f);
				//XMLから読むだけでは Tree構造が正しく設定されないので、再計算する。
				image.recalc(UDF_Element.RECALC_TREE, env.f);
				//XMLから読むだけでは ADLIST構造が正しく設定されないので、再計算する。
				image.recalc(UDF_Element.RECALC_ADLIST, env.f);
				image.recalc(UDF_Element.RECALC_GP, env.f);

			}
			catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
				exception = e;
				error = true;
				return;
			}

			return;

		}

		/**
			ディスクから読み込み
		*/
		private void read_disc() {
			error = false;
			exception = null;
			try {
				image = null;
				env = new UDF_Env();
				env.f = new UDF_RandomAccessATAPI(ip, drv);
				image = UDF_ImageFactory.genImage(env, env.f);
				if (image == null) {
//					System.out.println("genImage() failed");
					error = true;
					return;
				}
				image.setUDFDocument(UDF_Util.genDocument());
//				System.out.println("read start");
				image.readFrom(env.f);


//				System.out.println("read end");
			}
			catch (Exception e) {
//				System.out.println("catch in read file thread");
				System.out.println(e);
				e.printStackTrace();
				exception = e;
				error = true;
				image = null;
				env = null;
				return;
			}
			//System.out.println("C");

			return;
		}



		/**
			イメージファイル書き込み
		*/
		private void write_file() {
			error = false;
			exception = null;
			String fname = file.getAbsolutePath();
			if (fname.length() == 0) {
				error = true;
				return;
			}

			try {
				if (file.exists()) {
					file.delete();
				}

				UDF_RandomAccessFile f = new UDF_RandomAccessFile(fname);
				env.f = f;
				image.setEnv(env);
				image.writeTo(f);
				//f.close();
				// MEMO
				// もし GUI ツールにデータ変更機能が加わったとき、
				// env.f や env.image_file を読み込みのファイルにするか
				// 書き込んだファイルにするかの問題が出る気がする。
				// 今は編集しないからこのままでとりあえず問題は無いと思う。
				// 05.06.02 kawa
			}
			catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
				exception = e;
				error = true;
				return;
			}

			return;
		}

		private void write_disc() {
			error = false;
			exception = null;
//			String fname = file.getAbsolutePath();
			String fname = new String("c:\\temp\\keroro.img");
			if (fname.length() == 0) {
				error = true;
				return;
			}

//			try {
//				if (file.exists()) {
//					file.delete();
//				}
//
//				UDF_RandomAccessFile f = new UDF_RandomAccessFile(fname);
//				env.f = f;
//				image.setEnv(env);
//				image.writeTo(f);
//			}
//			catch (Exception e) {
//				System.out.println(e);
//				e.printStackTrace();
//				exception = e;
//				error = true;
//				return;
//			}
//			System.out.println("write disc end\n");

			try {
//				System.out.println("write start");
				UDF_RandomAccessATAPI f = new UDF_RandomAccessATAPI(ip, drv);
				f.write_image(fname);
			}
			catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
				error = true;
				exception = e;
				return;
			}
		}


	}


	/**
		読み込み状況を監視してプログレスバーを設定するクラス
	*/
	class Inspector extends Thread {
		private FileThread object; // 監視対象オブジェクト

		public void set_object(FileThread o) {
			object = o;
		}

		public void run() {
//			System.out.println("[INSPECTOR] start");

			if (object == null) {
//				System.out.println("[INSPECTOR]object null");
				progress_dialog.dispose();
				return;
			}

			try {
				while (true) {
					sleep(50);
					if (!object.isAlive()) {
//						System.out.println("[INSPECTOR] break");
						break;
					}

					if (image == null) {
						if (object.error) {
							break;
						}
//						System.out.println("[INSPECTOR]continue");
						continue;
					}

					double val = (double)image.imageProgressStatus();

					progress_bar.setValue((int)val);
//					//System.out.println("[INSPECTOR] " + val);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
			}

//			System.out.println("[INSPECTOR] loop end");
			progress_dialog.dispose();
		}
	}
}


class IsoImageFileFilter extends javax.swing.filechooser.FileFilter {
	/**
		このフィルタが指定されたファイルを受け付けるかどうかを返す。
		拡張子のみで判断するため判定結果が必ずあっているというわけ
		ではない。
		@pram f ファイル
		@return 判定結果
			@retval true  受け付ける
			@retval false 受け付けない
	*/
	public boolean accept(File f) {
		if (null == f) {
			return false;
		}

		if (f.isDirectory()) {
			return true;
		}

		String fname = f.getPath();
		String suffix = null;
		int i = fname.lastIndexOf(".");
		if (i > 0 && i < fname.length() - 1) {
			suffix = fname.substring(i + 1).toLowerCase();
		}

		if (suffix != null
		        && (suffix.equals("img") || suffix.equals("iso"))) {
			return true;
		}

		return false;
	}

	/**
		このフィルタの説明を返す
		@return 説明の文字列
	*/
	public String getDescription() {
		return "Image Files (*.img *.iso)";
	}
}

class XMLFileFilter extends javax.swing.filechooser.FileFilter {
	/**
		このファイルが XML であるかどうかを返す
		@param f ファイル
		@return 判定結果
	*/
	public boolean accept(File f) {
		if (null == f) {
			return false;
		}

		if (f.isDirectory()) {
			return true;
		}

		String fname = f.getPath();
		String suffix = null;
		int i = fname.lastIndexOf(".");
		if (i > 0 && i < fname.length() - 1) {
			suffix = fname.substring(i + 1).toLowerCase();
		}

		if (suffix != null && suffix.equals("xml")) {
			return true;
		}

		return false;
	}

	/**
		このフィルタの説明を返す
		@return 説明の文字列
	*/
	public String getDescription() {
		return "XML Files (*.xml)";
	}
}



