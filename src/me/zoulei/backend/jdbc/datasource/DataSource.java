package me.zoulei.backend.jdbc.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

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
	
	
	public static Connection getDMConn(){
		Properties info = new Properties();
		//设置dm信息
		String forname = "dm.jdbc.driver.DmDriver";
		String url = "jdbc:dm://10.32.184.100:5236?useServerPrepStmts=true";
		String user = "HY_GBGL_ZZGB";
		String password = "HY_GBGL_ZZGB";
		Connection con = null;
		try {
			Class.forName(forname).newInstance();
			con = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
}
