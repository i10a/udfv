package com.udfv.gui;

import java.net.URL;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
	UDFV GUI ツール メインクラス
*/
class UDFVGui {
	/**
		コンストラクタ
	*/
	public UDFVGui(String args[]) {
		int i;
		String fname = null;
		int ftype = MainFrame.FT_IMAGE;
		for (i = 0; i < args.length; i++) {
			if (args[i].equals("-x")) {
				ftype = MainFrame.FT_XML;
			}
			else if (args[i].equals("-h")) {
				System.out.println("java UDFVGui [options] [file]");
				System.out.println("options:");
				System.out.println("  -x : read file is XML");
				System.out.println("  -h : print help");
				System.exit(0);
			}
			else {
				if (fname == null) {
					fname = args[i];
				}
			}
		}

		MainFrame main_frame = new MainFrame();
		main_frame.setBounds(100, 100, 600, 450);
		main_frame.setVisible(true);
		main_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		main_frame.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});

		if (fname != null) {
			main_frame.setFilename(fname, ftype);
		}


	}


	/**
		メインメソッド
	*/
	public static void main(String args[]) {
		UDFVGui m = new UDFVGui(args);
	}

}
