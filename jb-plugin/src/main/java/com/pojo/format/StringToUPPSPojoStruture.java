package com.pojo.format;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.common.utils.StringUtils;

public class StringToUPPSPojoStruture {
	
	
	public static void main(String args[]) throws Exception{
		
		String pojoPath = "C:\\Users\\lipf07529\\Desktop\\统一支付4.0Pojo\\ReqRT0007.java";
		File pojoFile = new File(pojoPath);
		UPPSPojoStruture uPPSPojoStruture = createUPPSPojoStrutureByFile(pojoFile);
		System.out.println(uPPSPojoStruture);
	}
	

	/**
	 * 
	 * @param pojoFile
	 * @return
	 * @throws Exception
	 */
	public static UPPSPojoStruture createUPPSPojoStrutureByFile(File pojoFile) throws Exception {

		BufferedReader reader = new BufferedReader(new FileReader(pojoFile));

		String line = "";
		String headerStr = null;
		String bodyStr = null;
		String classStr = null;
		boolean flag = true;
		while (null != line) {
			line = reader.readLine();
			if (null != line) {

				line = line.trim();
				if (flag) {

					if (line.contains("class")) {
						classStr = line.trim();
						flag = false;
					} else {
						headerStr += line + "\n";
					}

				} else {
					bodyStr += line + "\n";
				}

			}

		}

		String className = getClassNameFromStr(headerStr);
		String transName = getTransNameFromStr(classStr);
		
		System.out.println(headerStr);
		System.out.println(classStr);
		System.out.println(bodyStr);
		
		int isReq = -1;
		String transCode = "";
		if(transName.contains("Req")||transName.contains("req")){
			transCode = transName.replace("Req", "").replace("req", "");
			isReq = 0;
			
		}else if(transName.contains("Resp")||transName.contains("resp")){
			transCode = transName.replace("Resp", "").replace("resp", "");
			isReq = 1;
			
		}else{
			
			//bean 
			transCode = transName;
			
		}
		
		UPPSPojoStruture uPPSPojoStruture = new UPPSPojoStruture();
		uPPSPojoStruture.setTransName(className);
		uPPSPojoStruture.setTransCode(transCode);
		List<String> strList = parseBodyFieldStr(bodyStr);
		
		if(transCode.contains("Batch")){
			System.out.println("!!!!!!!!********"+bodyStr);
			System.out.println("!!!!!!!!********"+strList.size());
			for(String str:strList){
				System.out.println("!!!!********"+str);
			}
			
		}
		
		List<UPPSPojoField> uPPSPojoFieldList = createUPPSPojoFieldList(strList);
		
		if(0 == isReq){
			uPPSPojoStruture.setReqPojoFieldList(uPPSPojoFieldList);
		}else if(1 == isReq){
			uPPSPojoStruture.setRespPojoFieldList(uPPSPojoFieldList);
		}else{
			//bean 对象  当请求对象处理
			uPPSPojoStruture.setReqPojoFieldList(uPPSPojoFieldList);
		}
		
//		for(UPPSPojoField uPPSPojoField:uPPSPojoFieldList){
//			System.out.println(uPPSPojoField);
//		}

		reader.close();
		return uPPSPojoStruture;

		// 解析应该分为2部分，一部分为类前面的定义解析，一类为类的body解析

	}
	
	

	/**
	 * 将class类的所有属性，拆成多组
	 * @param bodyStr
	 * @return
	 */
	public static List<String> parseBodyFieldStr(String bodyStr) {

		String[] lineArray = bodyStr.split("\n");
		List<String> fieldStrList = new ArrayList<String>();
		String fieldStr = "";
		for (String line : lineArray) {

			if (line.startsWith("private")) {

				fieldStr += line + "\n";
				fieldStrList.add(fieldStr);
				fieldStr = new String();
			} else if (line.startsWith("}")) {

			} else {

				fieldStr += line + "\n";
			}

		}
		return fieldStrList;
	}

	/**
	 * 
	 * @param fieldStrList
	 * @return
	 */
	public static List<UPPSPojoField> createUPPSPojoFieldList(List<String> fieldStrList) {

		List<UPPSPojoField> uPPSPojoFieldList = new ArrayList<UPPSPojoField>();
		for(String str:fieldStrList){
			UPPSPojoField uPPSPojoField = new UPPSPojoField();
			uPPSPojoField = bodyStrParseField(str);
			uPPSPojoFieldList.add(uPPSPojoField);
		}
		
		return uPPSPojoFieldList;
	}


	/**
	 * 
	 **/
	public static UPPSPojoField bodyStrParseField(String str) {

		UPPSPojoField uPPSPojoField = new UPPSPojoField();

		String[] strArray = str.split("\n");
		
		for (String line : strArray) {

			if (line.startsWith("private")) {
				
				// 拆成3部分
				String[] feildDefineArray = line.trim().replace(";", "").split(" ");
				uPPSPojoField.setType(feildDefineArray[1]);
				uPPSPojoField.setName(feildDefineArray[2]);

			}
			
			if (line.startsWith("*")&&(!line.startsWith("*/"))) {
				
				String filedDesc = line.replace("*","").trim();
				uPPSPojoField.setDesc(filedDesc);
				
			}
			
			if (line.contains("@NotNullBlank")){
				uPPSPojoField.setIsNotNull(true);
			}
			
			if (line.contains("@Length")){
//			    @Length(max = 1)
				//要获取长度和精度
				uPPSPojoField.setLength(StringUtils.getLength(line));
			}
			
			if (line.contains("@Digits")){
				//要获取长度和精度
//				@Digits(maxInteger = 16, maxFraction = 2, message = "交易金额超限")
				StringUtils.getBigDecimalLength(line, uPPSPojoField);
			}
			
			if (line.contains("@CheckWith")){
				//要获取长度和精度
//				@Digits(maxInteger = 16, maxFraction = 2, message = "交易金额超限")
				uPPSPojoField.setCheckClassName(StringUtils.getCheckClassName(line));
			}
			
		}

		return uPPSPojoField;

	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String getClassNameFromStr(String str){
//		/**
//		 * @author zhouliang12890@hundsun.com
//		 * @description 路由试算请求报文
//		 * @date 2019-03-29 10:50
//		 */
		
		String[] strArray = str.split("\n");
		String transName = "";
		for (String line : strArray) {
			if(line.contains("@description")){
				transName = line.replace("@description", "").replace("*", "").replace("请求接口", "").trim();
				
			}
		}
		return transName;
		
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String getTransNameFromStr(String str){
		//public class ReqRT0007 implements Serializable 
		
		String[] strArray = str.split(" ");
		if(strArray.length>=3){
			return strArray[2];
		}else{
			return "";
		}
		
	}


}
