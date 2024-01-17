package com;

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


@Service
public class GZImpl implements GZService {

	@Autowired
	private GZDao dao;
	
	@Autowired
	private CurrentUserService currentUserService;
	
	@Autowired
	private HBSession session;
   

	
	//实现类
	/**
	 * ====================================================================================================
	 * 方法名称: --列表<br>
	 * 方法创建日期: 2024年1月17日 17:51:00<br>
	 * 方法创建人员: zoulei<br>
	 * 方法维护信息: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@Override
	public List<Aa01> getAa01List(JSONObject pageData) {
	    List<Aa01> aa01List = dao.getAa01List(pageData);
	    return aa01List;
	}

	/**
	 * ====================================================================================================
	 * 方法名称: --根据id获取数据<br>
	 * 方法创建日期: 2024年1月17日 17:51:00<br>
	 * 方法创建人员: zoulei<br>
	 * 方法维护情况: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@Override
	public Aa01 getAa01InfoById(String aaa001) {
		Aa01 aa01 = session.get(Aa01.class, aaa001);
		return aa01;
	}
	
	/**
	 * ====================================================================================================
	 * 方法名称: --保存修改<br>
	 * 方法创建日期: 2024年1月17日 17:51:00<br>
	 * 方法创建人员: zoulei<br>
	 * 方法维护情况: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@Override
	@Transactional
	public void saveAa01Info(JSONObject pageData) {
		JSONObject aa01form = pageData.getJSONObject("aa01EntityData");
		Aa01 aa01 = JSON.parseObject(aa01form.toJSONString(), Aa01.class);
  		if(StringUtil.isEmpty(aa01.getAaa001())) {
  			aa01.setAaa001(UUIDGenerator.generate());
  			session.save(aa01);
  		}else {
  			session.saveOrUpdate(aa01);
  		}
	}
	
	/**
	 * ====================================================================================================
	 * 方法名称: --根据主键id删除<br>
	 * 方法创建日期: 2024年1月17日 17:51:00<br>
	 * 方法创建人员: zoulei<br>
	 * 方法维护情况: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@Override
	public void deleteAa01ById(String aaa001) {
		try {
			Aa01 aa01 = session.get(Aa01.class, aaa001);
			session.delete(aa01);
		} catch (Exception e) {
			throw new ServiceException("删除失败！");
		}
	}



}
