package me.zoulei.dbc.ui.components.center;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;
import org.fife.ui.rtextarea.RTextScrollPane;

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
 * 单独线程执行。 否则进度条只能在执行比对线程结束，才能显示。
 */
public class ExecCP extends Thread{
	
	private SchemaSelectComponent schema1;
	private SchemaSelectComponent schema2;
	private ResultsLogUI resultsLog;
	//进度条
	Progress progress = new Progress();
	
	public ExecCP(SchemaSelectComponent schemaSelectComponent1, SchemaSelectComponent schemaSelectComponent2, ResultsLogUI resultsLog) {
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
			if("".equals(schema1.selectSchema)||"".equals(schema2.selectSchema)) {
				return;
			}
			while(true) {
				//3、设置结果数据
				String log = bklog.poll();
				if(log!=null) {
					resultsLog.textAreaLog.append(log);
				}
				
				String tddl1 = bktableDDL1.poll();
				if(tddl1!=null) {
					resultsLog.textAreaDDL1.append(tddl1);
				}
				String tcolumnDDL1 = bkcolumnDDL1.poll();
				if(tcolumnDDL1!=null) {
					resultsLog.textAreaDDL1.append(tcolumnDDL1);
				}

				String tddl2 = bktableDDL2.poll();
				if(tddl2!=null) {
					resultsLog.textAreaDDL2.append(tddl2);
				}
				String tcolumnDDL2 = bkcolumnDDL2.poll();
				if(tcolumnDDL2!=null) {
					resultsLog.textAreaDDL2.append(tcolumnDDL2);
				}
				//滚动条
				((RTextScrollPane)(resultsLog.textAreaLog.getParent().getParent())).getVerticalScrollBar().setValue(Integer.MAX_VALUE);
				((RTextScrollPane)(resultsLog.textAreaDDL1.getParent().getParent())).getVerticalScrollBar().setValue(Integer.MAX_VALUE);
				((RTextScrollPane)(resultsLog.textAreaDDL2.getParent().getParent())).getVerticalScrollBar().setValue(Integer.MAX_VALUE);
				//状态为100 或者值情况返回
				if(activity.getCurrent()==100&&log==null&&tddl1==null&&tcolumnDDL1==null
						&&tddl2==null&&tcolumnDDL2==null) {
					resultsLog.textAreaLog.append("/*\n分析结束！\n*/\n");
					resultsLog.textAreaDDL1.append("/*\n分析结束！\n*/\n");
					resultsLog.textAreaDDL2.append("/*\n分析结束！\n*/\n");
					
					return;
				}
			}
		}).start();
		
		if("".equals(schema1.selectSchema)||"".equals(schema2.selectSchema)) {
			activity.setStatus(0, "库1或库2数据库未选择!");
			return;
		}
		
		try {
			activity.setStatus(10, "正在查询库1数据...");
			String tableSQL1 = String.format(SQLAdapterUtil.getTableSQL(schema1.dbc), schema1.selectSchema);
			String columnSQL1 = String.format(SQLAdapterUtil.getColumnSQL(schema1.dbc), schema1.selectSchema, schema1.selectSchema);
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
				//oracle 的column_default long sql里获取不到
				if(schema1.dbc.DBType.equals("oracle")||schema1.dbc.DBType.equals("达梦")) {
					String column_default = t.get("column_default");
					String add_column = t.get("add_column");
					if(column_default!=null&&!"".equals(column_default))
					t.put("add_column", add_column.replace("SdefaultS", column_default));
				}
				
				
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
			String tableSQL2 = String.format(SQLAdapterUtil.getTableSQL(schema2.dbc), schema2.selectSchema);
			String columnSQL2 = String.format(SQLAdapterUtil.getColumnSQL(schema2.dbc), schema2.selectSchema, schema2.selectSchema);
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
				//oracle 的column_default long sql里获取不到
				if(schema2.dbc.DBType.equals("oracle")||schema2.dbc.DBType.equals("达梦")) {
					String column_default = t.get("column_default");
					String add_column = t.get("add_column");
					if(column_default!=null&&!"".equals(column_default))
					t.put("add_column", add_column.replace("SdefaultS", column_default));
				}
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
			//3、设置结果数据 改成线程2023年11月29日18:35:06
			//resultsLog.textAreaLog.append(log.length()==0?"2个库表结构无同表同字段上的差异！":log.toString());
			//resultsLog.textAreaDDL1.append((tableDDL1.length()==0&&columnDDL1.length()==0)?"2个库表表结构和字段一致！":(tableDDL1.toString()+columnDDL1));
			//resultsLog.textAreaDDL2.append((tableDDL2.length()==0&&columnDDL2.length()==0)?"2个库表表结构和字段一致！":(tableDDL2.toString()+columnDDL2));
			activity.setStatus(100, "分析完成！");
			//滚动条
			((RTextScrollPane)(resultsLog.textAreaLog.getParent().getParent())).getVerticalScrollBar().setValue(Integer.MAX_VALUE);
			((RTextScrollPane)(resultsLog.textAreaDDL1.getParent().getParent())).getVerticalScrollBar().setValue(Integer.MAX_VALUE);
			((RTextScrollPane)(resultsLog.textAreaDDL2.getParent().getParent())).getVerticalScrollBar().setValue(Integer.MAX_VALUE);
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
					if(schema2.dbc.DBType.equals("mysql")) {
						List<HashMap<String, String>> tableddl = cq2.getListBySQL2("SHOW CREATE TABLE " + this.schema2.selectSchema +"."+ tablename);
						this.printTableDDL2("/* 库1不存在的表“"+resultInfo.getString("库2")+"”："+tablename+" */\n"+tableddl.get(0).get("create table")+";\n");
					}else {
						List<HashMap<String, String>> tableddl = cq2.getListBySQL2(
								"SELECT DBMS_METADATA.get_ddl(\r\n"
								+ "     'TABLE', \r\n"
								+ "     '"+(tablename.toUpperCase())+"',\r\n"
								+ "     '"+(this.schema2.selectSchema.toUpperCase())+"' \r\n"
								+ ") ddl FROM DUAL");
						this.printTableDDL2("/* 库1不存在的表“"+resultInfo.getString("库2")+"”："+tablename+" */\n"+tableddl.get(0).get("ddl"));
						//达梦自己加分号了
						if(schema1.dbc.DBType.equals("oracle")) {
							this.printTableDDL2(";\n");
						}else {
							this.printTableDDL2("\n");
						}
					}
					
				}else if("2".equals(info)) {//库2不存在表
					if(schema1.dbc.DBType.equals("mysql")) {
						List<HashMap<String, String>> tableddl = cq1.getListBySQL2("SHOW CREATE TABLE " + this.schema1.selectSchema +"."+ tablename);
						this.printTableDDL1("/* 库2不存在的表“"+resultInfo.getString("库1")+"”："+tablename+" */\n"+tableddl.get(0).get("create table")+";\n");
					}else {
						List<HashMap<String, String>> tableddl = cq1.getListBySQL2(
								"SELECT DBMS_METADATA.get_ddl(\r\n"
								+ "    'TABLE', \r\n"
								+ "    '"+(tablename.toUpperCase())+"',\r\n"
								+ "    '"+(this.schema1.selectSchema.toUpperCase())+"' \r\n"
								+ ") ddl FROM DUAL");
						this.printTableDDL1("/* 库2不存在的表“"+resultInfo.getString("库2")+"”："+tablename+" */\n"+tableddl.get(0).get("ddl"));
						//达梦自己加分号了
						if(schema1.dbc.DBType.equals("oracle")) {
							this.printTableDDL1(";\n");
						}else {
							this.printTableDDL1("\n");
						}
						
					}
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
					StringBuilder sb = new StringBuilder("字段"+tablename+"."+colname+"差异:");
					resultInfo.forEach((k,v)->{
						if("add_column".equals(k)) {
							return;
						}
						sb.append( k+(new JSONObject((LinkedHashMap)v)).toJSONString()+";" );
					});
					if(("字段"+tablename+"."+colname+"差异:").length()<sb.length()) {
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
						this.printColumnDDL2(ddl);
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
						this.printColumnDDL1(ddl);
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
	//存日志的地方
	StringBuilder log = new StringBuilder();
	StringBuilder tableDDL1 = new StringBuilder();
	StringBuilder tableDDL2 = new StringBuilder();
	StringBuilder columnDDL1 = new StringBuilder();
	StringBuilder columnDDL2 = new StringBuilder();
	//放日志的集合
	BlockingQueue<String> bklog = new LinkedBlockingQueue<String>();
	BlockingQueue<String> bktableDDL1 = new LinkedBlockingQueue<String>();
	BlockingQueue<String> bktableDDL2 = new LinkedBlockingQueue<String>();
	BlockingQueue<String> bkcolumnDDL1 = new LinkedBlockingQueue<String>();
	BlockingQueue<String> bkcolumnDDL2 = new LinkedBlockingQueue<String>();
	public void printLog(String s){
		//System.out.println(s);
		//resultsLog.textAreaLog.append(s);
		log.append(s+"\n");
		if(bklog.isEmpty()) {
			bklog.add(log.toString());
			log.setLength(0);
		}
	}
	
	public void printTableDDL1(String s){
		//System.out.println(s);
		//resultsLog.textAreaDDL1.append(s);
		tableDDL1.append(s+"\n");
		if(bktableDDL1.isEmpty()) {
			bktableDDL1.add(tableDDL1.toString());
			tableDDL1.setLength(0);
		}
	}
	public void printTableDDL2(String s){
		//System.out.println(s);
		//resultsLog.textAreaDDL2.append(s);
		tableDDL2.append(s+"\n");
		if(bktableDDL2.isEmpty()) {
			bktableDDL2.add(tableDDL2.toString());
			tableDDL2.setLength(0);
		}
	}
	
	public void printColumnDDL1(String s){
		//System.out.println(s);
		//resultsLog.textAreaDDL1.append(s);
		columnDDL1.append(s+"\n");
		if(bkcolumnDDL1.isEmpty()) {
			bkcolumnDDL1.add(columnDDL1.toString());
			columnDDL1.setLength(0);
		}
	}
	public void printColumnDDL2(String s){
		//System.out.println(s);
		//resultsLog.textAreaDDL2.append(s);
		columnDDL2.append(s+"\n");
		if(bkcolumnDDL2.isEmpty()) {
			bkcolumnDDL2.add(columnDDL2.toString());
			columnDDL2.setLength(0);
		}
	}



	@Override
	public void run() {
		runExecCP();
	}

}








