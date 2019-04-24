package com.excel.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.common.utils.ExcelUtils;
import com.common.utils.StringUtils;
import com.pojo.format.UPPSPojoField;
import com.pojo.format.UPPSPojoStruture;

public class UPPSPojoParseExcelUtils {
	
	
	public static String examplePath = System.getProperty("user.dir")+"//Example//example.xlsx";

	public static void main(String[] args) throws Exception {
		
		String pojoPath = "D://工作空间//01.工作空间//统一支付4.0//BUPPS40//trunk//Documents//D2.Designs//02.接口文档//dto";
		List<UPPSPojoStruture> pojoList = JavaFileToStruture.createPojoList(pojoPath);
		
		String destExcelPath = "C://Users//lipf07529//Desktop//test1";
		String destExcelName = "HUNDSUN银行统一支付平台V4.0-收单业务对外发布-服务接口V0.0.0.3.xlsx";
		
		InputStream in = new FileInputStream(examplePath);
		XSSFWorkbook fromBook = new XSSFWorkbook(in);
		createPojoExcel(pojoList,fromBook,destExcelPath,destExcelName);
		
		
	}
	
	
	/**
	 * 
	 * @param pojoList
	 * @param fromBook
	 * @param toExcelPath
	 * @param toExcelFileName
	 * @throws Exception 
	 */
	public static void createPojoExcel(List<UPPSPojoStruture> pojoList,XSSFWorkbook fromBook,String toExcelPath,String toExcelFileName) throws Exception{
	
		File file = new File(toExcelPath);
		file.mkdirs();
		String filePath = toExcelPath+"//"+toExcelFileName;
		createPojoExcel(pojoList,fromBook,filePath);
		
	}
	
	
	
	/**
	 * 
	 * @param pojoList
	 * @param fromBook
	 * @param toExcelPath
	 * @throws Exception
	 */
	public static void createPojoExcel(List<UPPSPojoStruture> pojoList,XSSFWorkbook fromBook,String toExcelPath) throws Exception{
		
		XSSFSheet fromSheet = fromBook.getSheet("example");
		//for循环创建 excel
		for(UPPSPojoStruture pojo:pojoList){
			
			String transCode = pojo.getTransCode();
			String transName = pojo.getTransName();
			List<UPPSPojoField> reqFieldList = pojo.getReqPojoFieldList();
			
			if(StringUtils.isBlank(transName)){
				transName = transCode;
			}
			
			XSSFSheet newSheet = fromBook.createSheet(transName);
			ExcelUtils.copySheet(fromSheet, newSheet);
			
			//接口excel头部赋值
			setCellValue(newSheet,1,2,transName);
			setCellValue(newSheet,2,2,transName);
			setCellValue(newSheet,3,2,transCode);
			
			int reqListSize = 0;
			if(null != reqFieldList&&reqFieldList.size()>0){
				//请求报文赋值到excel
				setExcelValue(reqFieldList,newSheet,12);
				reqListSize = reqFieldList.size();
			}else{
				
			}
			
			//应答报文赋值到excel
			List<UPPSPojoField> respFieldList = pojo.getRespPojoFieldList();
			
			//copy 复制第9、10、11行到新的三行  并且第9行复制后的那一行名字修改为“响应报文”   第9行+3+请求长度
			XSSFRow reqRow9 = newSheet.getRow(9);
			XSSFRow reqRow10 = newSheet.getRow(10);
			XSSFRow reqRow11 = newSheet.getRow(11);
			
			XSSFRow respRow1 = newSheet.createRow(9+3+reqListSize);
			XSSFRow respRow2 = newSheet.createRow(10+3+reqListSize);
			XSSFRow respRow3 = newSheet.createRow(11+3+reqListSize);
			
			ExcelUtils.copyRow(reqRow9, respRow1, true);
			ExcelUtils.copyRow(reqRow10, respRow2, true);
			ExcelUtils.copyRow(reqRow11, respRow3, true);
			respRow1.getCell(1).setCellValue("响应报文");
			respRow3.getCell(1).setCellValue("respHeader");
			
			int respStartRow = 9+3+reqListSize+3;
			if(null != respFieldList&&respFieldList.size()>0){
				//请求报文赋值到excel
				setExcelValue(respFieldList,newSheet,respStartRow);
			}
			
		}
		
		OutputStream out = new FileOutputStream(toExcelPath);
		fromBook.write(out);
		out.close();
		
	}
	
	/**
	 * 
	 * @param sheet
	 * @param rowNo
	 * @param cellNo
	 * @param value
	 */
	public static void setCellValue(XSSFSheet sheet,int rowNo,int cellNo,String value){
		XSSFRow row = sheet.getRow(rowNo);
		if(null == row){
			sheet.createRow(rowNo);
			row = sheet.getRow(rowNo);
		}
		XSSFCell cell = row.getCell(cellNo);
		if(null == cell){
			row.createCell(cellNo);
			cell = row.getCell(cellNo);
		}
		row.getCell(cellNo).setCellValue(value);
	}
	
	/**
	 * 
	 * @param fieldList
	 * @param sheet
	 * @param startRow
	 */
	public static void setExcelValue(List<UPPSPojoField> fieldList,XSSFSheet sheet,int startRow){
		
		for(UPPSPojoField field:fieldList){
			
			String name = field.getName();
			String type = field.getType();
			String length = field.getLength();
			String precision = field.getPrecision();//精度
			Boolean isNotNull = field.getIsNotNull();
//			String modelName = field.getModelName();
			String upperNode = field.getUpperNode();
			String desc = field.getDesc();
			String detailDesc = field.getDetailDesc();
			String checkClassName = field.getCheckClassName();
//			boolean isReq = field.isReq();//请求报文还是应答报文
			
			
			//字段名1	父节点名2	字段含义3	类型4	长度5	精度6	具体描述7	枚举字典8	填写标志9
			XSSFRow row = sheet.getRow(startRow);
			if(null == row){
				sheet.createRow(startRow);
				row = sheet.getRow(startRow);
			}
			row.createCell(1);
			row.getCell(1).setCellValue(name);
			row.createCell(2);
			row.getCell(2).setCellValue(upperNode);
			row.createCell(3);
			row.getCell(3).setCellValue(desc);
			row.createCell(4);
			row.getCell(4).setCellValue(type);
			row.createCell(5);
			row.getCell(5).setCellValue(length);
			row.createCell(6);
			row.getCell(6).setCellValue(precision);
			row.createCell(7);
			row.getCell(7).setCellValue(detailDesc);
			row.createCell(8);
			row.getCell(8).setCellValue(checkClassName);
			row.createCell(9);
			if(null == isNotNull){ isNotNull = false;}
			if(isNotNull){
				row.getCell(9).setCellValue("M");
			}else{
				row.getCell(9).setCellValue("O");
			}
			startRow++;
			
		}
		
	}
	

}
