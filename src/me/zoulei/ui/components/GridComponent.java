package me.zoulei.ui.components;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.border.LineBorder;

import me.zoulei.MainApp;

/**
 * 2023年9月14日11:17:12 zoulei
 * 表格组件，用于配置表格的参数  结构： JPanel（布局是BorderLayout，不留空隙）,里面是JScrollPane,在里面是JTable
 */
public class GridComponent {
	EditorGrid editorGrid;
	public void setComp(String tablename,String owner) {
		if(this.editorGrid!=null) {
			MainApp.mainFrame.remove(this.editorGrid);
		}
		this.editorGrid = new EditorGrid(tablename,owner);
		MainApp.mainFrame.add(this.editorGrid);
		this.editorGrid.setBounds(15,160,1680,342);
		this.editorGrid.setHeaderEditable(true);
		this.editorGrid.setTableEditable(true);
		this.editorGrid.setBorder(MainApp.lineBorder);
	}
}
