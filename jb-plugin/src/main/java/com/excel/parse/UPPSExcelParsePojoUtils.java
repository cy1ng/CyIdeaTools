package com.excel.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.common.utils.ExcelUtils;
import com.common.utils.StringUtils;
import com.pojo.format.UPPSPojoCommonStr;
import com.pojo.format.UPPSPojoField;
import com.pojo.format.UPPSPojoStruture;


public class UPPSExcelParsePojoUtils {
	
	public static int initStartSheetNo = 9;
	public static int sys_transcodeSheetNo = 5;//自然数
	public static int sys_transcodeStartRow = 4;//交易码所在sheet页的起始行数（按自然数计算）
	public static int sys_transcodeStartNum = 3;//交易码所在sheet页的起始列数（按自然数计算）
	
//	public static String EXCELPATH = "D://工作空间//01.工作空间//统一支付4.0//BUPPS40//trunk//Documents//D2.Designs//02.接口文档//HUNDSUN银行统一支付平台V4.0-收单业务对外发布-服务接口V0.0.0.1.xlsx";
//	public static String REQPOJOPATH = "D://工作空间//01.工作空间//统一支付4.0//BUPPS40//trunk//Documents//D2.Designs//02.接口文档//ReqPojo";
//	public static String RESPPOJOPATH = "D://工作空间//01.工作空间//统一支付4.0//BUPPS40//trunk//Documents//D2.Designs//02.接口文档//RespPojo";
	
	
	public static String EXCELPATH = "/Users/chengying/work/hundsun/newepay/design/HUNDSUN银行统一支付平台V4.0-资金通道系统交互-服务接口V0.0.0.1.xlsx";
	public static String REQPOJOPATH = "/Users/chengying/work/hundsun/newepay/design/req";
	public static String RESPPOJOPATH = "/Users/chengying/work/hundsun/newepay/design/resp";

	
	public static String PACKAGEPATH ="com.hundsun";
	
	public static void main(String[] args) throws Exception {

		String author = "chengying";
		
		/*方式一：根据页码生成java代码*/
//		List<UPPSPojoStruture> uPPSPojoStrutureList = UPPSExcelParsePojoUtils.genPojoList(EXCELPATH,10,13);
//		UPPSExcelParsePojoUtils.createPojoFile(PACKAGEPATH, author, uPPSPojoStrutureList, REQPOJOPATH, RESPPOJOPATH);	

		
//		/*方式二：根据交易代码列表生成jva代码*/
//		List<String> valueList = new ArrayList<String>();
//		valueList.add("ch0001");
//		valueList.add("ch0002");
//		valueList.add("ch0003");
//		List<UPPSPojoStruture> uPPSPojoStrutureList = UPPSExcelParsePojoUtils.genPojoList(EXCELPATH,valueList);
//		
//		UPPSExcelParsePojoUtils.createPojoFile(PACKAGEPATH, author, uPPSPojoStrutureList, REQPOJOPATH, RESPPOJOPATH);	

		/*方式三：指定接口excel中第4页交易代码的起始行数和截止行数，生成相应的pojo代码*/
		genPojoFile(EXCELPATH,PACKAGEPATH,author,REQPOJOPATH,RESPPOJOPATH,4, 8);//自然行数
		
		
	}


	/**
	 * 创建Pojo的java文件
	 * @param packagePath
	 * @param author
	 * @param uPPSPojoStrutureList
	 * @param reqPojoFilePath
	 * @param respPojoFilePath
	 * @throws Exception
	 */
	public static void createPojoFile(String packagePath,String author,List<UPPSPojoStruture> uPPSPojoStrutureList, String reqPojoFilePath,String respPojoFilePath) throws Exception {
		
		for(UPPSPojoStruture uPPSPojoStruture:uPPSPojoStrutureList){
			
			StringBuffer reqStrBuffer = new StringBuffer();
			StringBuffer respStrBuffer = new StringBuffer();
			
			//一个class类
			String transCode = uPPSPojoStruture.getTransCode();
			String transName = uPPSPojoStruture.getTransName();
			
			/*请求java代码生成*/
			reqStrBuffer.append(UPPSPojoCommonStr.getImportStr(packagePath, true));
			reqStrBuffer.append(UPPSPojoCommonStr.getPojoDesc(author, transName, true));
			reqStrBuffer.append(UPPSPojoCommonStr.getClassDefine(transCode, true));
			reqStrBuffer.append(UPPSPojoCommonStr.getAllFieldStr(uPPSPojoStruture.getReqPojoFieldList()));
			reqStrBuffer.append(UPPSPojoCommonStr.getEndClassStr());
			System.out.println(reqStrBuffer.toString());
			
			/*请求java代码生成*/
			respStrBuffer.append(UPPSPojoCommonStr.getImportStr(packagePath, false));
			respStrBuffer.append(UPPSPojoCommonStr.getPojoDesc(author, transName, false));
			respStrBuffer.append(UPPSPojoCommonStr.getClassDefine(transCode, false));
			respStrBuffer.append(UPPSPojoCommonStr.getAllFieldStr(uPPSPojoStruture.getRespPojoFieldList()));
			respStrBuffer.append(UPPSPojoCommonStr.getEndClassStr());
			System.out.println(respStrBuffer.toString());
			
			String reqPojoFileName = "Req"+uPPSPojoStruture.getTransCode()+".java";
			String respPojoFileName = "Resp"+uPPSPojoStruture.getTransCode()+".java";
			//生成请求POJO
			generateJavaPojo(reqStrBuffer, reqPojoFilePath,reqPojoFileName);
			//生成应答POJO
			generateJavaPojo(respStrBuffer,respPojoFilePath,respPojoFileName);
			
			
		}
		
	}

	/**
	 * 写文件
	 * @param strBuffer
	 * @param pojoFilePath
	 * @param pojoFileName
	 * @throws Exception
	 */
	public static void generateJavaPojo(StringBuffer strBuffer,String pojoFilePath,String pojoFileName) throws Exception{
		
		FileOutputStream out = null;
		File pojoFile = null;
		try {
			System.out.println(pojoFilePath+"//"+pojoFileName);
			pojoFile = new File(pojoFilePath+"//"+pojoFileName);
			if(!pojoFile.exists()){
				pojoFile.createNewFile();
			}
			
			out = new FileOutputStream(pojoFile);
			out.write(strBuffer.toString().getBytes("UTF-8"));
			out.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally{
			out.close();
		}
		
	}
	
	/**
	 * 生成PojoList
	 * @param path
	 * @param startPageNo
	 * @param endPageNo
	 * @return
	 * @throws Exception 
	 */
	public static List<UPPSPojoStruture> genPojoList(String path,int startPageNo,int endPageNo) throws Exception {

		List<UPPSPojoStruture> pojoList = new ArrayList<UPPSPojoStruture>();

		try {

			InputStream is = new FileInputStream(path);

			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);

			int total = xssfWorkbook.getNumberOfSheets();
			
			if(endPageNo<total){
				total = endPageNo;
			}

			List<UPPSPojoField> reqPojoFieldList = null;
			List<UPPSPojoField> respPojoFieldList = null;
			
			for (int i = startPageNo; i < total; i++) {
				
				UPPSPojoStruture uPPSPojoStruture = new UPPSPojoStruture();
				XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(i);

				//rowNum -行   cell(i) -列  均从0开始
				String modelName = ExcelUtils.getValue(xssfSheet.getRow(1).getCell(2));//功能模块名字
				String transName = ExcelUtils.getValue(xssfSheet.getRow(2).getCell(2));//交易名称
				String transCode = ExcelUtils.getValue(xssfSheet.getRow(3).getCell(2));//交易代码
				uPPSPojoStruture.setTransCode(transCode);
				uPPSPojoStruture.setTransName(transName);
				uPPSPojoStruture.setModelName(modelName);
				
				System.out.println(transCode);
				System.out.println(transName);
				System.out.println(modelName);
				
				//xssfSheet.getRow(5).getCell(2);//场景描述
				
				//获取请求报文的起始行数、截止行数和应答报文的起始行数和截止行数
				int reqStartRowNum = 0;
				int reqEndRowNum = 0;
				int respStartRowNum = 0;
				int respEndRowNum = xssfSheet.getLastRowNum();
				
				XSSFRow xssfRow = null;
				boolean isRespRow = false;
				for (int rowNum = 1; rowNum <respEndRowNum; rowNum++) {

					//当第一次取到第二列的值="请求接口"时，生成请求pojo  	直到第二列的值="应答接口"时，生成应答pojo到结尾。
					xssfRow = xssfSheet.getRow(rowNum);
					
					if(null == xssfRow){
						
						continue;
						
					}else{
						
						XSSFCell accountCell0 = xssfRow.getCell(1);
						String cell2Value = ExcelUtils.getValue(accountCell0);
						
						if("请求报文".equalsIgnoreCase(cell2Value)||"请求接口".equalsIgnoreCase(cell2Value)){
							reqStartRowNum = rowNum+3;
							System.out.println("当前rownum："+ rowNum +"  reqStartRowNum = " + reqStartRowNum);
							//跳转到当前行数  
						}
						if("响应报文".equalsIgnoreCase(cell2Value)||"响应接口".equalsIgnoreCase(cell2Value)){
							reqEndRowNum = rowNum-1;
							respStartRowNum = rowNum+3;
							
							System.out.println("当前rownum："+ rowNum +"  reqEndRowNum = " + reqEndRowNum);
							System.out.println("当前rownum："+ rowNum +"  respStartRowNum = " + respStartRowNum);
							isRespRow = true;
						}
						
						if(isRespRow&&StringUtils.isBlank(cell2Value)){
							//如何判断应答报文的最后一行？ 在存在某一行第二列value=“响应报文”后，出现新的空行，空行的上一行即为应答报文的最后一行
							respEndRowNum = rowNum-1;
							System.out.println("当前rownum："+ rowNum +"  respEndRowNum = " + respEndRowNum);
							break;
						}
						
					}
					
				}
				
				System.out.println("reqStartRowNum="+reqStartRowNum);
				System.out.println("reqEndRowNum="+reqEndRowNum);
				System.out.println("respStartRowNum="+respStartRowNum);
				System.out.println("respEndRowNum="+respEndRowNum);
				
				reqPojoFieldList = parseExcelToPojo(xssfSheet,reqStartRowNum,reqEndRowNum,transCode,transName,true);
				respPojoFieldList = parseExcelToPojo(xssfSheet,respStartRowNum,respEndRowNum,transCode,transName,false);

				uPPSPojoStruture.setReqPojoFieldList(reqPojoFieldList);
				uPPSPojoStruture.setRespPojoFieldList(respPojoFieldList);
				
				pojoList.add(uPPSPojoStruture);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} 
		
		return pojoList;
	}
	
	
	/**
	 * 根据服务列表生成待生成的接口Pojo列表
	 * @param path
	 * @param transCodeList
	 * @return
	 * @throws Exception
	 */
	public static List<UPPSPojoStruture> genPojoList(String path,List<String> transCodeList) throws Exception {

		List<UPPSPojoStruture> pojoList = new ArrayList<UPPSPojoStruture>();

		try {

			InputStream is = new FileInputStream(path);

			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);

			int total = xssfWorkbook.getNumberOfSheets();
			
			List<UPPSPojoField> reqPojoFieldList = null;
			List<UPPSPojoField> respPojoFieldList = null;
			
			for (int i = initStartSheetNo; i < total; i++) {
				
				UPPSPojoStruture uPPSPojoStruture = new UPPSPojoStruture();
				XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(i);

				//rowNum -行   cell(i) -列  均从0开始
				String modelName = ExcelUtils.getValue(xssfSheet.getRow(1).getCell(2));//功能模块名字
				String transName = ExcelUtils.getValue(xssfSheet.getRow(2).getCell(2));//交易名称
				String transCode = ExcelUtils.getValue(xssfSheet.getRow(3).getCell(2));//交易代码
				uPPSPojoStruture.setTransCode(transCode);
				uPPSPojoStruture.setTransName(transName);
				uPPSPojoStruture.setModelName(modelName);
				//xssfSheet.getRow(5).getCell(2);//场景描述
				
				System.out.println(transCode);
				System.out.println(transName);
				System.out.println(modelName);
				
				if(transCodeList.contains(transCode)){
					//获取请求报文的起始行数、截止行数和应答报文的起始行数和截止行数
					int reqStartRowNum = 0;
					int reqEndRowNum = 0;
					int respStartRowNum = 0;
					int respEndRowNum = xssfSheet.getLastRowNum();
					
					XSSFRow xssfRow = null;
					boolean isRespRow = false;
					for (int rowNum = 1; rowNum <respEndRowNum; rowNum++) {

						//当第一次取到第二列的值="请求接口"时，生成请求pojo  	直到第二列的值="应答接口"时，生成应答pojo到结尾。
						xssfRow = xssfSheet.getRow(rowNum);
						
						if(null == xssfRow){
							
							continue;
							
						}else{
							
							XSSFCell accountCell0 = xssfRow.getCell(1);
							String cell2Value = ExcelUtils.getValue(accountCell0);
							
							if("请求报文".equalsIgnoreCase(cell2Value)||"请求接口".equalsIgnoreCase(cell2Value)){
								reqStartRowNum = rowNum+3;
								System.out.println("当前rownum："+ rowNum +"  reqStartRowNum = " + reqStartRowNum);
								//跳转到当前行数  
							}
							if("响应报文".equalsIgnoreCase(cell2Value)||"响应接口".equalsIgnoreCase(cell2Value)){
								reqEndRowNum = rowNum-1;
								respStartRowNum = rowNum+3;
								
								System.out.println("当前rownum："+ rowNum +"  reqEndRowNum = " + reqEndRowNum);
								System.out.println("当前rownum："+ rowNum +"  respStartRowNum = " + respStartRowNum);
								isRespRow = true;
							}
							
							if(isRespRow&&StringUtils.isBlank(cell2Value)){
								//如何判断应答报文的最后一行？ 在存在某一行第二列value=“响应报文”后，出现新的空行，空行的上一行即为应答报文的最后一行
								respEndRowNum = rowNum-1;
								System.out.println("当前rownum："+ rowNum +"  respEndRowNum = " + respEndRowNum);
								break;
							}
							
						}
						
					}
					
					System.out.println("reqStartRowNum="+reqStartRowNum);
					System.out.println("reqEndRowNum="+reqEndRowNum);
					System.out.println("respStartRowNum="+respStartRowNum);
					System.out.println("respEndRowNum="+respEndRowNum);
					
					reqPojoFieldList = parseExcelToPojo(xssfSheet,reqStartRowNum,reqEndRowNum,transCode,transName,true);
					respPojoFieldList = parseExcelToPojo(xssfSheet,respStartRowNum,respEndRowNum,transCode,transName,false);

					uPPSPojoStruture.setReqPojoFieldList(reqPojoFieldList);
					uPPSPojoStruture.setRespPojoFieldList(respPojoFieldList);
					
					pojoList.add(uPPSPojoStruture);
				
					
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} 
		
		return pojoList;
	}
	
	
	/**
	 * 根据excel中第4页的交易代码页面，设置起始交易码行数和截至交易码行数，生成待生成统一支付接口列表
	 * @param path
	 * @param transCodeStartRowNum
	 * @param transCodeEndRowNum
	 * @return
	 * @throws Exception
	 */
	public static List<UPPSPojoStruture> genPojoListFromTransCode(String path,int transCodeStartRowNum, int transCodeEndRowNum) throws Exception {

		
		List<UPPSPojoStruture> pojoList = new ArrayList<UPPSPojoStruture>();

		InputStream is = new FileInputStream(path);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		
		//默认取第4页
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(sys_transcodeSheetNo-1);
		
		if(transCodeStartRowNum< sys_transcodeStartRow){
			transCodeStartRowNum = sys_transcodeStartRow;
		}
		
		List<String> transCodeList = ExcelUtils.getValueListFromSheet(xssfSheet, transCodeStartRowNum-1, transCodeEndRowNum-1, sys_transcodeStartNum-1);
		
		for(String str:transCodeList){
			System.out.println(str);
		}
		
		List<UPPSPojoStruture> uPPSPojoStrutureList = UPPSExcelParsePojoUtils.genPojoList(EXCELPATH,transCodeList);
		return uPPSPojoStrutureList;
	}
	
	
	/**
	 * 根据excel中第4页的交易代码页面，设置起始交易码行数和截至交易码行数，生成统一支付java文件
	 * @param path
	 * @param packagePath
	 * @param author
	 * @param reqPojoFilePath
	 * @param respPojoFilePath
	 * @param transCodeStartRowNum
	 * @param transCodeEndRowNum
	 * @throws Exception
	 */
	public static void genPojoFile(String path,String packagePath,String author,String reqPojoFilePath,String respPojoFilePath,int transCodeStartRowNum, int transCodeEndRowNum) throws Exception {

		List<UPPSPojoStruture> pojoList = genPojoListFromTransCode(path,transCodeStartRowNum,transCodeEndRowNum);
		createPojoFile(packagePath,author,pojoList, reqPojoFilePath,respPojoFilePath);
	}
	
	
	
	/**
	 * 生成一个请求或应答类的属性列表信息
	 * @param xssfSheet
	 * @param startNum
	 * @param endNum
	 * @param transCode
	 * @param transName
	 * @param isReq
	 * @return
	 */
	public static List<UPPSPojoField> parseExcelToPojo(XSSFSheet xssfSheet,int startNum,int endNum,String transCode,String transName,Boolean isReq){
		
		List<UPPSPojoField> uPPSPojoFieldList = new ArrayList<UPPSPojoField>();
		//生成请求Pojo
		XSSFRow xssfRow = null;
		
		for(int rowNum = startNum; rowNum <= endNum; rowNum++){
			
			UPPSPojoField pojoField = null;
			xssfRow = xssfSheet.getRow(rowNum);
//			PojoField respPojo = null;
			if (xssfRow != null) {
				// 读取第一列数据
				
				XSSFCell cell1 =xssfRow.getCell(1);
				if(null == cell1){
					continue;
				}else{
					
					if(StringUtils.isBlank(ExcelUtils.getValue(cell1))){
						continue;
					}
				}
				
				String fieldName = ExcelUtils.getValue(xssfRow.getCell(1)).trim();
				String upperNode = ExcelUtils.getValue(xssfRow.getCell(2)).trim();
				String fieldDesc = ExcelUtils.getValue(xssfRow.getCell(3)).trim();
				String fieldType = ExcelUtils.getValue(xssfRow.getCell(4)).trim();
				String fieldLength = ExcelUtils.getValue(xssfRow.getCell(5)).trim();
				String precision = ExcelUtils.getValue(xssfRow.getCell(6)).trim();
				String detailDesc = ExcelUtils.getValue(xssfRow.getCell(7)).trim();
				String checkClassName = ExcelUtils.getValue(xssfRow.getCell(8)).trim();
				String isNotNull = ExcelUtils.getValue(xssfRow.getCell(9)).trim();
				
				System.out.println(fieldName+upperNode+fieldDesc+fieldType+fieldLength+precision+detailDesc+checkClassName+isNotNull);
				
				if(!StringUtils.isBlank(fieldName)){
					
					pojoField = new UPPSPojoField();
					pojoField.setName(fieldName);
					pojoField.setUpperNode(upperNode);
					pojoField.setDesc(fieldDesc);
					pojoField.setType(fieldType);
					pojoField.setLength(fieldLength);
					pojoField.setPrecision(precision);
					pojoField.setCheckClassName(checkClassName);
					pojoField.setReq(isReq);
					pojoField.setTransCode(transCode);
					pojoField.setTransName(transName);
					pojoField.setDetailDesc(detailDesc);
					if("M".equalsIgnoreCase(isNotNull)){
						pojoField.setIsNotNull(true);
					}else{
						pojoField.setIsNotNull(false);
					}
					
					uPPSPojoFieldList.add(pojoField);
				}

			}

		}
		
		return uPPSPojoFieldList;
		
	}

}

