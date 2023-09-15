package me.zoulei.ui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import me.zoulei.ui.components.DataSourceComponent;
import me.zoulei.ui.components.SearchComponent;

/**
 * 2023年9月12日14:59:17 zoulei
 * ui配置
 */
public class MainApp {
	
	public static JFrame mainFrame;
	//边框颜色
	public static Border lineBorder = new LineBorder(Color.gray);
	
	public static void main(String[] args) {
		
		
		mainFrame = new JFrame("代码生成工具");
		mainFrame.setSize(1720, 580);
		//mainFrame.setLocation(20, 20);
		mainFrame.setLocationRelativeTo(null);
        //setLayOut是设置布局的意思，当我们传入null，那后面新建的组件的位置就完全由我们所定义(即我想怎么摆就怎么摆），不受Frame约束。
		mainFrame.setLayout(null);
		mainFrame.setResizable(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //数据库连接
        DataSourceComponent dsf = new DataSourceComponent();
        //表名搜索
        SearchComponent sch = new SearchComponent();
        dsf.setComp(sch);
        
        
        
        mainFrame.setVisible(true);
	}
	
}
