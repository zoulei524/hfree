package me.zoulei.dbc.ui.components.north;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
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
import me.zoulei.dbc.ui.components.MainPanel;
import me.zoulei.exception.myException;

/**
 * 2023年11月14日10:53:48 zoulei
 * 该组件用于配置数据库连接参数，若连接成功，则加载 搜索数据库表的组件。  数据库比对
 */
public class DataConnComponent extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8070098865369824453L;

	//选择数据库配置控件
	static Item[] items = null;
	
	
	//数据库连接之后显示的模式选择框， 左边一个右边一个 都放在panel中
	public SchemaSelectComponent schemaSelectComponent = new SchemaSelectComponent();
	
	public DataConnComponent(JPanel north, String label) {
		DataSourceDBC dbc = new DataSourceDBC();
		
		String baseDir = System.getProperty("user.dir")+"/dsProp";
		//读取数据库配置
		String url = baseDir + "/1.properties";
		Properties p = new Properties();
		
		if(items==null) {
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
				JOptionPane.showMessageDialog(MainPanel.mainFrame, "读取1.properties失败："+e.getMessage());
				e.printStackTrace();
				System.exit(0);
			}
		}else {
			try {	
				p.load(new InputStreamReader(new FileInputStream(url), "utf-8"));
			} catch (Exception e) {
				JOptionPane.showMessageDialog(MainPanel.mainFrame, "读取1.properties失败："+e.getMessage());
				e.printStackTrace();
				System.exit(0);
			}
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
		JTextField urlText = new JTextField(p.getProperty("url"),40);
		
		JLabel  namelabel= new JLabel("用户名: ", JLabel.RIGHT);
		JTextField userText = new JTextField(p.getProperty("user"),12);
		
		JLabel  passwordLabel = new JLabel("密码: ", JLabel.CENTER);
		JTextField passwordText = new JTextField(p.getProperty("password"),12);      

		JButton loginButton = new JButton("连接数据库");
		loginButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {     
	            //测试数据库连接
	        	 dbc.dsprop = new Properties();
	        	 dbc.dsprop.setProperty("user", userText.getText());
	        	 dbc.dsprop.setProperty("password", passwordText.getText());
	        	 dbc.dsprop.setProperty("url", urlText.getText());
	        	 dbc.dsprop.setProperty("forname", driverText.getText());
	            try {
	            	dbc.DBType = dsText.getText();
	            	Connection c = dbc.openDMConn();
	            	if(c==null) {
	            		return;
	            	}
					//JOptionPane.showMessageDialog(MainPanel.mainFrame, "连接成功！");    
					//表格配置组件
			        //GridComponent grid = new GridComponent();
			        //grid.setComp();
					
					//展示模式选择框
	            	schemaSelectComponent.setComp(north,dbc,label);
			        loginButton.setEnabled(false);
					/*
			        String url = this.getClass().getResource("./dsProp/1.properties").getPath();
			        try {
						DataSource.dsprop.store(new FileWriter(url), "1112");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			        */
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(MainPanel.mainFrame, "连接失败："+e1.getMessage());    
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
						JOptionPane.showMessageDialog(MainPanel.mainFrame, "保存成功！"); 
					} catch (IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(MainPanel.mainFrame, "保存失败："+e1.getMessage());    
					}

	        	 }
	         }
		});
		
		
		
	    this.setLayout(new FlowLayout());//FlowLayout是默认布局，它以方向流布局组件。
	    //数据源选择控件
	    this.add(dbSource);
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
					dbc.closeCon();
	            	//先移除组件
					north.removeAll();
					//原连接参数重置
					schemaSelectComponent.dbc=null;
					schemaSelectComponent.selectSchema="";
					
					MainPanel.mainFrame.getContentPane().repaint();
				}
			}
		});
		
	    
	    this.add(dslabel);
	    this.add(dsText);
	    this.add(driverlabel);
	    this.add(driverText);
	    
	    this.add(urllabel);
	    this.add(urlText);
	    this.add(namelabel);
	    this.add(userText);
	    this.add(passwordLabel);       
	    this.add(passwordText);
	    this.add(loginButton);
	    this.add(saveButton);
	    //位置及大小
	    //controlPanel.setBounds(0, 5, 1700, 35);
	    
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
