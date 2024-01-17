package com;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.insigma.business.components.hyfield.HYBeanUtil;
import com.insigma.business.util.FilePathStoreUtil;
import com.insigma.framework.ResponseMessage;
import com.insigma.framework.exception.AppException;
import com.insigma.sys.common.CurrentUserService;
import com.insigma.web.support.service.PageInitService;


@RestController
@RequestMapping("/business/aa01")
public class GZController {
	
	private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private CurrentUserService currentUserService;

	@Autowired
	private PageInitService pageInitService;
	
    @Autowired
    private GZService service;
    
	
	
	//代码初始化
	@PostMapping("/doInit")
	public ResponseMessage doInit(@RequestBody JSONObject jsonObject) {
		pageInitService.init(jsonObject);
		return ResponseMessage.ok("初始化成功！", jsonObject);
	}

	/**
	 * ====================================================================================================
	 * 方法名称: --列表<br>
	 * 方法创建日期: 2024年1月17日 17:51:00<br>
	 * 方法创建人员: zoulei<br>
	 * 方法维护情况: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@RequestMapping("/getAa01List")
	public ResponseMessage getAa01List(@RequestBody JSONObject pageData ) {
		try {
			List<Aa01> aa01List = service.getAa01List(pageData);
			pageData.put("tableData", aa01List);
			return ResponseMessage.ok(pageData);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.error(e.getMessage());
		}
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
	@RequestMapping("/getAa01InfoById")
	public ResponseMessage getAa01InfoById(@RequestBody JSONObject pageData) {
		try {
		    String aaa001 = pageData.getString("aaa001");
		    Aa01 aa01 = service.getAa01InfoById(aaa001);
		    return ResponseMessage.ok(aa01);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.error(e.getMessage());
		}
	}
    
    /**
	 * ====================================================================================================
	 * 方法名称: --保存修改接口<br>
	 * 方法创建日期: 2024年1月17日 17:51:00<br>
	 * 方法创建人员: zoulei<br>
	 * 方法维护情况: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@RequestMapping("/saveAa01Info")
	public ResponseMessage saveAa01Info(@RequestBody JSONObject pageData) {
		try {
	        service.saveAa01Info(pageData);
	        return ResponseMessage.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.error(e.getMessage());
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
	@RequestMapping("/deleteAa01ById")
	public ResponseMessage deleteAa01ById(@RequestBody JSONObject pageData) {
		try {
			String aaa001 = pageData.getString("aaa001");
			service.deleteAa01ById(aaa001);
			return ResponseMessage.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.error(e.getMessage());
		}
	}
 
 
  

}
