package me.zoulei.backend.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Data;
import me.zoulei.backend.jdbc.datasource.DataSource;
import me.zoulei.backend.jdbc.utils.CommQuery;

/**
 * 2023年9月8日15:19:22 zoulei
 * List<HashMap<String, String>> tableMetaData
 * 获取数据库表的各种信息 字段名column_name 字段备注comments 字段类型data_type 字段长度data_length 主键p
 */
@Data
public class TableMetaDataConfig {
	List<HashMap<String, String>> tableMetaData;
	private String tablename;
	/**
	 * 目前就2中情况，一种传tablename，生成实体类，一种传tablename和查询的sql，生成dto
	 */
	private String type;
	private String sql = "select t.column_name,nvl(c.comments,t.column_name) comments,data_type,to_char(data_length) data_length ,nvl(pk.p,0) p  "
			+ " from ALL_tab_cols t,ALL_col_comments c, "
			+ " (select 1 p,cu.column_name from ALL_cons_columns cu, ALL_constraints au where cu.constraint_name = au.constraint_name and au.constraint_type = 'P' and au.table_name=cu.table_name and au.table_name = upper('%s')) pk"
			+ " where t.TABLE_NAME=c.table_name and t.COLUMN_NAME=c.column_name "
			+ " and pk.column_name(+) =c.column_name  and t.owner=upper('%s')"
			+ " and t.TABLE_NAME=upper('%s')   ORDER BY t.COLUMN_ID";//and t.data_type in('VARCHAR2')
	
	/**
	 * 2023年9月8日18:04:28 zoulei
	 * List<HashMap<String, String>> tableMetaData
	 * 传入表名tablename 和 sql
	 * 获取数据库表的各种信息 字段名column_name 字段备注comments(无) 字段类型data_type 字段长度data_length 主键p（无）
	 */
	public TableMetaDataConfig(String tablename, String sql) throws Exception{
		this.tablename = tablename;
		this.sql = "select * from("+sql+") a where 1=2";
		this.type = "sql";
		this.initMetaData();
	}
	
	/**
	 * 2023年9月8日16:37:39 zoulei
	 * List<HashMap<String, String>> tableMetaData
	 * 传入表名tablename  获取数据库表的各种信息 字段名column_name 字段备注comments 字段类型data_type 字段长度data_length 主键p
	 */
	public TableMetaDataConfig(String tablename,String owner,String e) throws Exception{
		this.tablename = tablename;
		this.sql = String.format(this.sql, tablename,owner,tablename);
		this.type = "table";
		this.initMetaData();
	}
	
	public void initMetaData() throws Exception {
		CommQuery cq = new CommQuery();
		List<HashMap<String, String>> list = cq.getListBySQL2(this.sql);
		if("sql".equals(this.type)) {
			List<HashMap<String, String>> sqllist = new ArrayList<HashMap<String,String>>();
			//创建连接  
	        Connection con = null;  
	        PreparedStatement pStemt = null;  
	        try {  
	            con = DataSource.openDMConn();
	            pStemt = con.prepareStatement(this.sql);  
	            ResultSetMetaData rsmd = pStemt.getMetaData();  
	            int size = rsmd.getColumnCount();   //统计列  
	            HashMap<String, String> c;
	            for (int i = 0; i < size; i++) {  
	            	c = new HashMap<String, String>();
	                c.put("column_name", rsmd.getColumnName(i + 1));
	                c.put("data_type", rsmd.getColumnTypeName(i + 1));
	                c.put("data_length", rsmd.getColumnDisplaySize(i + 1)+"");
	                sqllist.add(c);  
	            }  
	            this.tableMetaData = sqllist;
	            pStemt.close();
	        } catch (SQLException e) {  
	            e.printStackTrace();  
	        } 
		}else {
			this.tableMetaData = list;
		}
		
		
	}
	
}
