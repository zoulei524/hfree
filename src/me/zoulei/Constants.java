package me.zoulei;

/**
 * 
* @author zoulei 
* @date 2023年9月18日 上午11:15:51 
* @description  配置项
 */
public class Constants {
	/**接口路径 RequestMapping*/
	public static String DATA_URL = "/Test";
	/**属性是否换行 配置项*/
	public static Boolean ATTR_NEW_LINE = true;
	/**作者*/
	public static String AUTHOR = "zoulei";
	/**实体类包*/
	public static String PACKAGE_OUTPATH = "com.insigma.business.entity.yuhang";
	
	/**获取codetype的sql*/
	public static String CODE_VALUE_SQL = "select code_type,type_name from HY_GBGL_ZZGB.code_type order by code_type";
	
	/**存取codetype的项*/
	public static String[] codetype_items=new String[1];
	
	/** 
     * 功能：将输入字符串的首字母改成大写 
     * @param str 
     * @return 
     */  
	public static String initcap(String str) {  
          
        char[] ch = str.toCharArray();  
        if(ch[0] >= 'a' && ch[0] <= 'z'){  
            ch[0] = (char)(ch[0] - 32);  
        }  
        return new String(ch);  
    }  
	
}
