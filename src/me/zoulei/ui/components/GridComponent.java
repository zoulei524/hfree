package me.zoulei.ui.components;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.border.LineBorder;

public class GridComponent {
	public void setComp(JFrame mainFrame) {
		/*
		JPanel p = new JPanel();
		p.setBounds(0, 40, 1700, 500);
		
		
		Object[][] tableDate = new Object[15][8];
		for (int i = 0; i < 15; i++) {
			tableDate[i][0] = "1000" + i;
			for (int j = 1; j < 8; j++) {
				tableDate[i][j] = 0;
			}
		}
		String[] name = { "学号", "软件工程", "Java", "网络", "数据结构", "数据库", "总成绩", "平均成绩" };
		JTable table = new JTable(tableDate, name);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane sp = new JScrollPane(table);
		//table.setSize(20,20);
		sp.setBorder(new LineBorder(Color.red));
		p.add(sp);
		
		Container contentPane = mainFrame.getContentPane();
		contentPane.add(p);
		*/
		TableyTable table = new TableyTable(10, 28);
		table.setBounds(35,160,1600,300);
        table.setHeaderEditable(true);
        table.setTableEditable(true);
        table.setBorder(new LineBorder(Color.red));
        
        mainFrame.add(table);
	}
}
