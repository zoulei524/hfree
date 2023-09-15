package me.zoulei.gencode;

import java.util.HashMap;
import java.util.List;

import me.zoulei.backend.entity.GenEntity;
import me.zoulei.backend.entity.TableMetaDataConfig;
import me.zoulei.frontend.vue.elements.VueAttr;
import me.zoulei.frontend.vue.elements.VueNode;
import me.zoulei.frontend.vue.templete.ElTableColTpl;
import me.zoulei.frontend.vue.templete.ElTableTpl;
import me.zoulei.ui.components.codeEditor.Document;

public class Gencode {
	
	public void gencode(TableMetaDataConfig config) throws Exception {
		ElTableTpl gt = new ElTableTpl();
		VueNode gridTable = gt.getEltable();
		VueNode eltableCONST = gt.getEltableCONST();
		
		List<HashMap<String, String>> list = config.getTableMetaData();
		list.forEach(column->{
			if("显示".equals(column.get("visible"))) {
				VueNode col = ElTableColTpl.getTPL();
				col.setComments(col.getComments()+":"+(column.get("comments2")==null?column.get("comments"):column.get("comments2")));
				//prop="yb001"
			    //label="序号"
				col.addAttr(new VueAttr("prop", column.get("column_name").toLowerCase()))
				   .addAttr(new VueAttr("label", column.get("comments")))
				   .addAttr(new VueAttr("width", column.get("width")))
				   .addAttr(new VueAttr("align", column.get("align")))
				;
				
				eltableCONST.append(col);  
			}
			 
		});
		
		
		
		//System.out.println(gridTable.toString());
		
		
		
		
		GenEntity genEntity = new GenEntity(config);  
        //System.out.println(genEntity.getEntity_content());
        //System.out.println(genEntity.getJson_content());
        
        new Document(gridTable.toString(), genEntity.getJson_content(), genEntity.getEntity_content(), "", "", "", "");
        /*
        genEntity = new GenEntity(new TableMetaDataConfig("a01","select * from code_value"));  
        System.out.println(genEntity.getEntity_content());
        System.out.println(genEntity.getJson_content());
        */
	}

}
