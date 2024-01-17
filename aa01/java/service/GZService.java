package com;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.insigma.framework.exception.AppException;

public interface GZService {

	//接口
	List<Aa01> getAa01List(JSONObject pageData);
	public Aa01 getAa01InfoById(String aaa001);
	public void saveAa01Info(JSONObject aa01form);
	public void deleteAa01ById(String aaa001);
	
	


}
