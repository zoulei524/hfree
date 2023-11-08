package com.insigma.business.components.hyfield;

import java.io.Serializable;

import lombok.Data;

/**
 * 字段 这种字段结构主要针对代码字段，通过MapToHYDtoUtil.java将Map<String, Object>类型转成对应的dto
 * hibernate方式获取：
 * 实体类对象上添加注解如
 *  @Type(type ="com.insigma.business.components.hyfield.HYfieldType", 
 * 		parameters = { @Parameter(name ="codetype", value ="GB8561"), @Parameter(name ="p", value ="E")})
 * 2023年10月26日11:08:56 zoulei
 */
@Data
public class HYField  implements Serializable{
	
	/**值*/
	private String value = "";
	/**若有代码，就有代码值，没有就为空*/
	private String key;
	/**R—必填，E—可编辑，H—隐藏，D—不可编辑, E,R—编辑可变为必填*/
	private String p = "E";
	
	/**代码类别*/
	private String codetype;

	public HYField() {
	}
	
	public HYField(String codetype) {
		this.codetype = codetype;
	}
	
	public HYField(String codetype, String p) {
		this.codetype = codetype;
		if(p!=null&&!"".equals(p))
			this.p = p;
	}

	@Override
	public String toString() {
		if(this.codetype!=null && !"".equals(this.codetype)) {
			if(this.key!=null && !"".equals(this.key)) {
				return this.key;
			}else {
				return this.value;
			}
		}
		return this.value;
	}
}











package com.insigma.business.components.hyfield;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.insigma.business.QGGWY.QGGWY_PUB001.QGGWY_PUB001_0004.consts.Consiants;
import com.insigma.business.components.pub.SysCodeUtil;
import com.insigma.framework.exception.AppException;

import cn.hutool.core.util.ObjectUtil;

/**
 * 2023年10月27日09:31:43 zoulei
 * f_rmb1: {
                // 任免表（一）
                status: { value: "", p: "H" },
                a0000: { value: this.propsa0000, p: "H" },
                a0101: { value: "", p: "R" },
                a0104: { key: "", value: "", p: "R" }, // 性别
                a0107: { key: "", value: "", p: "R" }, // 出生年月
            }
 	为了让bean可以直接生成上面的这种结构，增加HYField类型作为字段类型。
    可以将将Map<String, Object>类型或上面这种结构的JSONObject转成对应的dto bean或实体bean。 bean可以有HYField类型的字段。
 */
@Component
public class HYBeanUtil {
	
	@Autowired
	SysCodeUtil sysCodeUtil;
	
	/**
     * 将数据库查询返回的map对象转bean对象，bean可以有 HYField 类型的字段,针对有代码的对象
     */
    public <E> List<E> toHYBean(List<Map<String, Object>> mapList, Class<E> entityClass) throws AppException {
        if (ObjectUtil.isNotEmpty(mapList)) {
            List<E> eList = new ArrayList<>();
            for (Map<String, Object> objectMap : mapList) {
            	eList.add(toHYBean(objectMap, entityClass));
			}
            return eList;
        }
        return null;
    }
 
    /**
     * 将数据库查询返回的map对象转bean对象，bean可以有 HYField 类型的字段,针对有代码的对象
     */
    public <E> E toHYBean(Map<String, Object> map, Class<E> entityClass) throws AppException {
        try {
           
            E entity = entityClass.newInstance();
            List<Field> declaredFields = getAllFiled(entity);
            for (Field field : declaredFields) {
                String key = field.getName().toLowerCase();
                
                Object object = map.get(key);
                
                if (object != null) {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    //主要是针对代码字段
                    if (field.getType() == HYField.class) {
                    	//dto里只允许这种HYField类型的字段
                        HYField hf = (HYField) field.get(entity);
                    	if(object.getClass() == JSONObject.class) {
                    		JSONObject hyObject = (JSONObject) object;
                        	String k = hyObject.getString("key");
                        	String v = hyObject.getString("value");
                        	String p = hyObject.getString("p");
                            hf.setValue(v);
    						hf.setKey(k);
    						hf.setP(p);
                    	}else {
                    		//设置codetype的值
                        	String codetype = hf.getCodetype();
                        	if(codetype!=null) {
        						String codeName = sysCodeUtil.getCodeName(codetype, object.toString());
        						hf.setValue(codeName);
        						hf.setKey(object.toString());
                        	}else {
                        		hf.setValue(object.toString());
                        	}
                    	}
                    	
                    }else
                    //其他类型
                    if (object instanceof BigInteger) {
                        BigInteger bigInteger = (BigInteger) object;
                        if (field.getType() == Long.class) {
                            field.set(entity, bigInteger.longValue());
                        }
                    } else if (object instanceof Timestamp) {
                        Timestamp timestamp = (Timestamp) object;
                        if (field.getType() == LocalDateTime.class) {
                            field.set(entity, timestamp.toLocalDateTime());
                        }
                    } else if (object instanceof Date) {
                        Date date = (Date) object;
                        if (field.getType() == LocalDate.class) {
                            field.set(entity, date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                        } else if (field.getType() == Date.class) {
                            field.set(entity, date);
                        }
                    } else if (object instanceof Byte) {
                        Byte bytes = (Byte) object;
                        if (field.getType() == Byte.class) {
                            field.set(entity, bytes);
                        } else if (field.getType() == Boolean.class) {
                            field.set(entity, bytes.intValue() == 1);
                        }
                    } else if (field.getType() == object.getClass()) {
                        field.set(entity, object);
                    }
                    Object value = field.get(entity);
                    field.setAccessible(accessible);
                    if (value == null) {
                        throw new ClassCastException(String.format("%s.%s 无法将 %s 转换为 %s ", entity.getClass(), field.getName(), object.getClass(), field.getType()));
                    }
                }
                
            }
            return entity;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AppException(ex.getMessage());
 
        }
 
 
    }
 
    /**
     * 获取自身所以属性且超类属性
     *
     * @param object
     * @return
     */
    private List<Field> getAllFiled(Object object) {
        Class<Field> clazz = (Class<Field>) object.getClass();
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            fields.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = (Class<Field>) clazz.getSuperclass();
        }
        return fields;
    }
    
    
    
    /**
     * 将JSONObject转bean对象，bean可以有 HYField 类型的字段,针对有代码的对象
     * 需JSONObject的key都设置成小写
     * 如a0104: { key: "", value: "", p: "R" }
     */
    public <T> T pageElementToBean(JSONObject pageData, String formName, Class<T> clazz) throws AppException {
        JSONObject target = pageData.getJSONObject(formName);
        return pageElementToBean(target, clazz);
    }

    /**
     * 将JSONObject转bean对象，bean可以有 HYField 类型的字段,针对有代码的对象
     * 需JSONObject的key都设置成小写
     * 如a0104: { key: "", value: "", p: "R" }
     */
    public <T> T pageElementToBean(JSONObject pageData, Class<T> clazz) throws AppException {
        
        try {
            T entity = clazz.newInstance();
            List<Field> declaredFields = getAllFiled(entity);
            String k, v, p;
            for (Field field : declaredFields) {
                String key = field.getName().toLowerCase();
                
                Object object = pageData.get(key);
                
                if (object != null&&!"".equals(object)) {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    //主要是针对代码字段
                    if (field.getType() == HYField.class) {
                    	//dto里只允许这种HYField类型的字段
                        HYField hf = (HYField) field.get(entity);
                        //pageData对应的HYField设置到bean上
                        if(object.getClass() == JSONObject.class) {
                        	JSONObject hyObject = (JSONObject) object;
                        	k = hyObject.getString("key");
                            v = hyObject.getString("value");
                            p = hyObject.getString("p");
                            hf.setValue(v);
    						hf.setKey(k);
    						hf.setP(p);
                        }else {
                        	hf.setValue(object.toString());
    						hf.setKey(object.toString());
                        }
                        
                        
                    }else if(object.getClass() == JSONObject.class) {
                    	JSONObject hyObject = (JSONObject) object;
                    	k = hyObject.getString("key");
                        v = hyObject.getString("value");
                        if(k!=null&&!"".equals(k)) {
                        	field.set(entity, k);
                        }else {
                        	field.set(entity, v);
                        }
                        
                    }else
                    //其他类型
                    if (object instanceof BigInteger) {
                        BigInteger bigInteger = (BigInteger) object;
                        if (field.getType() == Long.class) {
                            field.set(entity, bigInteger.longValue());
                        }
                    } else if (object instanceof Timestamp) {
                        Timestamp timestamp = (Timestamp) object;
                        if (field.getType() == LocalDateTime.class) {
                            field.set(entity, timestamp.toLocalDateTime());
                        }
                    } else if (object instanceof Date) {
                        Date date = (Date) object;
                        if (field.getType() == LocalDate.class) {
                            field.set(entity, date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                        } else if (field.getType() == Date.class) {
                            field.set(entity, date);
                        }
                    } else if (object instanceof Byte) {
                        Byte bytes = (Byte) object;
                        if (field.getType() == Byte.class) {
                            field.set(entity, bytes);
                        } else if (field.getType() == Boolean.class) {
                            field.set(entity, bytes.intValue() == 1);
                        }
                    }else if (object instanceof Integer) {
                    	Integer integer = (Integer) object;
                        if (field.getType() == Long.class) {
                            field.set(entity, integer.longValue());
                        } else if (field.getType() == object.getClass()) {
                            field.set(entity, object);
                        }
                    } else if (field.getType() == object.getClass()) {
                        field.set(entity, object);
                    }
                    Object value = field.get(entity);
                    field.setAccessible(accessible);
                    if (value == null) {
                        throw new ClassCastException(String.format("%s.%s 无法将 %s 转换为 %s ", entity.getClass(), field.getName(), object.getClass(), field.getType()));
                    }
                }
                
            }
            return entity;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AppException(ex.getMessage());
 
        }
        
    }
    
}





package com.insigma.business.components.hyfield;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppContext implements ApplicationContextAware {
    
	public static ApplicationContext applicationContext;

    public AppContext() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    	AppContext.applicationContext = applicationContext;
    }
}










package com.insigma.business.components.hyfield;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;
import org.springframework.jdbc.core.JdbcTemplate;

import com.insigma.business.QGGWY.QGGWY_PUB001.QGGWY_PUB001_0004.enums.ECODE;
import com.insigma.business.util.EhCacheUtil;
import com.insigma.odin.framework.persistence.HBSession;

/**
 * hibernate实体的转换
 */
public class HYfieldType implements UserType, DynamicParameterizedType{
	
	private static final int[] SQL_TYPES={Types.VARCHAR}; 
	
	private String codetype;
	private String p;
	
	@Override
	public void setParameterValues(Properties parameters) {
		this.codetype = parameters.getProperty("codetype");
		this.p = parameters.getProperty("p");
	}
	
	@Override
	public Object assemble(Serializable serializable, Object obj)
			throws HibernateException {
		return serializable;
	}

	@Override
	public Object deepCopy(Object obj) throws HibernateException {
		return obj; 
	}

	@Override
	public Serializable disassemble(Object obj) throws HibernateException {
		return (Serializable) obj;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		if(x==y){  
            return true;  
        }else if(x==null||y==null){  
            return false;  
        }else {  
            return x.equals(y);  
        }  
	}

	@Override
	public int hashCode(Object obj) throws HibernateException {
		return obj.hashCode();
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Object replace(Object obj, Object obj1, Object obj2)
			throws HibernateException {
		return obj;
	}

	@Override
	public Class returnedClass() {
		return HYField.class;
	}

	@Override
	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		String data = rs.getString(names[0]); 
		if(rs.wasNull()){  
            return null;  
        }else{  
            HYField hyField = new HYField(codetype, p);
            if(data==null){
            	return hyField;
            }
            hyField.setKey(data);
            String v = this.getCodeName(codetype, data, "");
            hyField.setValue(v);
            return hyField;  
        }  
	}
	
	/**
	 * 通过code_type及code_value获取code_name、code_name2、code_name3
	 *
	 * 样例: SysCodeUtil.getCodeName("ZB01", "110000", "3");
	 */
	@SuppressWarnings("unchecked")
	public  String getCodeName(String codetype,String codevalue, String codeNameNum) {
		if(codevalue == null || "".equals(codevalue)){
			return  "";
		}
		String codetypeNum = "".equals(codeNameNum) ? codetype : codetype + "_" + codeNameNum;
		HashMap<String,String> hashmap=(HashMap<String,String>)EhCacheUtil.getObjectInCache(codetypeNum+ECODE.SELECT.getCode());
		if(hashmap!=null) {
			return hashmap.get(codevalue);
		}else {
			String sql="select code_name from code_value where code_type='"+codetype+"' and code_value='"+codevalue+"' and CODE_STATUS = '1'";
			JdbcTemplate jdbcTemplate = (JdbcTemplate)(AppContext.applicationContext).getBean("jdbcTemplate");
			List<Map<String, Object>> codeDetail = jdbcTemplate.queryForList(sql);
			if (codeDetail != null && codeDetail.size() > 0) {
				Map<String, Object> codes = codeDetail.get(0);
				return codes.get("code_name").toString();
			}else {
				return "";
			}
		}
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
			throws HibernateException, SQLException {
		if(value==null){  
			st.setNull(index, Types.VARCHAR);  
        }else {  
        	HYField hyField = (HYField) value;
        	String k = hyField.getKey();
        	st.setString(index, k);  
        }
		
	}
}







