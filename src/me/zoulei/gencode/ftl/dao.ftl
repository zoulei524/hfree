package com;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.insigma.business.components.hyfield.HYBeanUtil;
import com.insigma.odin.framework.datasource.SQLProcessor;
import com.insigma.odin.framework.persistence.HBSession;


@Repository
public class ${name}Dao {

	@Autowired
	private HBSession session;

${content}

}
