package me.zoulei.backend.templete.grid.xml;

import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import lombok.Data;
import me.zoulei.Constants;
import me.zoulei.backend.TableMetaDataConfig;

/**
* @author zoulei 
* @date 2023年9月18日14:38:15
* @description 输出xml相关代码。
 */
@Data
public class GenXml {
	
	/**输出的java代码*/
	private String code;
	
	public GenXml(TableMetaDataConfig config) throws Exception {
		
		InputStream is = this.getClass().getResourceAsStream("xml.tpl");
		
		String tpl = IOUtils.toString(is,"utf-8");
		
		//配置项
		HashMap<String, String> params = new HashMap<String, String>();
		String tablename = config.getTablename().toLowerCase();
		String author = Constants.AUTHOR;
		String entity = this.initcap(tablename);
		String tablecomment = config.getTablecomment();
		
		String otherParams = "";
		params.put("tablename", tablename);
		params.put("author", author);
		//首字母大写的表名
		params.put("entity", entity);
		params.put("tablecomment", tablecomment);
		params.put("otherParams", otherParams);
		//查询sql
		String sqlFields = config.getSqlFields();
		String listDataSQL = "select " + sqlFields + " from " + tablename + " t";
		params.put("listDataSQL", listDataSQL);
		//时间
		params.put("time", new SimpleDateFormat("yyyy年M月d日 hh:mm:ss").format(new Date()));
		
		Template template = new Template("ctl", tpl, new Configuration(new Version("2.3.30")) );
		StringWriter result = new StringWriter();
	    template.process(params, result);
		this.code = result.toString();
	}
	
	public static void main(String[] args) throws Exception {
		GenXml genCTL = new GenXml(null);
		System.out.println(genCTL.code);
	}
	
	/** 
     * 功能：将输入字符串的首字母改成大写 
     * @param str 
     * @return 
     */  
    private String initcap(String str) {  
          
        char[] ch = str.toCharArray();  
        if(ch[0] >= 'a' && ch[0] <= 'z'){  
            ch[0] = (char)(ch[0] - 32);  
        }  
        return new String(ch);  
    }  
}