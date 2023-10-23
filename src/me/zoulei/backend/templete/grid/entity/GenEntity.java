package me.zoulei.backend.templete.grid.entity;  
  
  
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import dm.jdbc.util.StringUtil;
import lombok.Data;
import me.zoulei.Constants;
import me.zoulei.backend.templete.grid.TableMetaDataConfig;  

/**
 * 2023年9月8日15:18:59 zoulei
 * 生成实体类
 */
@Data
public class GenEntity {  
    
	public GenEntity(TableMetaDataConfig config){
		this.config = config;
		this.genE();
	}
	
	TableMetaDataConfig config;
	
    private String packageOutPath = Constants.PACKAGE_OUTPATH;//指定实体生成所在包的路径  
    private String authorName = Constants.AUTHOR;//作者名字  
    private boolean f_util = false; // 是否需要导入包java.util.*  
    private boolean f_sql = false; // 是否需要导入包java.sql.*  
    private String entity_content;
    private String json_content;
      
    //数据库连接  
	/* 
	private static final String URL ="jdbc:oracle:thin:@127.0.0.1:1521:ORCL";   
    private static final String NAME = "scrot";  
    private static final String PASS = "tiger";  
    private static final String DRIVER ="oracle.jdbc.driver.OracleDriver";  
	 */
    
    /* 
     * 构造函数 
     */  
    private void genE(){  
    	List<HashMap<String, String>> tableMetaData = this.config.getTableMetaData();
        
            
        int size = tableMetaData.size();   //统计列  
        //column_name 字段备注comments 字段类型data_type 字段长度data_length 主键p
        String colType;
        HashMap<String, String> fconf;
        for (int i = 0; i < size; i++) {  
        	fconf = tableMetaData.get(i);
            colType = fconf.get("data_type");
              
            if(colType.equalsIgnoreCase("date") || colType.equalsIgnoreCase("timestamp")){  
                f_util = true;  
            }  
            if(colType.equalsIgnoreCase("blob") || colType.equalsIgnoreCase("char")){  
                f_sql = true;  
            }  
        }  
          
       parse();  
			/*   
            try {  
                File directory = new File("");  
                //System.out.println("绝对路径："+directory.getAbsolutePath());  
                //System.out.println("相对路径："+directory.getCanonicalPath());  
                String path=this.getClass().getResource("").getPath();  
                  
                System.out.println(path);  
                System.out.println("src/?/"+path.substring(path.lastIndexOf("/com/", path.length())) );  
//              String outputPath = directory.getAbsolutePath()+ "/src/"+path.substring(path.lastIndexOf("/com/", path.length()), path.length()) + initcap(tablename) + ".java";  
                String outputPath = directory.getAbsolutePath()+ "/src/"+this.packageOutPath.replace(".", "/")+"/"+initcap(tablename) + ".java";  
                FileWriter fw = new FileWriter(outputPath);  
                PrintWriter pw = new PrintWriter(fw);  
                pw.println(content);  
                pw.flush();  
                pw.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
              */ 
       
    }  
  
    /** 
     * 功能：生成实体类主体代码 
     * @param colnames 
     * @param colTypes 
     * @param colSizes 
     */  
    private void parse() {  
        StringBuilder sb = new StringBuilder();  
        sb.append("package " + this.packageOutPath + ";\n");  
        sb.append("\n");  
        
        sb.append("import lombok.Data;\n\n"); 
        
        if("table".equals(this.getConfig().getType())) {
        	sb.append("import javax.persistence.Id;\n"); 
            sb.append("import javax.persistence.Table;\n");
            sb.append("import javax.persistence.Entity;\n"); 
        }
        
        sb.append("import java.io.Serializable;\n\n"); 
        
      //判断是否导入工具包  
        if(f_util){  
            sb.append("import java.util.Date;\n");  
        }  
        if(f_sql){  
            sb.append("import java.sql.*;\n");  
        }  
        
        //注释部分  
        sb.append("/**\n");  
        sb.append(" * "+this.config.getTablename()+" 实体类\n");  
        sb.append(" * "+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())  +" "+this.authorName+"\n");  
        sb.append(" */ \n");  
        //实体部分  
        
        
        
        sb.append("@Data\n");  
        if("table".equals(this.getConfig().getType())) {
        	sb.append("@Entity \n");  
            sb.append("@Table(name = \""+(this.config.getTablename().toUpperCase())+"\") \n");  
        }
        sb.append("public class " + initcap(this.config.getTablename()) + " implements Serializable {\n");  
        sb.append("\n\tprivate static final long serialVersionUID = "+new Date().getTime()+"L;\n\n");  
        
        //json对象
        StringBuilder jsonSB = new StringBuilder();  
        jsonSB.append("\t\t\t"+this.config.getTablename().toLowerCase()+"EntityData : {\n");
        processAllAttrs(sb,jsonSB);//属性  
        //processAllMethod(sb);//get set方法  
        jsonSB.append("\t\t\t}");  
        sb.append("}\n");  
          
        //System.out.println(sb.toString());  
        this.entity_content = sb.toString();
        this.json_content = jsonSB.toString();
    }  
      
    /** 
     * 功能：生成所有属性 
     * @param sb 
     * @param jsonSB 
     */  
    private void processAllAttrs(StringBuilder sb, StringBuilder jsonSB) {  
    	List<HashMap<String, String>> tableMetaData = this.config.getTableMetaData();
    	HashMap<String, String> fconf;
        for (int i = 0; i < tableMetaData.size(); i++) {  
        	fconf = tableMetaData.get(i);
        	//column_name 字段备注comments 字段类型data_type 字段长度data_length 主键p
        	if(StringUtil.isNotEmpty(fconf.get("comments"))) {
        		sb.append("\t/** "+fconf.get("comments")+" 长度"+fconf.get("data_length")+" */\n");
        	}
        	
        	if("1".equals(fconf.get("p"))) {
        		sb.append("\t@Id\n");
        	}
            sb.append("\tprivate " + sqlType2JavaType(fconf.get("data_type")) + " " + (fconf.get("column_name").toLowerCase()) + ";\n\n"); 
            jsonSB.append("\t\t\t\t" + (fconf.get("column_name").toLowerCase()) + ": " +"\"\","  );
            if(StringUtil.isNotEmpty(fconf.get("comments"))) {
            	jsonSB.append(" /** "+fconf.get("comments")+" */\n");
            }else {
            	jsonSB.append("\n");
            }
            
        }  
          
    }  
  
    /** 
     * 功能：生成所有方法 
     * @param sb 
     */ 
    /*
    private void processAllMethod(StringBuffer sb) {  
          
        for (int i = 0; i < colnames.length; i++) {  
            sb.append("\tpublic void set" + initcap(colnames[i]) + "(" + sqlType2JavaType(colTypes[i]) + " " +   
                    colnames[i] + "){\n");  
            sb.append("\t\tthis." + colnames[i] + "=" + colnames[i] + ";\n");  
            sb.append("\t}\n");  
            sb.append("\tpublic " + sqlType2JavaType(colTypes[i]) + " get" + initcap(colnames[i]) + "(){\n");  
            sb.append("\t\treturn " + colnames[i] + ";\n");  
            sb.append("\t}\n");  
        }  
          
    }  
    */
      
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
        if("sql".equals(this.getConfig().getType())) {
        	 return new String(ch) + "Dto";  
        }
        return new String(ch);  
    }  
  
    /** 
     * 功能：获得列的数据类型 
     * @param sqlType 
     * @return 
     */  
    public static String sqlType2JavaType(String sqlType) {  
          
        if(sqlType.equalsIgnoreCase("binary_double")){  
            return "double";  
        }else if(sqlType.equalsIgnoreCase("binary_float")){  
            return "float";  
        }else if(sqlType.equalsIgnoreCase("blob")){  
            return "byte[]";  
        }else if(sqlType.equalsIgnoreCase("blob")){  
            return "byte[]";  
        }else if(sqlType.equalsIgnoreCase("char") || sqlType.equalsIgnoreCase("nvarchar2")   
                || sqlType.equalsIgnoreCase("varchar2")|| sqlType.equalsIgnoreCase("varchar")){  
            return "String";  
        }else if(sqlType.equalsIgnoreCase("date") || sqlType.equalsIgnoreCase("timestamp")  
                 || sqlType.equalsIgnoreCase("timestamp with local time zone")   
                 || sqlType.equalsIgnoreCase("timestamp with time zone")){  
            return "Date";  
        }else if(sqlType.equalsIgnoreCase("number")){  
            return "Long";  
        }  
          
        return "String";  
    }  
      
    /** 
     * 出口 
     * TODO 
     * @param args 
     * @throws Exception 
     */  
    public static void main(String[] args) throws Exception {
          
    	GenEntity genEntity = new GenEntity(new TableMetaDataConfig("y339","HY_GBGL_ZZGB",""));  
        System.out.println(genEntity.getEntity_content());
        System.out.println(genEntity.getJson_content());
    }  
  
}