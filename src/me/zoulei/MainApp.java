package me.zoulei;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import me.zoulei.ui.components.DataSourceComponent;
import me.zoulei.ui.components.SearchComponent;
import me.zoulei.ui.toolSamples.DocumentTool;

/**
 * 2023年9月12日14:59:17 zoulei
 * ui启动
 */
public class MainApp {
	
	public static JFrame mainFrame;
	//边框颜色
	public static Border lineBorder = new LineBorder(Color.gray,0);
	public static JPanel north;
	
	public static void main(String[] args) {
		
		
		mainFrame = new JFrame("odin7c开发辅助工具");
		mainFrame.setSize(1720, 580);
		//mainFrame.setLocation(20, 20);
		mainFrame.setLocationRelativeTo(null);
        //setLayOut是设置布局的意思，当我们传入null，那后面新建的组件的位置就完全由我们所定义(即我想怎么摆就怎么摆），不受Frame约束。
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setResizable(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//上边布局 NORTH组件
		north = new JPanel(new BorderLayout());
		north.setPreferredSize(new Dimension(-1, 86));
		north.setBorder(MainApp.lineBorder);
		mainFrame.add(north,BorderLayout.NORTH);
		
		//表名搜索 点击连接后的的其他组件生成
        SearchComponent sch = new SearchComponent();
		
        //数据库连接
        DataSourceComponent dsf = new DataSourceComponent();
        //数据库连接设置
        JPanel controlPanel = dsf.setComp(sch);
        controlPanel.setBorder(MainApp.lineBorder);
	    north.add(controlPanel,BorderLayout.NORTH);
	    
	    
	    
	    
	    //菜单
	    JMenuBar menuBar = new JMenuBar();
	    JMenu view = new JMenu("查看");
		menuBar.add(view);
		JMenuItem showstatus = new JMenuItem("一些常用代码");
		showstatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new DocumentTool("123");
			}
		});
		view.add(showstatus);
	    
	    
		mainFrame.setJMenuBar(menuBar);
	    
	    //logo
		java.net.URL imgUrl = MainApp.class.getResource("logo.png");
		ImageIcon imageIcon= new ImageIcon(imgUrl);
		mainFrame.setIconImage(imageIcon.getImage());
		
		
        mainFrame.setVisible(true);
	}
	
}
