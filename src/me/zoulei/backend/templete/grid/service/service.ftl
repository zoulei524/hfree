	//接口
	List<${entity}> get${entity}List(JSONObject pageData);
<#if config.iscrud>
	public ${entity} get${entity}InfoById(String ${config.pk});
	public void save${entity}Info(JSONObject ${tablename}form);
	public void delete${entity}ById(String ${config.pk});
</#if>
<#if config.exportExcel>
	public String export${entity}Excel(JSONObject pageData) throws AppException;
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
	
	






//导出excel的类
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ${entity}Excel {
   
 
    /**
	 * 该方法用来将Excel中的ABCD列转换成具体的数据
	 * @param column:ABCD列名称
	 * @return integer：将字母列名称转换成数字
	 * **/
    public static int excelColStrToNum(String column) {
        int num = 0;
        int result = 0;
        int length =column.length(); 
        for(int i = 0; i < length; i++) {
            char ch = column.charAt(length - i - 1);
            num = (int)(ch - 'A' + 1) ;
            num *= Math.pow(26, i);
            result += num;
        }
        return result-1;
    }
 
    /**
	 * 该方法用来将具体的数据转换成Excel中的ABCD列
	 * @param int：需要转换成字母的数字
	 * @return column:ABCD列名称
	 * **/
    public static String excelColIndexToStr(int columnIndex) {
    	columnIndex++;
        if (columnIndex <= 0) {
            return null;
        }
        String columnStr = "";
        columnIndex--;
        do {
            if (columnStr.length() > 0) {
                columnIndex--;
            }
            columnStr = ((char) (columnIndex % 26 + (int) 'A')) + columnStr;
            columnIndex = (int) ((columnIndex - columnIndex % 26) / 26);
        } while (columnIndex > 0);
        return columnStr;
    }
    
    
    
    
    /**
	 * 插入行
	 * @param sheet
	 * @param rowIndex
	 * @return
	 */
	public static Row insertRow(Sheet sheet, Integer rowIndex) {  
        Row row = null;  
        if (sheet.getRow(rowIndex) != null) {  
            int lastRowNo = sheet.getLastRowNum();  
            sheet.shiftRows(rowIndex, lastRowNo, 1);  
        }  
        row = sheet.createRow(rowIndex);  
        return row;  
    }  
	
	
	public static void setCellValue(Cell cell,CellStyle style,Object value){
		
		cell.setCellStyle(style);
		if(value!=null&&!"".equals(value)){
			cell.setCellValue(value.toString());
		}
	}
	
	/**
	 * 带上下左右边框的单元格样式
	 * @param workbook
	 * @return
	 */
	public static CellStyle getCellStyleZhengWen(Workbook workbook,HorizontalAlignment ha,String fname,short size){
		CellStyle style = workbook.createCellStyle();
		DataFormat format = workbook.createDataFormat();
		style.setDataFormat(format.getFormat("@"));
		style.setBorderLeft(BorderStyle.THIN);//左边框   
		style.setBorderRight(BorderStyle.THIN);//右边框  	
		style.setBorderTop(BorderStyle.THIN);//上边框  	
		style.setBorderBottom(BorderStyle.THIN);//下边框  	
		
		style.setAlignment(ha);
		style.setVerticalAlignment(VerticalAlignment.CENTER); 
		Font font =workbook.createFont();  
		font.setFontName(fname);  
		font.setFontHeightInPoints(size);//字体大小
		style.setFont(font);
		style.setWrapText(true);
		return style;
	}

	/**
	 * 输出表格数据
	 * @param path 表格路径
	 * @param aa10List 数据
	 * @throws Exception 
	 */
	public static void writeSheetData(String path, List<Map<String, Object>> ${tablename}List) throws Exception {
		
		//表格列参数  宽度width，是否数字type，水平位置align，列名colname，列名描述label
		String tableConfigStr = "${config.excelCFG}";
		JSONArray tableConfig = JSON.parseArray(tableConfigStr);
		//新建excel工作簿
		Workbook workbook = new XSSFWorkbook();
		//新建sheet页
		Sheet sheet = workbook.createSheet("${tablecomment}（${tablename}）".replaceAll("[/\\\\?*\\[\\]'']", ""));
		//表头样式
		CellStyle styleTitle = getCellStyleZhengWen(workbook,HorizontalAlignment.CENTER,"黑体",(short)14);
		//居左样式
		CellStyle styleLEFT = getCellStyleZhengWen(workbook,HorizontalAlignment.LEFT,"仿宋_GB2312",(short)14);
		//居中样式
		CellStyle styleCENTER = getCellStyleZhengWen(workbook,HorizontalAlignment.CENTER,"仿宋_GB2312",(short)14);
		//居左样式
		CellStyle styleRIGHT = getCellStyleZhengWen(workbook,HorizontalAlignment.RIGHT,"仿宋_GB2312",(short)14);
		//数字样式
		CellStyle styleNumber = getCellStyleZhengWen(workbook,HorizontalAlignment.CENTER,"Times New Roman",(short)14);

		int rowIndex = 0;//行序号
		int cellIndex = 0;//列序号
		//新建一行
		Row row = insertRow(sheet,rowIndex++);
		//表头行高
		row.setHeightInPoints(40);
		//单元格
		Cell cell = null;
		
		//序号列
		cell = row.createCell(cellIndex++);
		setCellValue(cell, styleTitle, "序号");
		//配置参数：宽度width  是否数字type 水平位置align  列名colname  列名描述label
		//设置表头
		for(Object tc : tableConfig){
			JSONObject dataMap = (JSONObject) tc;
			//宽
			String width = dataMap.get("width").toString();
			//列名
			String label = dataMap.get("label").toString();
			//列宽
			sheet.setColumnWidth(cellIndex, Double.valueOf(Double.valueOf(width)/8*255.86+184.27).intValue());
			cell = row.createCell(cellIndex++);
			setCellValue(cell, styleTitle, label);
		}
		
		//填充数据
		int rowCount = ${tablename}List.size();
		//数据
		Map<String, Object> m;
		//配置
		JSONObject cfgMap;
		//配置的字段属性
		String colname,type,align;
		//单元格数据
		Object value;
		//单元格样式
		CellStyle style;
		//填充正文数据
		for(int i=0; i<rowCount; i++){
			//一行数据
			m = ${tablename}List.get(i);
			//新建一行
			row = insertRow(sheet,rowIndex++);
			//列序号
			cellIndex = 0;
			//序号
			cell = row.createCell(cellIndex++);
			setCellValue(cell, styleNumber, i+1);
			for(Object tc : tableConfig){
				//配置
				cfgMap = (JSONObject) tc;
				//字段名
				colname = cfgMap.get("colname").toString();
				//类型除string类型，都用数字样式
				type = cfgMap.get("type").toString();
				//水平位置
				align = cfgMap.get("align").toString();
				//单元格数据
				value = m.get(colname);
				//样式判断
				if("String".equals(type)) {
					if("left".equals(align)) {
						style = styleLEFT;
					}else if("right".equals(align)) {
						style = styleRIGHT;
					}else {
						style = styleCENTER;
					}
				}else {
					style = styleNumber;
				}
				//设置样式及单元格数据
				cell = row.createCell(cellIndex++);
				setCellValue(cell, style, value);
			}
		}

		//输出文件
		OutputStream fos = new FileOutputStream(new File(path));
		workbook.write(fos);
		fos.close();
		workbook.close();
	}
	
}

</#if>