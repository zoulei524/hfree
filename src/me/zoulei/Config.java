package me.zoulei;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;

import me.zoulei.frame.SafeProperties;

/**
 * 
* @author zoulei 
* @date 2023年10月23日 下午6:22:11 
* @description 初始化数据库配置，默认生成2个数据库文件，用于保存数据库配置信息。可以手动增加数据库配置。
 */
public class Config {
	Logger logger = Logger.getLogger(Config.class);
	public Config(){
		
		try {
			logger.info(System.getProperty("user.dir"));
			
	        String baseDir = System.getProperty("user.dir")+"/dsProp";
	        File f = new File(baseDir);
	        if(f.exists()&&f.isDirectory()) {
	        	
	        }else {
	        	f.mkdir();
		        //达梦 oracle mysql三种类型
	        	//返回读取指定资源的输入流  /hfree/src/me/zoulei/ui/components/dsProp/1.properties
		        InputStream is=this.getClass().getClassLoader().getResourceAsStream("me/zoulei/ui/components/dsProp/001.properties");   
		        File p1 = new File(baseDir+"/001.properties");
		        this.copyInputStreamToFile(is, p1);
		        is.close();
		        
		        is=this.getClass().getClassLoader().getResourceAsStream("me/zoulei/ui/components/dsProp/002.properties");  
		        File p2 = new File(baseDir+"/002.properties");
		        this.copyInputStreamToFile(is, p2);
		        is.close();
		        
		        is=this.getClass().getClassLoader().getResourceAsStream("me/zoulei/ui/components/dsProp/003.properties");  
		        File p3 = new File(baseDir+"/003.properties");
		        this.copyInputStreamToFile(is, p3);
		        is.close();
	        }
	        
	        
	        //初始化文件
	        String inifilep = System.getProperty("user.dir")+"/hfree.ini";
	        File inifile = new File(inifilep);
	        if(inifile.exists()&&inifile.isFile()) {
	        	//读取配置
	        	SafeProperties p = new SafeProperties();
	        	p.load(inifile);
	        	Constants.DATA_URL = p.getProperty("DATA_URL");
	        	Constants.ATTR_NOT_NEW_LINE = Boolean.valueOf(p.getProperty("ATTR_NOT_NEW_LINE"));
	        	Constants.AUTHOR = p.getProperty("AUTHOR");
	        	Constants.PACKAGE_OUTPATH = p.getProperty("PACKAGE_OUTPATH");
	        	Constants.CODE_VALUE_SQL = p.getProperty("CODE_VALUE_SQL");
	        	Constants.OUTPUT_PACKAGE = p.getProperty("OUTPUT_PACKAGE");
	        }else {
	        	//生成配置
	        	SafeProperties p = new SafeProperties();
	        	p.setProperty("DATA_URL", Constants.DATA_URL,"接口路径 RequestMapping。 已废弃 接口默认拼接表名，生成后自己改");
	        	
	        	p.setProperty("ATTR_NOT_NEW_LINE", Constants.ATTR_NOT_NEW_LINE.toString(),"属性是否换行");
	        	
	        	p.setProperty("AUTHOR", Constants.AUTHOR,"作者");
	        	
	        	p.setProperty("PACKAGE_OUTPATH", Constants.PACKAGE_OUTPATH,"实体类包名。已废弃，放到dao下面了");
	        	
	        	p.setProperty("OUTPUT_PACKAGE", Constants.OUTPUT_PACKAGE,"导出类的包名，填写到controller前一个目录");
	        	
	        	p.setProperty("CODE_VALUE_SQL", Constants.CODE_VALUE_SQL,"获取codetype的sql");
	        	
	        	OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(inifile), "utf-8");
				p.store(outputStreamWriter);
				outputStreamWriter.close();
	        }
	       
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
     * 流输出到文件的方法
     */
    private void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {

        try (FileOutputStream outputStream = new FileOutputStream(file)) {

            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.close();
            inputStream.close();
        }
    }
}
