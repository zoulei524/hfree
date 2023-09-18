package me.zoulei.ui.components;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.zoulei.MainApp;
import me.zoulei.backend.jdbc.datasource.DataSource;
import me.zoulei.exception.myException;
import me.zoulei.ui.components.SearchComponent.Item;

/**
 * 2023年9月14日11:28:15  zoulei  
 * 该组件用于配置数据库连接参数，若连接成功，则加载 搜索数据库表的组件。
 */
public class DataSourceComponent {

	public void setComp(SearchComponent sch) {
		//读取数据库配置
		String url = this.getClass().getResource("./dsProp/1.properties").getPath();
		Properties p = new Properties();
		Item[] items = null;
		
		try {
			p.load(new FileInputStream(url));
			//获取所有数据库配置
			File[] listFiles = new File(url).getParentFile().listFiles();
			items = new Item[listFiles.length];
			int i=0;
			for(File f : listFiles) {
				Properties fp = new Properties();
				fp.load(new FileInputStream(f));
				items[i++] = new Item(fp.getProperty("desc"), fp);
			}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(MainApp.mainFrame, "读取1.properties失败："+e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
		
		//选择数据库配置控件
		JComboBox<Item> dbSource = new JComboBox<Item>(items);
		dbSource.setSize(60, 30);
		//ui初始化
		JLabel  dslabel= new JLabel("数据库: ", JLabel.LEFT);
		JTextField dsText = new JTextField(p.getProperty("DBType"),5);
		
		JLabel  driverlabel= new JLabel("驱动: ", JLabel.LEFT);
		JTextField driverText = new JTextField(p.getProperty("forname"),14);
		
		JLabel  urllabel= new JLabel("jdbc-URL: ", JLabel.LEFT);
		JTextField urlText = new JTextField(p.getProperty("url"),30);
		
		JLabel  namelabel= new JLabel("用户名: ", JLabel.RIGHT);
		JTextField userText = new JTextField(p.getProperty("user"),12);
		
		JLabel  passwordLabel = new JLabel("密码: ", JLabel.CENTER);
		JTextField passwordText = new JTextField(p.getProperty("password"),12);      

		JButton loginButton = new JButton("连接数据库");
		loginButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {     
	            //测试数据库连接
	            DataSource.dsprop.setProperty("user", userText.getText());
	            DataSource.dsprop.setProperty("password", passwordText.getText());
	            DataSource.dsprop.setProperty("url", urlText.getText());
	            DataSource.dsprop.setProperty("forname", driverText.getText());
	            try {
					DataSource.testDMConn();
					//JOptionPane.showMessageDialog(MainApp.mainFrame, "连接成功！");    
					//表格配置组件
			        //GridComponent grid = new GridComponent();
			        //grid.setComp();
			        sch.setComp();
			        loginButton.setEnabled(false);
					/*
			        String url = this.getClass().getResource("./dsProp/1.properties").getPath();
			        try {
						DataSource.dsprop.store(new FileWriter(url), "1112");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			        */
				} catch (myException e1) {
					JOptionPane.showMessageDialog(MainApp.mainFrame, "连接失败："+e1.getMessage());    
				}
	            
	         }
	      }); 
		
		JPanel controlPanel = new JPanel();
	    controlPanel.setLayout(new FlowLayout());//FlowLayout是默认布局，它以方向流布局组件。
	    //数据源选择控件
	    controlPanel.add(dbSource);
	    //选择数据源事件
	    dbSource.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Item item = (Item) dbSource.getSelectedItem();
				if(item!=null) {
					Properties p = item.getProp();
					dsText.setText(p.getProperty("DBType"));
					driverText.setText(p.getProperty("forname"));
					urlText.setText(p.getProperty("url"));
					userText.setText(p.getProperty("user"));
					passwordText.setText(p.getProperty("password"));
				}
			}
		});
		
	    
	    controlPanel.add(dslabel);
	    controlPanel.add(dsText);
	    controlPanel.add(driverlabel);
	    controlPanel.add(driverText);
	    
	    controlPanel.add(urllabel);
	    controlPanel.add(urlText);
	    controlPanel.add(namelabel);
	    controlPanel.add(userText);
	    controlPanel.add(passwordLabel);       
	    controlPanel.add(passwordText);
	    controlPanel.add(loginButton);
	    //位置及大小
	    controlPanel.setBounds(0, 5, 1700, 35);
	    controlPanel.setBorder(MainApp.lineBorder);
	    
	    Container contentPane = MainApp.mainFrame.getContentPane();
	    contentPane.add(controlPanel,BorderLayout.PAGE_START);
	}
	
	/**
	 * 表名选择下拉框
	 */
	@Data
	@AllArgsConstructor
	public class Item {
		private String key;
		private Properties prop;
		public String toString(){
			return key;
		}
	}
}
