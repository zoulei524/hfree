package me.zoulei;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

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
			 //返回读取指定资源的输入流  /hfree/src/me/zoulei/ui/components/dsProp/1.properties
	        InputStream is=this.getClass().getClassLoader().getResourceAsStream("me/zoulei/ui/components/dsProp/1.properties");   
	        
	        String baseDir = System.getProperty("user.dir")+"/dsProp";
	        File f = new File(baseDir);
	        if(f.exists()&&f.isDirectory()) {
	        	return;
	        }
	        f.mkdir();
	        
	        File p1 = new File(baseDir+"/1.properties");
	        this.copyInputStreamToFile(is, p1);
	        
	        is=this.getClass().getClassLoader().getResourceAsStream("me/zoulei/ui/components/dsProp/2.properties");  
	        File p2 = new File(baseDir+"/2.properties");
	        this.copyInputStreamToFile(is, p2);
	        
			
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
