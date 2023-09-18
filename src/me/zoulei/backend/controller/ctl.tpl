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
	@RequestMapping("/get${entity}List")
	public ResponseMessage get${entity}List(@RequestBody JSONObject pageData ${otherParams}) {
	
		List<Map<String,Object>> ${tablename}List = service.get${entity}List(pageData);
		return ResponseMessage.ok(${tablename}List);
	}