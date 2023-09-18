package me.zoulei.gencode;

import java.util.HashMap;
import java.util.List;

import me.zoulei.backend.controller.GenCTL;
import me.zoulei.backend.dao.GenDao;
import me.zoulei.backend.entity.GenEntity;
import me.zoulei.backend.entity.TableMetaDataConfig;
import me.zoulei.backend.service.GenService;
import me.zoulei.backend.xml.GenXml;
import me.zoulei.frontend.vue.elements.VueAttr;
import me.zoulei.frontend.vue.elements.VueNode;
import me.zoulei.frontend.vue.templete.ElTableColTpl;
import me.zoulei.frontend.vue.templete.ElTableTpl;
import me.zoulei.ui.components.codeEditor.Document;

public class Gencode {
	
	public void gencode(TableMetaDataConfig config) throws Exception {
		//表格模板
		ElTableTpl gt = new ElTableTpl();
		//表格vue
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
		
		
		
		//实体类及对应的json代码
		GenEntity genEntity = new GenEntity(config);  
        //System.out.println(genEntity.getEntity_content());
        //System.out.println(genEntity.getJson_content());
		//控制层代码
		GenCTL ctl = new GenCTL(config);
		//服务层
		GenService serv = new GenService(config);
		//dao层
		GenDao dao = new GenDao(config);
		//xml层
		GenXml xml = new GenXml(config);
        
        new Document(gridTable.toString(), genEntity.getJson_content(), genEntity.getEntity_content(),
        		ctl.getCode(), serv.getCode(), dao.getCode(), xml.getCode());
        /*
        genEntity = new GenEntity(new TableMetaDataConfig("a01","select * from code_value"));  
        System.out.println(genEntity.getEntity_content());
        System.out.println(genEntity.getJson_content());
        */
	}

}
