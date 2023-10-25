package me.zoulei.ui.components;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
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

/**
 * 2023年9月14日11:28:15  zoulei  
 * 该组件用于配置数据库连接参数，若连接成功，则加载 搜索数据库表的组件。
 */
public class DataSourceComponent {
	
	public JPanel setComp(SearchComponent sch) {
		
		String baseDir = System.getProperty("user.dir")+"/dsProp";
		
		//读取数据库配置
		String url = baseDir + "/1.properties";
		Properties p = new Properties();
		Item[] items = null;
		
		try {
			p.load(new InputStreamReader(new FileInputStream(url), "utf-8"));
			//获取所有数据库配置
			File[] listFiles = new File(baseDir).listFiles();
			items = new Item[listFiles.length];
			int i=0;
			for(File f : listFiles) {
				Properties fp = new Properties();
				fp.load(new InputStreamReader(new FileInputStream(f), "utf-8"));
				items[i++] = new Item(fp.getProperty("desc"), fp, f);
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
	        	DataSource.dsprop = new Properties();
	            DataSource.dsprop.setProperty("user", userText.getText());
	            DataSource.dsprop.setProperty("password", passwordText.getText());
	            DataSource.dsprop.setProperty("url", urlText.getText());
	            DataSource.dsprop.setProperty("forname", driverText.getText());
	            try {
	            	DataSource.DBType = dsText.getText();
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
		
		JButton saveButton = new JButton("保存连接");
		saveButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {    
	        	 Item item = (Item) dbSource.getSelectedItem();
	        	 if(item!=null) {
					Properties p = item.getProp();
					p.setProperty("user", userText.getText());
		            p.setProperty("password", passwordText.getText());
		            p.setProperty("url", urlText.getText());
		            p.setProperty("forname", driverText.getText());
		            p.setProperty("DBType", dsText.getText());
		            try {
		            	item.getFile().delete();
		            	OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(item.getFile()), "utf-8");
						p.store(outputStreamWriter, new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date()));
						outputStreamWriter.close();
						JOptionPane.showMessageDialog(MainApp.mainFrame, "保存成功！"); 
					} catch (IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(MainApp.mainFrame, "保存失败："+e1.getMessage());    
					}

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
					//连接按钮可用
					loginButton.setEnabled(true);
					//断开数据库连接
	            	DataSource.closeCon();
	            	//先移除组件
	            	sch.removeAll();
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
	    controlPanel.add(saveButton);
	    //位置及大小
	    //controlPanel.setBounds(0, 5, 1700, 35);
	    
	    return controlPanel;
	}
	
	/**
	 * 表名选择下拉框
	 */
	@Data
	@AllArgsConstructor
	public class Item {
		private String key;
		private Properties prop;
		private File file;
		public String toString(){
			return key;
		}
	}
}
