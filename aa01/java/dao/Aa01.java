package com.insigma.business.entity.yuhang;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import com.insigma.business.components.hyfield.HYField;
import java.io.Serializable;

/**
 * aa01 实体类
 * 2024-01-17 05:51:00 zoulei
 */ 
@Data
@Entity 
@Table(name = "AA01") 
public class Aa01 implements Serializable {

	private static final long serialVersionUID = 1705485060693L;

	/** 参数类别代码 长度50 */
	@Id
	private String aaa001;

	/** 参数类别名称 长度500 */
	private String aaa002;

	/** 参数值 长度500 */
	private String aaa005;

	/** 参数值说明描述 长度200 */
	private String aaa105;

	/** 参数类别代码是否可维护标志代码 长度20 */
	private String aaa104;

	/** 是否有效标志代码 长度20 */
	private String active;

	/** 上一版本参数值 长度500 */
	private String oldparam;

}
