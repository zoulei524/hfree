package me.zoulei.gencode;

import me.zoulei.backend.controller.GenCTL;
import me.zoulei.backend.dao.GenDao;
import me.zoulei.backend.entity.GenEntity;
import me.zoulei.backend.entity.TableMetaDataConfig;
import me.zoulei.backend.service.GenService;
import me.zoulei.backend.xml.GenXml;
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
        
        new Document(table.getCode(), genEntity.getJson_content(), genEntity.getEntity_content(),
        		ctl.getCode(), serv.getCode(), dao.getCode(), xml.getCode(),css.getCode());
        /*
        genEntity = new GenEntity(new TableMetaDataConfig("a01","select * from code_value"));  
        System.out.println(genEntity.getEntity_content());
        System.out.println(genEntity.getJson_content());
        */
	}

}
