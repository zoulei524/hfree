package com;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.insigma.business.components.hyfield.HYBeanUtil;
import com.insigma.business.util.FilePathStoreUtil;
import com.insigma.framework.ResponseMessage;
import com.insigma.framework.exception.AppException;
import com.insigma.sys.common.CurrentUserService;
import com.insigma.web.support.service.PageInitService;


@RestController
@RequestMapping("/business/${tablename}")
public class ${name}Controller {
	
	private Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private CurrentUserService currentUserService;
	
	@Autowired
	private PageInitService pageInitService;
	
	@Autowired
	private ${name}Service service;

${content}

}
