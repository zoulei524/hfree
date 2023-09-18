	/**
	 * ====================================================================================================
	 * 方法名称: ${tablecomment}--列表<br>
	 * 方法创建日期: ${time}<br>
	 * 方法创建人员: ${author}<br>
	 * 方法维护信息: 写明时间，维护人员，维护内容<br>
	 * 方法功能描述: ${tablecomment}--列表<br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	public List<Map<String,Object>> get${entity}List(JSONObject pageData) {
	
		SQLProcessor sqlPro = new SQLProcessor(hbSession, this.getClass());
		
		Map<String,Object> params = new HashMap<>();
		String sql = sqlPro.getSQLFromXml("get${entity}List", params);
		List<Map<String,Object>> ${tablename}List = = hbSession.queryForList(sql,params);
		
		return ${tablename}List;
	}
	