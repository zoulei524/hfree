package me.zoulei.gencode;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import me.zoulei.backend.templete.dependency.GenDep;
import me.zoulei.backend.templete.grid.TableMetaDataConfig;
import me.zoulei.backend.templete.grid.controller.GenCTL;
import me.zoulei.backend.templete.grid.dao.GenDao;
import me.zoulei.backend.templete.grid.entity.GenEntity;
import me.zoulei.backend.templete.grid.javascript.GenJS;
import me.zoulei.backend.templete.grid.service.GenService;
import me.zoulei.backend.templete.grid.xml.GenXml;
import me.zoulei.frontend.templete.grid.GenCSS;
import me.zoulei.frontend.templete.grid.GenTable;
import me.zoulei.ui.components.codeEditor.CodeDocument;

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
        
		
		Map<String,String[]> codemap = new LinkedHashMap<String, String[]>();
		//String vue, String js, String entity, String controller, String service, String dao, String xml,String css
		codemap.put("   VUE   ", new String[] {table.getCode(), SyntaxConstants.SYNTAX_STYLE_HTML});
		codemap.put("   CSS   ", new String[] {css.getCode(), SyntaxConstants.SYNTAX_STYLE_LESS});
		codemap.put("     JS     ", new String[] {js.getCode(), SyntaxConstants.SYNTAX_STYLE_TYPESCRIPT});
		codemap.put("    实体类   ", new String[] {genEntity.getEntity_content(), SyntaxConstants.SYNTAX_STYLE_JAVA});
		codemap.put("    Dto   ", new String[] {genEntity.getDto_content(), SyntaxConstants.SYNTAX_STYLE_JAVA});
		codemap.put(" Controller ", new String[] {ctl.getCode(), SyntaxConstants.SYNTAX_STYLE_JAVA});
		codemap.put("  Service   ", new String[] {serv.getCode(), SyntaxConstants.SYNTAX_STYLE_JAVA});
		codemap.put("  ServiceImp   ", new String[] {serv.getCodeimp(), SyntaxConstants.SYNTAX_STYLE_JAVA});
		codemap.put("  ExcelExp   ", new String[] {serv.getCodeexcel(), SyntaxConstants.SYNTAX_STYLE_JAVA});
		codemap.put("    Dao     ", new String[] {dao.getCode(), SyntaxConstants.SYNTAX_STYLE_JAVA});
		codemap.put("    Xml     ", new String[] {xml.getCode(), SyntaxConstants.SYNTAX_STYLE_XML});
		
		//其他工具类
		GenDep dep = new GenDep(config);
		codemap.put("4个工具类用于兼容下拉和弹出框的，加一次就好了", new String[] {dep.getCode(), SyntaxConstants.SYNTAX_STYLE_JAVA});
        new CodeDocument(codemap,config);
        /*
        genEntity = new GenEntity(new TableMetaDataConfig("a01","select * from code_value"));  
        System.out.println(genEntity.getEntity_content());
        System.out.println(genEntity.getJson_content());
        */
	}

}
