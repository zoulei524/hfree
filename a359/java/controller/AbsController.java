package com

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
@RequestMapping("/business/a359")
public class AbsController {
	
	private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private CurrentUserService currentUserService;

	@Autowired
	private PageInitService pageInitService;
	
    @Autowired
    private AbsService service;
    
	
	
	//代码初始化
	@PostMapping("/doInit")
	public ResponseMessage doInit(@RequestBody JSONObject jsonObject) {
		pageInitService.init(jsonObject);
		return ResponseMessage.ok("初始化成功！", jsonObject);
	}

	/**
	 * ====================================================================================================
	 * 方法名称: 艰苦地区工作经历--列表<br>
	 * 方法创建日期: 2024年1月17日 15:58:56<br>
	 * 方法创建人员: zoulei<br>
	 * 方法维护情况: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@RequestMapping("/getA359List")
	public ResponseMessage getA359List(@RequestBody JSONObject pageData ) {
		try {
			List<A359> a359List = service.getA359List(pageData);
			pageData.put("tableData", a359List);
			return ResponseMessage.ok(pageData);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.error(e.getMessage());
		}
	}
	/**
	 * ====================================================================================================
	 * 方法名称: 艰苦地区工作经历--根据id获取数据<br>
	 * 方法创建日期: 2024年1月17日 15:58:56<br>
	 * 方法创建人员: zoulei<br>
	 * 方法维护情况: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@RequestMapping("/getA359InfoById")
	public ResponseMessage getA359InfoById(@RequestBody JSONObject pageData) {
		try {
		    String a35900 = pageData.getString("a35900");
		    A359 a359 = service.getA359InfoById(a35900);
		    return ResponseMessage.ok(a359);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.error(e.getMessage());
		}
	}
    
    /**
	 * ====================================================================================================
	 * 方法名称: 艰苦地区工作经历--保存修改接口<br>
	 * 方法创建日期: 2024年1月17日 15:58:56<br>
	 * 方法创建人员: zoulei<br>
	 * 方法维护情况: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@RequestMapping("/saveA359Info")
	public ResponseMessage saveA359Info(@RequestBody JSONObject pageData) {
		try {
	        service.saveA359Info(pageData);
	        return ResponseMessage.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.error(e.getMessage());
		}
	}
    
    /**
	 * ====================================================================================================
	 * 方法名称: 艰苦地区工作经历--根据主键id删除<br>
	 * 方法创建日期: 2024年1月17日 15:58:56<br>
	 * 方法创建人员: zoulei<br>
	 * 方法维护情况: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@RequestMapping("/deleteA359ById")
	public ResponseMessage deleteA359ById(@RequestBody JSONObject pageData) {
		try {
			String a35900 = pageData.getString("a35900");
			service.deleteA359ById(a35900);
			return ResponseMessage.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.error(e.getMessage());
		}
	}
 
 
 	/**
	 * ====================================================================================================
	 * 方法名称: 艰苦地区工作经历--导出全部数据到excel<br>
	 * 方法创建日期: 2024年1月17日 15:58:56<br>
	 * 方法创建人员: zoulei<br>
	 * 方法维护情况: <br>
	 * 方法: ●原创○沿用○重构汇
	 * ====================================================================================================
	 */
	@RequestMapping("/exportA359Excel")
	public ResponseMessage exportA359Excel(@RequestBody JSONObject pageData) {
		try {
			//导出excel，返回excel绝对路径
			String path = service.exportA359Excel(pageData);
			pageData.put("path", FilePathStoreUtil.storePath(path));
			pageData.put("filename", "艰苦地区工作经历");
			return ResponseMessage.ok(pageData);
		} catch (AppException e) {
			e.printStackTrace();
			return ResponseMessage.error(e.getMessage());
		}
	}
  

}
