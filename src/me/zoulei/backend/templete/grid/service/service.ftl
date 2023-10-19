	//接口
	List<Map<String,Object>> get${entity}List(JSONObject pageData);
<#if config.iscrud>
	public ${entity} get${entity}InfoById(String ${config.pk});
	public void save${entity}Info(JSONObject ${tablename}form);
	public void delete${entity}ById(String ${config.pk});
</#if>
	
	//import cn.hutool.core.bean.BeanUtil;
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
	public List<Map<String,Object>> get${entity}List(JSONObject pageData) {
	
	    List<Map<String,Object>> ${tablename}List = dao.get${entity}List(pageData);;
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
	public void save${entity}Info(JSONObject ${tablename}form) {
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