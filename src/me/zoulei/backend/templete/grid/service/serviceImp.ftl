
	
	//实现类
	/**
	 * ====================================================================================================
	 * 方法名称: ${tablecomment}--列表<br>
	 * 方法创建日期: ${time}<br>
	 * 方法创建人员: ${author}<br>
	 * 方法维护信息: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@Override
	public List<${entity}> get${entity}List(JSONObject pageData) {
	    List<${entity}> ${tablename}List = dao.get${entity}List(pageData);
	    return ${tablename}List;
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
	@Override
	public ${entity} get${entity}InfoById(String ${config.pk}) {
		${entity} ${tablename} = session.get(${entity}.class, ${config.pk});
		return ${tablename};
	}
	
	/**
	 * ====================================================================================================
	 * 方法名称: ${tablecomment}--保存修改<br>
	 * 方法创建日期: ${time}<br>
	 * 方法创建人员: ${author}<br>
	 * 方法维护情况: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@Override
	@Transactional
	public void save${entity}Info(JSONObject pageData) {
		JSONObject ${tablename}form = pageData.getJSONObject("${tablename}EntityData");
		${entity} ${tablename} = JSON.parseObject(${tablename}form.toJSONString(), ${entity}.class);
  		if(StringUtil.isEmpty(${tablename}.get${config.pkU}())) {
  			${tablename}.set${config.pkU}(UUIDGenerator.generate());
  			session.save(${tablename});
  		}else {
  			session.saveOrUpdate(${tablename});
  		}
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
	@Override
	public void delete${entity}ById(String ${config.pk}) {
		try {
			${entity} ${tablename} = session.get(${entity}.class, ${config.pk});
			session.delete(${tablename});
		} catch (Exception e) {
			throw new ServiceException("删除失败！");
		}
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
	@Override
	public String export${entity}Excel(JSONObject pageData) throws AppException {
		//导出excel绝对路径
		String path = DownFileDirUtil.getTempFilePath(EHyFile.DOWNLOAD).getAbsolutePath()+"/export.xlsx";
		
		//设置分页信息从第1页开始，导出total全部数据
		JSONObject pageInfo = pageData.getJSONObject("pageInfo");
		pageInfo.put("currentPage",1);
		int total = pageInfo.getIntValue("total");
		pageInfo.put("pageSize",total);
		
		//通过分页方法获取数据
		List<Map<String,Object>> ${tablename}List = dao.get${entity}MapList(pageData);
		try {
			${entity}Excel.writeSheetData(path, ${tablename}List);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("导出失败：" + e.getMessage());
		}
		
		return path;
	}
	
	


</#if>