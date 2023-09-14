package me.zoulei.backend.jdbc.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javax.swing.JOptionPane;

import me.zoulei.exception.myException;
import me.zoulei.ui.mainApp.MainApp;

/**
 * 2023年9月6日18:20:06 zoulei
 * 数据源
 */
public class DataSource {
	public static Connection getOracleConn(){
		//设置oracle信息
		String forname = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:orcl";
		String user = "dameng";
		String password = "dameng";
		Connection con = null;
		try {
			Class.forName(forname).newInstance();
			con = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	
	
	
	
	
	
	
	static Connection con;
	public static Connection openDMConn(){
		if(con!=null)return con;
		//设置dm信息
		try {
			Class.forName(dsprop.getProperty("forname")).newInstance();
			con = DriverManager.getConnection(dsprop.getProperty("url"),dsprop);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(MainApp.mainFrame, "连接失败："+e.getMessage());    
		}
		return con;
	}
	
	
	public static Properties dsprop = new Properties();
	static {
		dsprop.setProperty("forname", "dm.jdbc.driver.DmDriver");
	}
	/**
	 * 测试连接
	 * @return
	 * @throws myException 
	 * @throws Exception 
	 */
	public static void testDMConn() throws myException{
		
		Connection con = null;
		try {
			Class.forName(dsprop.getProperty("forname")).newInstance();
			con = DriverManager.getConnection(dsprop.getProperty("url"),dsprop);
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new myException(e.getMessage());
		}
		
	}
	
	
	
	
	
}
