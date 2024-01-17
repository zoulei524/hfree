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
public class GZDao {

	@Autowired
	private HBSession session;

	
	@Autowired
	HYBeanUtil hybean;
	
	/**
	 * ====================================================================================================
	 * 方法名称: --列表<br>
	 * 方法创建日期: 2024年1月17日 17:51:00<br>
	 * 方法创建人员: zoulei<br>
	 * 方法功能描述: --列表 分页查询<br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	public String getAa01ListSQL(JSONObject pageData) {
	
		SQLProcessor sqlPro = new SQLProcessor(session, this.getClass());
		
		Map<String,Object> params = new HashMap<>();
		String sql = sqlPro.getSQLFromXml("getAa01List", params);
		
		//分页信息
		JSONObject pageInfo = pageData.getJSONObject("pageInfo");
		int currentPage = pageInfo.getIntValue("currentPage");
		int pageSize = pageInfo.getIntValue("pageSize");
		int start = (currentPage-1)*pageSize;
		//总数
		String countsql = "select count(*) from (" + sql + ") c";
		int total = session.queryForInteger(countsql);
		pageInfo.put("total", total);
		//分页查询
		String querySQL = "select * from (" + sql + ") c limit "+start+","+pageSize ;
		
		List<Map<String,Object>> aa01List = session.queryForList(querySQL);
		
		return querySQL;
	}
	
	public List<Aa01> getAa01List(JSONObject pageData) {
		String querySQL = this.getAa01ListSQL(pageData);
		List<Aa01> aa01List = session.getCurrentSession().createSQLQuery(querySQL).addEntity(Aa01.class).list();
		return aa01List;
	}

	public List<Map<String,Object>> getAa01MapList(JSONObject pageData) {
		String querySQL = this.getAa01ListSQL(pageData);
		List<Map<String,Object>> aa01MapList = session.queryForList(querySQL);
		return aa01MapList;
	}
	
	
	public List<Aa01Dto> getAa01DtoList(JSONObject pageData) {
		List<Map<String,Object>> aa01MapList = this.getAa01MapList(pageData);
		List<Aa01Dto> aa01DtoList = hybean.toHYBean(aa01MapList, Aa01Dto.class);
		return aa01DtoList;
	}
	
	


}
