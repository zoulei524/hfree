package me.zoulei.frontend.templete.grid;

import java.util.HashMap;
import java.util.List;

import lombok.Data;
import me.zoulei.Constants;
import me.zoulei.backend.TableMetaDataConfig;
import me.zoulei.frontend.node.vue.VueAttr;
import me.zoulei.frontend.node.vue.VueNode;

/**
* @author zoulei 
* @date 2023年9月28日 下午3:03:30 
* @description 生成表格vue及css代码
 */
@Data
public class GenTable {
	
	/**输出的vue代码*/
	private String code;
	
	public GenTable(TableMetaDataConfig config) throws Exception {
		//表格模板
		ElTableTpl gt = new ElTableTpl();
		//表格vue
		VueNode gridPage = gt.getPage();
		VueNode el_table = gt.getEl_table();
		
		List<HashMap<String, String>> list = config.getTableMetaData();
		
		//序号
		/*
		 * <el-table-column type="index" label="行号" align="center" fixed width="55" >
		 * </el-table-column>
		 */
		VueNode 序号 = ElTableColTpl.getTPL();
		序号.setAttrNotNewLine(Constants.ATTR_NEW_LINE);
		序号.addAttr(new VueAttr("type", "index"))
		   .addAttr(new VueAttr("label", "序号"))
		   .addAttr(new VueAttr("width", "55"))
		   .addAttr(new VueAttr("align", "center"));
		;
		
		el_table.append(序号);  
		list.forEach(column->{
			if("显示".equals(column.get("visible"))) {
				VueNode col = ElTableColTpl.getTPL();
				col.setComments(col.getComments()+":"+(column.get("comments2")==null?column.get("comments"):column.get("comments2")));
				col.setAttrNotNewLine(Constants.ATTR_NEW_LINE);
				//prop="yb001"
			    //label="序号"
				col.addAttr(new VueAttr("prop", column.get("column_name").toLowerCase()))
				   .addAttr(new VueAttr("label", column.get("comments")))
				   .addAttr(new VueAttr("width", column.get("width")))
				   .addAttr(new VueAttr("align", column.get("align")))
				;
				
				el_table.append(col);  
			}
			 
		});
		
		//title
		String tablecomment = config.getTablecomment();
		String tablename = config.getTablename();
		//第一个P标签里放标题文字
		VueNode title = (VueNode) gt.getTitle();
		((VueNode) title.getNode(0)).setText(tablecomment+"("+tablename+")");
		
		this.code = gridPage.toString();
	}
	
	public static void main(String[] args) throws Exception {
		GenTable genCTL = new GenTable(null);
		System.out.println(genCTL.code);
	}
	
	
}
