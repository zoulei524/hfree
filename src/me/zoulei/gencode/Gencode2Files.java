package me.zoulei.gencode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import me.zoulei.backend.templete.grid.TableMetaDataConfig;

/**
 * 将前端和后台的代码生成到文件目录中
 * controller
 * --XXController.java
 * dao
 * --entity.java
 * --XXDao.java
 * --XXDao.sql.xml
 * service
 * --XXServiceImp.java
 * --imp
 * ----XXService.java
 * 
 * 
 * xx.vue
 * js
 * --xx.js
 * @ClassName: Gencode2Files
 * @Desc: TODO
 * @author zoulei
 * @date 2024年1月16日 下午5:43:15
 */
public class Gencode2Files {

	public Gencode2Files(Map<String, String[]> codemap, TableMetaDataConfig config, String name) {
		try {
			String expdir = config.getTablename().toLowerCase();
			//获取跟目录
			String baseDir = System.getProperty("user.dir");
			//生成目录
			String gendir = baseDir + "/" + expdir;
			
			File f_gendir = new File(gendir);
			if(f_gendir.isDirectory()) {
				
				FileUtils.deleteDirectory(f_gendir);
			}
			
			f_gendir.mkdir();
			
			//vue目录
			String vuedir = gendir + "/vue";
			new File(vuedir).mkdir();
			//java目录
			String javadir = gendir + "/java";
			new File(javadir).mkdir();
			
			File vuefile = new File(vuedir + "/" + initlow(name) + ".vue");
			vuefile.createNewFile();
			
			FileWriter vuefw = new FileWriter(vuefile);
			String vue = codemap.get("   VUE   ")[0] + "\n" 
					+ "<script src=\"./js/"+initlow(name)+".js\"></script>\n"
					+ codemap.get("   CSS   ")[0];
			vuefw.write(vue);
			vuefw.close();
			
			new File(vuedir + "/js").mkdir();
			File jsfile = new File(vuedir + "/js/" + initlow(name) + ".js");
			jsfile.createNewFile();
			FileWriter jsfw = new FileWriter(jsfile);
			String js = codemap.get("     JS     ")[0] ;
			jsfw.write(js);
			jsfw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
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
    
    
    /** 
     * 功能：将输入字符串的首字母改成大写 
     * @param str 
     * @return 
     */  
    private String initlow(String str) {  
          
        char[] ch = str.toCharArray();  
        if(ch[0] >= 'A' && ch[0] <= 'Z'){  
            ch[0] = (char)(ch[0] + 32);  
        }  
        return new String(ch);  
    }  
	
}










