package ${config.outputpackage}.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.insigma.business.QGGWY.QGGWY_PUB001.QGGWY_PUB001_0004.enums.EHyFile;
import com.insigma.business.components.hyfield.HYBeanUtil;
import com.insigma.business.util.DownFileDirUtil;
import com.insigma.business.util.StringUtil;
import com.insigma.business.util.UUIDGenerator;
import com.insigma.common.exception.ServiceException;
import com.insigma.framework.exception.AppException;
import com.insigma.odin.framework.persistence.HBSession;
import com.insigma.sys.common.CurrentUserService;

import ${config.outputpackage}.dao.${entity};
import ${config.outputpackage}.dao.${name}Dao;
import ${config.outputpackage}.service.${name}Service;

/**
 * 
 * @author generated by hfree
 * @date ${time}
 */
@Service
public class ${name}ServiceImpl implements ${name}Service {

	@Autowired
	private ${name}Dao dao;
	
	@Autowired
	private CurrentUserService currentUserService;
	
	@Autowired
	private HBSession session;
   
${content}

}
