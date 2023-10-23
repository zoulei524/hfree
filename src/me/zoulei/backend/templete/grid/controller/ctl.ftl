	//import com.alibaba.fastjson.JSONObject;
	/**
	 * ====================================================================================================
	 * 方法名称: ${tablecomment}--列表<br>
	 * 方法创建日期: ${time}<br>
	 * 方法创建人员: ${author}<br>
	 * 方法维护情况: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@RequestMapping("/get${entity}List")
	public ResponseMessage get${entity}List(@RequestBody JSONObject pageData ${otherParams}) {
		List<Map<String,Object>> ${tablename}List = service.get${entity}List(pageData);
		pageData.put("tableData", ${tablename}List);
		return ResponseMessage.ok(pageData);
	}
<#if config.iscrud>	
	/**
	 * ====================================================================================================
	 * 方法名称: ${tablecomment}--根据id获取数据<br>
	 * 方法创建日期: ${time}<br>
	 * 方法创建人员: ${author}<br>
	 * 方法维护情况: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@RequestMapping("/get${entity}InfoById")
	public ResponseMessage get${entity}InfoById(@RequestBody JSONObject pageData) {
	    String ${config.pk} = pageData.getString("${config.pk}");
	    ${entity} vo = service.get${entity}InfoById(${config.pk});
	    return ResponseMessage.ok(vo);
	}
    
    /**
	 * ====================================================================================================
	 * 方法名称: ${tablecomment}--保存修改接口<br>
	 * 方法创建日期: ${time}<br>
	 * 方法创建人员: ${author}<br>
	 * 方法维护情况: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@RequestMapping("/save${entity}Info")
	public ResponseMessage save${entity}Info(@RequestBody JSONObject pageData) {
        JSONObject ${tablename}EntityData = pageData.getJSONObject("${tablename}EntityData");
        service.save${entity}Info(${tablename}EntityData);
        return ResponseMessage.ok();
	}
    
    /**
	 * ====================================================================================================
	 * 方法名称: ${tablecomment}--根据主键id删除<br>
	 * 方法创建日期: ${time}<br>
	 * 方法创建人员: ${author}<br>
	 * 方法维护情况: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@RequestMapping("/delete${entity}ById")
	public ResponseMessage delete${entity}ById(@RequestBody JSONObject pageData) {
		String ${config.pk} = pageData.getString("${config.pk}");
		service.delete${entity}ById(${config.pk});
		return ResponseMessage.ok();
	}
 </#if>  
 
 
 <#if config.exportExcel>
 	/**
	 * ====================================================================================================
	 * 方法名称: ${tablecomment}--导出全部数据到excel<br>
	 * 方法创建日期: ${time}<br>
	 * 方法创建人员: ${author}<br>
	 * 方法维护情况: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@RequestMapping("/export${entity}Excel")
	public ResponseMessage export${entity}Excel(@RequestBody JSONObject pageData) {
		try {
			//导出excel，返回excel绝对路径
			String path = service.export${entity}Excel(pageData);
			pageData.put("path", FilePathStoreUtil.storePath(path));
			pageData.put("filename", "${tablecomment}");
			return ResponseMessage.ok(pageData);
		} catch (AppException e) {
			e.printStackTrace();
			return ResponseMessage.error(e.getMessage());
		}
	}
</#if>  