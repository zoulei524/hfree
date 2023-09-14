package me.zoulei.ui.components;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.border.LineBorder;

import me.zoulei.ui.mainApp.MainApp;

/**
 * 2023年9月14日11:17:12 zoulei
 * 表格组件，用于配置表格的参数  结构： JPanel（布局是BorderLayout，不留空隙）,里面是JScrollPane,在里面是JTable
 */
public class GridComponent {
	EditorGrid table;
	public void setComp(String tablename,String owner) {
		if(this.table!=null) {
			MainApp.mainFrame.remove(this.table);
		}
		this.table = new EditorGrid(tablename,owner);
		MainApp.mainFrame.add(this.table);
		this.table.setBounds(15,160,1680,342);
		this.table.setHeaderEditable(true);
		this.table.setTableEditable(true);
		this.table.setBorder(new LineBorder(Color.red));
	}
}
