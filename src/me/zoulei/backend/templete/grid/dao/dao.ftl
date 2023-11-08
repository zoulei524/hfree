	/**
	 * ====================================================================================================
	 * 方法名称: ${tablecomment}--列表<br>
	 * 方法创建日期: ${time}<br>
	 * 方法创建人员: ${author}<br>
	 * 方法维护信息: 写明时间，维护人员，维护内容<br>
	 * 方法功能描述: ${tablecomment}--列表 分页查询<br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	public String get${entity}ListSQL(JSONObject pageData) {
	
		SQLProcessor sqlPro = new SQLProcessor(session, this.getClass());
		
		Map<String,Object> params = new HashMap<>();
		String sql = sqlPro.getSQLFromXml("get${entity}List", params);
		
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
		<#if DBType=='mysql'>
		String querySQL = "select * from (" + sql + ") c limit "+start+","+pageSize ;
		<#else>
		String querySQL = "select * from (select rownum as numrow,c.* from (" + sql + ") c  where rownum<=" + (start + pageSize)+") where numrow>=" + (start + 1) ;
		</#if>
		
		List<Map<String,Object>> ${tablename}List = session.queryForList(querySQL);
		
		return querySQL;
	}
	
	public List<${entity}> get${entity}List(JSONObject pageData) {
		String querySQL = this.get${entity}ListSQL(pageData);
		List<${entity}> ${tablename}List = session.getCurrentSession().createSQLQuery(querySQL).addEntity(${entity}.class).list();
		return ${tablename}List;
	}

	public List<Map<String,Object>> get${entity}MapList(JSONObject pageData) {
		String querySQL = this.get${entity}ListSQL(pageData);
		List<Map<String,Object>> ${tablename}MapList = session.queryForList(querySQL);
		return ${tablename}MapList;
	}
	
	
	
	
	
	