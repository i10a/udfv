package com.udfv.gui;

import javax.swing.tree.*;

import com.udfv.access.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;


/**
	UDF_Element インスタンスを所持する MutableTreeNode クラス
*/
class DescTreeNode extends DefaultMutableTreeNode {
	private UDF_Element element;

	/**
		コンストラクタ
		@param name ノード名
		@param my   保持する UDF_Element
	*/
	public DescTreeNode(String name, UDF_Element my) {
		super(name);
//		System.out.println("create " + name);

		element = my;

		// 子ノードを登録しなくてもよいノードなら
//		if (my == null || UDF_CrcDesc.class.isAssignableFrom(element.getClass())) {
//			return;
//		}

		if (null != element) {
			UDF_Element children[] = my.getChildren();
			int i;
			for (i = 0; i < children.length; i++) {
//				DescTreeNode child
//				        = new DescTreeNode(children[i].getName(), children[i]);
				add(children[i].getName(), children[i]);
			}
		}
	}

	/**
		子ノードを追加する
		@param elm 子の UDF_Element
	*/
	public void add(UDF_Element elm) {
		//System.out.println(elm.getName());
		DescTreeNode item
		        = new DescTreeNode(elm.getName(), elm);
		super.add(item);
	}

	/**
		子ノードを追加する
		@param str ノード名
	*/
	public void add(String str) {
		DescTreeNode item
		        = new DescTreeNode("", null);
		super.add(item);
	}

	/**
		子ノードを追加する
		@param str ノード名
		@param elm 保持する UDF_Element
	*/
	public void add(String str, UDF_Element elm) {
		//System.out.println("add " + str);
		DescTreeNode item
		        = new DescTreeNode(str, elm);
		super.add(item);
	}

	public String getName() {
		if (element == null) {
			return new String("");
		}
		return element.getName();
	}

	public UDF_Element getElement() {
		return element;
	}


}
