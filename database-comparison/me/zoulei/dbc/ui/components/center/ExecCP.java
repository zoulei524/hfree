package me.zoulei.dbc.ui.components.center;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import me.zoulei.backend.jdbc.utils.CommQuery;
import me.zoulei.dbc.ui.components.MainPanel;
import me.zoulei.dbc.ui.components.north.SchemaSelectComponent;
import me.zoulei.dbc.ui.components.orthers.Progress;
import me.zoulei.dbc.ui.components.orthers.Progress.SimulatorActivity;

/**
 * 2023年11月15日16:11:16 zoulei
 * 执行数据库表结构比对
 */
public class ExecCP extends Thread{
	
	private SchemaSelectComponent schema1;
	private SchemaSelectComponent schema2;
	private ResultsLog resultsLog;
	//进度条
	Progress progress = new Progress();
	
	public ExecCP(SchemaSelectComponent schemaSelectComponent1, SchemaSelectComponent schemaSelectComponent2, ResultsLog resultsLog) {
		this.resultsLog = resultsLog;
		this.schema1 = schemaSelectComponent1;
		this.schema2 = schemaSelectComponent2;
		progress.simulaterActivity.setTaskThread(this);
	}

	/**
	 * 2023年11月22日15:47:04
	 * mysql
	 * 1、先比对表差异
	 * 2、对于两个库都有的表再比对字段差异
	 * @param schemaSelectComponent1 第一个库 对象中含有数据库连接connect
	 * @param schemaSelectComponent2 第二个库 对象中含有数据库连接connect
	 * @param resultsLog 
	 */
	public void runExecCP() {
		//进度条进度
		SimulatorActivity activity = progress.simulaterActivity;
		//1、清空日志
		new Thread(()->{
			resultsLog.textAreaLog.setText("/*\n同表同字段下，备注、类型、长度、主键差异日志：\n*/\n");
			resultsLog.textAreaDDL1.setText("/*\n库1中多的表和字段：\n*/\n");
			resultsLog.textAreaDDL2.setText("/*\n库2中多的表和字段：\n*/\n");
		}).start();
		
		if("".equals(schema1.selectSchema)||"".equals(schema2.selectSchema)) {
			activity.setStatus(0, "库1或库2数据库未选择!");
			return;
		}
		String columnSQL = "SELECT b.table_name,b.column_name, b.column_comment, b.data_type,\r\n"
				+ "	CASE WHEN b.CHARACTER_MAXIMUM_LENGTH != ''  AND b.CHARACTER_MAXIMUM_LENGTH IS NOT NULL THEN b.CHARACTER_MAXIMUM_LENGTH  \r\n"
				+ "	     WHEN b.NUMERIC_PRECISION != ''  AND b.NUMERIC_PRECISION IS NOT NULL THEN concat( b.NUMERIC_PRECISION, ',', b.NUMERIC_SCALE ) ELSE \r\n"
				+ "			 b. DATETIME_PRECISION  END AS data_length,\r\n"
				+ "			CASE WHEN b.COLUMN_KEY = 'PRI' THEN 1 ELSE 0 END p   "
				+ ","
				+ "concat(\"ALTER TABLE \",\"`\",b.TABLE_NAME,\"`\",\" add \",\" \",\"`\",b.COLUMN_NAME,\"`\",\" \",b.COLUMN_TYPE , \" \",\r\n"
				+ "if(b.CHARACTER_SET_NAME is null,\" \",concat(\" character set \",b.CHARACTER_SET_NAME,\" \")),\r\n"
				+ "if(b.COLLATION_NAME is null,\" \",concat(\" COLLATE \",\"'\",b.COLLATION_NAME,\"' \")),\r\n"
				+ "if(b.IS_NULLABLE='NO',\" NOT NULL \",\" null \"), \r\n"
				+ "if(b.COLUMN_DEFAULT is null , if(b.EXTRA='auto_increment' or b.IS_NULLABLE='NO',\" \",\" DEFAULT null \") ,concat(\" DEFAULT \",if(b.DATA_TYPE='timestamp' or b.DATA_TYPE='bit' ,b.COLUMN_DEFAULT,concat(\"'\",b.COLUMN_DEFAULT,\"'\")))),\r\n"
				+ "if(b.EXTRA is null ,\" \",concat(\" \",b.EXTRA,\" \"  )), "
				+ "\" COMMENT \",\" \",\"'\",b.COLUMN_COMMENT,\"'\",\";\") add_column"
				+ " "
				+ " FROM information_schema.COLUMNS b"
				+ " WHERE   b.table_schema = upper('%s') "
				+ " and b.TABLE_NAME in(select TABLE_NAME from information_schema.tables where TABLE_TYPE='BASE TABLE' and table_schema = upper('%s') )"
				+ "		ORDER BY b.table_name,b.ORDINAL_POSITION";
		
		String tableSQL = "SELECT TABLE_NAME,TABLE_COMMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA=upper('%s') and TABLE_TYPE='BASE TABLE'  order by table_name ";
		
		
		activity.setStatus(10, "正在查询库1数据...");
		try {
			String tableSQL1 = String.format(tableSQL, schema1.selectSchema);
			String columnSQL1 = String.format(columnSQL, schema1.selectSchema, schema1.selectSchema);
			//第一个数据库信息
			CommQuery cq1 = new CommQuery(schema1.dbc);
			List<HashMap<String, String>> tablelist1 = cq1.getListBySQL2(tableSQL1);
			//表名处理
			HashMap<String, String> tableMap1 = new LinkedHashMap<String, String>();
			for(HashMap<String, String> t : tablelist1) {
				tableMap1.put(t.get("table_name"), t.get("table_comment"));
			}
			
			List<HashMap<String, String>> columnlist1 = cq1.getListBySQL2(columnSQL1);
			//字段数据结构处理
			Map<String, HashMap<String,HashMap<String, String>>> ddlInfo1 = new LinkedHashMap<String, HashMap<String,HashMap<String,String>>>(); 
			for(HashMap<String, String> t : columnlist1) {
				String table = t.get("table_name");
				String col = t.get("column_name");
				HashMap<String,HashMap<String, String>> c = ddlInfo1.get(table);
				if(c==null) {
					c = new LinkedHashMap<String,HashMap<String,String>>();
					ddlInfo1.put(table, c);
				}
				c.put(col, t);
			}
			//System.out.println(JSON.toJSONString(ddlInfo1, true));
			
			//第二个数据库信息
			activity.setStatus(30, "正在查询库2数据...");
			String tableSQL2 = String.format(tableSQL, schema2.selectSchema);
			String columnSQL2 = String.format(columnSQL, schema2.selectSchema, schema2.selectSchema);
			CommQuery cq2 = new CommQuery(schema2.dbc);
			List<HashMap<String, String>> tablelist2 = cq2.getListBySQL2(tableSQL2);
			List<HashMap<String, String>> columnlist2 = cq2.getListBySQL2(columnSQL2);
			//表名处理
			HashMap<String, String> tableMap2 = new LinkedHashMap<String, String>();
			for(HashMap<String, String> t : tablelist2) {
				tableMap2.put(t.get("table_name"), t.get("table_comment"));
			}
			//字段数据结构处理
			Map<String, HashMap<String,HashMap<String, String>>> ddlInfo2 = new LinkedHashMap<String, HashMap<String,HashMap<String,String>>>(); 
			for(HashMap<String, String> t : columnlist2) {
				String table = t.get("table_name");
				String col = t.get("column_name");
				HashMap<String,HashMap<String, String>> c = ddlInfo2.get(table);
				if(c==null) {
					c = new LinkedHashMap<String,HashMap<String,String>>();
					ddlInfo2.put(table, c);
				}
				c.put(col, t);
			}
			//System.out.println(JSON.toJSONString(ddlInfo2, true));
			activity.setStatus(50, "正在执行比对...");
			JSONObject tableResults = new JsonCompareUtils().compare2Json(JSON.toJSONString(tableMap1), JSON.toJSONString(tableMap2));
			//System.out.println(JSON.toJSONString(tableResults,true));
			JSONObject colunmResults = new JsonCompareUtils().compare2Json(JSON.toJSONString(ddlInfo1), JSON.toJSONString(ddlInfo2));
			
			
			//1、清空日志
			this.log = new StringBuilder();
			this.tableDDL1 = new StringBuilder();
			this.tableDDL2 = new StringBuilder();
			this.columnDDL1 = new StringBuilder();
			this.columnDDL2 = new StringBuilder();
			//2、分析结果
			activity.setStatus(90, "正在分析结果...");
			analyzeTableResults(tableResults,colunmResults);
			analyzeColumnResults(colunmResults);
			//3、设置结果数据
			resultsLog.textAreaLog.append(log.length()==0?"2个库表结构无同表同字段上的差异！":log.toString());
			resultsLog.textAreaDDL1.append((tableDDL1.length()==0&&columnDDL1.length()==0)?"2个库表表结构和字段一致！":(tableDDL1.toString()+columnDDL1));
			resultsLog.textAreaDDL2.append((tableDDL2.length()==0&&columnDDL2.length()==0)?"2个库表表结构和字段一致！":(tableDDL2.toString()+columnDDL2));
			activity.setStatus(100, "分析完成！");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(MainPanel.mainFrame, "比对失败："+e.getMessage());    
		}

	}

	

	/**
	 * 分析成差异日志
	 * @param tableResults
	 * @param columnResults 
	 * @throws Exception 
	 */
	private void analyzeTableResults(JSONObject tableResults, JSONObject colunmResults) throws Exception {
		CommQuery cq1 = new CommQuery(this.schema1.dbc);
		CommQuery cq2 = new CommQuery(this.schema2.dbc);
		
		//表差异
		for(String tablename : tableResults.keySet()) {
			JSONObject resultInfo = tableResults.getJSONObject(tablename);
//			"a01":{
//				"库1":"[0004]人员基本信息表",
//				"库2":"",
//				"info":2
//			},
			String info = resultInfo.getString("info");
			if(StringUtils.isEmpty(info)) {
				this.printLog("表"+tablename+"备注:"+ resultInfo.toJSONString());
			}else {
				//其中一个库不存在表， 在字段结果中删除该表，不需要比较字段了
				colunmResults.remove(tablename);
				
				if("1".equals(info)) {//库1不存在表
					List<HashMap<String, String>> tableddl = cq2.getListBySQL2("SHOW CREATE TABLE " + this.schema2.selectSchema +"."+ tablename);
					this.printTableDDL2("/* 库1不存在的表“"+resultInfo.getString("库2")+"”："+tablename+" */\n"+tableddl.get(0).get("create table")+";\n");
				}else if("2".equals(info)) {//库2不存在表
					List<HashMap<String, String>> tableddl = cq1.getListBySQL2("SHOW CREATE TABLE " + this.schema1.selectSchema +"."+ tablename);
					this.printTableDDL1("/* 库2不存在的表“"+resultInfo.getString("库1")+"”："+tablename+" */\n"+tableddl.get(0).get("create table")+";\n");
				}
			}
		}
		
	}
	
	/**
	 * 分析成差异日志
	 * @param columnResults
	 * @throws Exception 
	 */
	private void analyzeColumnResults(JSONObject colunmResults) {
		

		//System.out.println(JSON.toJSONString(colunmResults,true));
		
		CommQuery cq1 = new CommQuery(this.schema1.dbc);
		CommQuery cq2 = new CommQuery(this.schema2.dbc);
		
		//表差异
		for(String tablename : colunmResults.keySet()) {
			JSONObject columnResultInfo = colunmResults.getJSONObject(tablename);
			
			for(String colname : columnResultInfo.keySet()) {
				//比对信息
				JSONObject resultInfo = columnResultInfo.getJSONObject(colname);
				
				JSONObject column_name_info = resultInfo.getJSONObject("column_name");
				if(column_name_info==null) {//字段相同
					StringBuilder sb = new StringBuilder("表"+tablename+"."+colname+"差异:");
					resultInfo.forEach((k,v)->{
						if("add_column".equals(k)) {
							return;
						}
						sb.append( k+(new JSONObject((LinkedHashMap)v)).toJSONString()+";" );
					});
					if(("表"+tablename+"."+colname+"差异:").length()<sb.length()) {
						this.printLog(sb.toString());
					}
					
				}else {//某个库缺字段
					String info = column_name_info.getString("info");
					if("1".equals(info)) {//库1不存在字段
//						String column_comment = resultInfo.getJSONObject("column_comment").getString("库2");
//						String data_type = resultInfo.getJSONObject("data_type").getString("库2");
//						String data_length = resultInfo.getJSONObject("data_length").getString("库2");
//						String p = resultInfo.getJSONObject("p").getString("库2");
//						ALTER TABLE `hy_gbgl_zzgb`.`kh_code` 
//						ADD COLUMN `ACC` varchar(255) COMMENT '123';
//						String ddl = "ALTER TABLE "+this.schema2.selectSchema+"."+tablename
//								+ "ADD COLUMN " + colname +" "+ data_type+"("+data_length+") COMMENT '"+column_comment+"';";
						String ddl = resultInfo.getJSONObject("add_column").getString("库2");
						this.printTableDDL2(ddl);
					}else if("2".equals(info)) {//库2不存在字段
//						String column_comment = resultInfo.getJSONObject("column_comment").getString("库1");
//						String data_type = resultInfo.getJSONObject("data_type").getString("库1");
//						String data_length = resultInfo.getJSONObject("data_length").getString("库1");
//						String p = resultInfo.getJSONObject("p").getString("库1");
//						ALTER TABLE `hy_gbgl_zzgb`.`kh_code` 
//						ADD COLUMN `ACC` varchar(255) COMMENT '123';
//						String ddl = "ALTER TABLE "+this.schema1.selectSchema+"."+tablename
//								+ "ADD COLUMN " + colname +" "+ data_type+"("+data_length+") COMMENT '"+column_comment+"';";
						String ddl = resultInfo.getJSONObject("add_column").getString("库1");
						this.printTableDDL1(ddl);
					}
				}
				
				
//			"A28216":{
//				"table_name":{
//					"库1":"a282",
//					"库2":"不存在 a282.A28216.table_name",
//					"info":2
//				},
//				"column_name":{
//					"库1":"A28216",
//					"库2":"不存在 a282.A28216.column_name",
//					"info":2
//				},
//				"column_comment":{
//					"库1":"评审未通过处理结果",
//					"库2":"不存在 a282.A28216.column_comment",
//					"info":2
//				},
//				"data_type":{
//					"库1":"varchar",
//					"库2":"不存在 a282.A28216.data_type",
//					"info":2
//				},
//				"data_length":{
//					"库1":"400",
//					"库2":"不存在 a282.A28216.data_length",
//					"info":2
//				},
//				"p":{
//					"库1":"0",
//					"库2":"不存在 a282.A28216.p",
//					"info":2
//				}
//			},
				
			}

			
		}
		
	}
	
	StringBuilder log = new StringBuilder();
	StringBuilder tableDDL1 = new StringBuilder();
	StringBuilder tableDDL2 = new StringBuilder();
	StringBuilder columnDDL1 = new StringBuilder();
	StringBuilder columnDDL2 = new StringBuilder();
	public void printLog(String s){
		//System.out.println(s);
		//resultsLog.textAreaLog.append(s);
		log.append(s+"\n");
	}
	
	public void printTableDDL1(String s){
		//System.out.println(s);
		//resultsLog.textAreaDDL1.append(s);
		tableDDL1.append(s+"\n");
	}
	public void printTableDDL2(String s){
		//System.out.println(s);
		//resultsLog.textAreaDDL2.append(s);
		tableDDL2.append(s+"\n");
	}
	
	public void printColumnDDL1(String s){
		//System.out.println(s);
		//resultsLog.textAreaDDL1.append(s);
		columnDDL1.append(s+"\n");
	}
	public void printColumnDDL2(String s){
		//System.out.println(s);
		//resultsLog.textAreaDDL2.append(s);
		columnDDL2.append(s+"\n");

	}



	@Override
	public void run() {
		runExecCP();
	}

}









