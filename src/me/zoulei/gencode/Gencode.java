package me.zoulei.gencode;

import java.util.HashMap;

import me.zoulei.backend.templete.grid.TableMetaDataConfig;
import me.zoulei.backend.templete.grid.controller.GenCTL;
import me.zoulei.backend.templete.grid.dao.GenDao;
import me.zoulei.backend.templete.grid.entity.GenEntity;
import me.zoulei.backend.templete.grid.javascript.GenJS;
import me.zoulei.backend.templete.grid.service.GenService;
import me.zoulei.backend.templete.grid.xml.GenXml;
import me.zoulei.frontend.templete.grid.GenCSS;
import me.zoulei.frontend.templete.grid.GenTable;
import me.zoulei.ui.components.codeEditor.Document;

/**
 * 
* @author zoulei 
* @date 2023年9月28日 下午3:09:06 
* @description 生成代码
 */
public class Gencode {
	
	public void gencode(TableMetaDataConfig config) throws Exception {

		//表格vue
		GenTable table = new GenTable(config);
		
		//表格css
		GenCSS css = new GenCSS(config);
		
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
		
		//js
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("entityJSON", genEntity.getJson_content());
		GenJS js = new GenJS(config, params);
        
        new Document(table.getCode(), js.getCode(), genEntity.getEntity_content(),
        		ctl.getCode(), serv.getCode(), dao.getCode(), xml.getCode(),css.getCode());
        /*
        genEntity = new GenEntity(new TableMetaDataConfig("a01","select * from code_value"));  
        System.out.println(genEntity.getEntity_content());
        System.out.println(genEntity.getJson_content());
        */
	}

}
