package ${config.outputpackage}.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.insigma.business.components.hyfield.HYBeanUtil;
import com.insigma.framework.exception.AppException;
import com.insigma.odin.framework.datasource.SQLProcessor;
import com.insigma.odin.framework.persistence.HBSession;

import ${config.outputpackage}.dao.${entity};
import ${config.outputpackage}.dto.${entity}Dto;


@Repository
public class ${name}Dao {

	@Autowired
	private HBSession session;

${content}

}
