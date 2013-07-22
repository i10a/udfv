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
package com.udfv.gui;

import javax.swing.tree.*;

import com.udfv.access.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;



// UDF_FEDesc をもつ MutableTreeNode クラス
class DirTreeNode extends DefaultMutableTreeNode {
	private UDF_FEDesc file_element;
	private UDF_desc257 element257;

	/**
		コンストラクタ
		@param desc 保持する UDF_FEDesc
	*/
	public DirTreeNode(String name, UDF_FEDesc desc, UDF_desc257 desc257) {
		super(name);

		//System.out.println("create DirTreeNode :" + name);

		file_element = desc;
		element257 = desc257;

		if (null == desc) {
			return;
		}

		// ディレクトリなら
		UDF_icbtag icbtag = desc.getICBTag();
		if (icbtag.isTypeDirectory()) {
			UDF_ElementList list = desc.getDirectoryList();

			int i;
			for (i = 0; i < list.size(); i++) {
				com.udfv.ecma167.UDF_desc257 elm
				        = (com.udfv.ecma167.UDF_desc257)list.elementAt(i);

				UDF_FEDesc fe = elm.getReferenceTo();

				String dirname = elm.getFileId().getStringData();
				int idx = 1 + dirname.indexOf(":");
				dirname = dirname.substring(idx);

				// 削除されたファイルで実体がない場合はスキップ
				if (fe == null || fe.getICBTag() == null) {
//					System.out.println("#### Warning : " + dirname);
					continue;
				}

				if (fe.getICBTag().isTypeDirectory()) {
					dirname += "/";
				}

				// 親ディレクトリならパス
				if ((elm.getFileChar().getIntValue() & UDF_desc257.PARENT) == UDF_desc257.PARENT) {
					continue;
				}

				// 子ディレクトリを作成
				add(dirname, fe, elm);
			}
		}
	}

	/**
		子ノードを追加
		@param name ノード名(ディレクトリ名)
		@param desc 保持する UDF_FEDesc
	*/
	public void add(String name, UDF_FEDesc desc, UDF_desc257 desc257) {
		DirTreeNode item = new DirTreeNode(name, desc, desc257);

		if (null != desc) {
			UDF_icbtag icbtag = desc.getICBTag();
			if (icbtag.isTypeDirectory()) {
				//System.out.println("add " + name + "/");
			}
			else {
				//System.out.println("add " + name);
			}
		}
		else {
			//System.out.println("add " + name);
		}

		super.add(item);
	}

	/**
		ノードの名前を返す
	*/
	public String getName() {
		return toString();
	}

	/**
		エレメントを返す
	*/
	public UDF_FEDesc getElement() {
		return file_element;
	}

	/**
		UDF_desc257 のエレメントを返す
	*/
	public UDF_desc257 getElement257() {
		return element257;
	}


}














