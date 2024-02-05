package me.zoulei.ui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import me.zoulei.MainApp;
import me.zoulei.dbc.ui.components.MainPanel;
import me.zoulei.dbc.ui.components.orthers.VersionDialog;
import me.zoulei.ui.toolSamples.DocumentTool;

public class MainMenuBar extends JMenuBar{
	private static final long serialVersionUID = 4986776746050350761L;

	public MainMenuBar() {
		//菜单
	    JMenu view = new JMenu("查看");
		this.add(view);
		JMenuItem showstatus = new JMenuItem("一些常用代码");
		showstatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new DocumentTool("123");
			}
		});
		view.add(showstatus);
		
		
		JMenuItem dc = new JMenuItem("数据库表结构比对");
		view.add(dc);
		dc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanel.main(new String[] {"1"});
			}
		});
		
		
		
		JMenuItem about = new JMenuItem("关于");
		view.add(about);
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Map<String,String> caipinMap = new LinkedHashMap<String,String>();
		        caipinMap.put("版本","测试1.0.0");
		        caipinMap.put("日期","2023年12月1日");
		        caipinMap.put("作者","邹磊");
		        caipinMap.put("联系方式","18042307016");        
		        String desc = "数据库表反向生成odin7c的前后端代码。";
		        VersionDialog d =new VersionDialog(MainApp.mainFrame, true,caipinMap,360,320,desc);
		        d.setVisible(true);
			}
		});
	}

}
