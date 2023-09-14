package me.zoulei.ui.mainApp;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JFrame;

import me.zoulei.ui.components.DataSourceComponent;
import me.zoulei.ui.components.GridComponent;
import me.zoulei.ui.components.SearchComponent;

/**
 * 2023年9月12日14:59:17 zoulei
 * ui配置
 */
public class MainApp {
	
	public static JFrame mainFrame;
	
	public static void main(String[] args) {
		Container c = new Container();
		
		mainFrame = new JFrame("代码生成工具");
		mainFrame.setSize(1720, 980);
		mainFrame.setLocation(20, 20);
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
