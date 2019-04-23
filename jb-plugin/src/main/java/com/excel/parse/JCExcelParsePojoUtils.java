package com.excel.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class JCExcelParsePojoUtils {

	public static void main(String[] args) {

		String excelPath = "C://Users//lipf07529//Desktop//接口定义.xlsx";
		String javaPath = "C://Users//lipf07529//Desktop//接口";
		JCExcelParsePojoUtils generatePojoUtils = new JCExcelParsePojoUtils();
		List<List<PojoField>> pojoList = generatePojoUtils.genPojoList(excelPath);
		generatePojoUtils.createPojoFile(pojoList,javaPath);
		

	}

	// 转换数据格式
	private static String getValue(XSSFCell xssfRow) {

		if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfRow.getBooleanCellValue());
		} else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
			DecimalFormat df = new DecimalFormat("0");
			String cellText = df.format(xssfRow.getNumericCellValue());
			return cellText;
		} else {
			return String.valueOf(xssfRow.getStringCellValue());
		}
	}

	public static String leftFix(String str, int length, char fixChar) {

		// 左补0
		int strLength = str.length();
		if (strLength < length) {
			for (int i = 0; i < (length - strLength); i++) {
				str = fixChar + str;
			}
		}
		return str;
	}

	public void createPojoFile(List<List<PojoField>> pojoList, String pojoFilePath) {

		PojoStr pojoStr = new PojoStr();
		
		System.out.println(pojoList.size());

		for(List<PojoField> pojoClassList:pojoList){
			
			StringBuffer strBuffer = new StringBuffer();
			
			String pojoFileName = "";
			//一个class类
			String transCode = pojoClassList.get(0).getTransCode();
			String transName = pojoClassList.get(0).getTransName();
			boolean isReq = pojoClassList.get(0).isReq();
			if(isReq){
				pojoFileName = "ReqPojo"+transCode;
			}else{
				pojoFileName = "RespPojo"+transCode;
				
			}
			
			strBuffer.append(pojoStr.getPrefixPojoStr(pojoFileName, transName));
			
			for(PojoField pojoField : pojoClassList){
				
				strBuffer.append(pojoStr.getFieldStr(pojoField));
				
			}
			
			strBuffer.append(" \n } \n");
			
			
			File pojoFile = null;
			FileOutputStream out = null;
			try {

				pojoFile = new File(pojoFilePath+"//"+pojoFileName+".java");
				pojoFile.createNewFile();
				out = new FileOutputStream(pojoFile);
				out.write(strBuffer.toString().getBytes("UTF-8"));
				out.flush();
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		
	}

	public List<List<PojoField>> genPojoList(String path) {

		List<List<PojoField>> pojoList = new ArrayList<>();

		try {

			InputStream is = new FileInputStream(path);

			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);

			int total = xssfWorkbook.getNumberOfSheets();

			List<PojoField> reqPojoFieldList = null;new ArrayList<PojoField>();
			List<PojoField> respPojoFieldList = null;new ArrayList<PojoField>();

			for (int i = 0; i < total; i++) {

				XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(i);

				reqPojoFieldList = new ArrayList<PojoField>();
				respPojoFieldList = new ArrayList<PojoField>();
				
				boolean flag = true;
				
				

				XSSFRow firstXssfRow = xssfSheet.getRow(0);
				String transCode = getValue(firstXssfRow.getCell(0));
				String transName = getValue(firstXssfRow.getCell(1));
				
				for (int rowNum = 2; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {

					XSSFRow xssfRow = xssfSheet.getRow(rowNum);
					PojoField reqPojo = null;
					PojoField respPojo = null;
					if (xssfRow != null) {
						// 读取第一列数据

						XSSFCell accountCell0 = xssfRow.getCell(0);
						XSSFCell accountCell1 = xssfRow.getCell(1);
						XSSFCell accountCell2 = xssfRow.getCell(2);
						XSSFCell accountCell3 = xssfRow.getCell(3);

						XSSFCell accountCell6 = xssfRow.getCell(6);
						XSSFCell accountCell7 = xssfRow.getCell(7);
						XSSFCell accountCell8 = xssfRow.getCell(8);
						XSSFCell accountCell9 = xssfRow.getCell(9);

						if (null != accountCell0) {

							String name = getValue(accountCell0);
							if (!("".equals(name) || null == name)) {
								reqPojo = new PojoField();
								reqPojo.setTransCode(transCode);
								reqPojo.setTransName(transName);
								reqPojo.setReq(true);
								reqPojo.setName(name.trim());

								if (null != accountCell1) {
									reqPojo.setType(getValue(accountCell1).trim());
								}
								if (null != accountCell2) {
									reqPojo.setDesc(getValue(accountCell2).trim());
								}
								if (null != accountCell3) {
									reqPojo.setDetailDesc(getValue(accountCell3).trim());
								}
								reqPojoFieldList.add(reqPojo);
							}

						}

						if (null != accountCell6) {

							String name = getValue(accountCell6);
							if (!("".equals(name) || null == name)) {
								respPojo = new PojoField();
								respPojo.setTransCode(transCode);
								respPojo.setTransName(transName);
								respPojo.setReq(false);
								respPojo.setName(name.trim());

								if (null != accountCell7) {
									respPojo.setType(getValue(accountCell7).trim());
								}
								if (null != accountCell8) {
									respPojo.setDesc(getValue(accountCell8).trim());
								}
								if (null != accountCell9) {
									respPojo.setDetailDesc(getValue(accountCell9).trim());
								}
								respPojoFieldList.add(respPojo);
							}

						}

					}

				}

				pojoList.add(reqPojoFieldList);
				pojoList.add(respPojoFieldList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return pojoList;
	}

}

class PojoField {

	String transCode;
	String transName;
	String name;
	String type;
	public String getTransName() {
		return transName;
	}

	public void setTransName(String transName) {
		this.transName = transName;
	}

	String desc;
	String detailDesc;
	boolean isReq = false;

	public boolean isReq() {
		return isReq;
	}

	public void setReq(boolean isReq) {
		this.isReq = isReq;
	}

	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDetailDesc() {
		return detailDesc;
	}

	public void setDetailDesc(String detailDesc) {
		this.detailDesc = detailDesc;
	}

	public String toString() {

		return "transCode = [" + transCode + "] transName = ["+transName+"] name = [" + name + "] type = [" + type + "] desc = [" + desc
				+ "] detailDesc = [" + detailDesc + "]";
	}
}

class PojoStr {

	public String getPrefixPojoStr(String pojoFileName,String transName){
		
		String reqOrResp = "";
		if(pojoFileName.startsWith("req")||pojoFileName.startsWith("Req")){
			reqOrResp = "req";
		}else{
			reqOrResp = "resp";
		}
		
		return "/********************************************  \n"
				+ "* 文件名称: "+pojoFileName+".java         \n"
				+ "* 系统名称: 9c公共系统模块V1.0                     \n"
				+ "* 模块名称:                                    \n"
				+ "* 软件版权: 恒生电子股份有限公司                                                                \n"
				+ "* 功能说明:                                    \n"
				+ "* 系统版本: 1.0.0.1                            \n"
				+ "* 开发人员: lipf                                 \n"
				+ "* 开发时间: 2019-03-25 上午09:15:29                \n"
				+ "* 审核人员:                                        \n"
				+ "* 相关文档:                                         \n"
				+ "* 修改记录:                                        \n"
				+ "* 版本信息 	修改标签 	修改人员 	修改说明 	修改单编号                                           \n"
				+ "*********************************************/           \n"
				+ "                                                      \n"
				+ "package com.hundsun.epay.pub.pojo.ebs."+reqOrResp+";           \n"
				+ "                                                         \n"
				+ "import com.alibaba.fastjson.annotation.JSONField;        \n"
				+ "import com.hundsun.epay.pub.base.frame.BusiPojo;                                                                                \n"
				+ "                                                                                                                                \n"
				+ "                                                                                                                                \n"
				+ "/**                                                                                                                             \n"
				+ " * @ClassName "+pojoFileName+"                                                                                             \n"
				+ " * @Description "+transName+"                                                                                                   \n"
				+ " * @author lipf07529 lipf07529@hundsun.com                                                                            \n"
				+ " * @date 2019-3-25                                                                                                              \n"
				+ " */                                                                                                                             \n"
				+ "public class "+pojoFileName+" extends BusiPojo{                                                                                   \n"
				+ "                                                                                                                                \n"
				+ "	public static final String HUNDSUN_VERSION= \"@system 9c公共系统模块   @version 1.0.0.1 @lastModiDate 20150909 @describe\";   \n"
				+ "                                                                                                                                \n"
				+ "                                                                                                                                \n"
				+ "	/**                                                                                                                           \n"
				+ "	 * @Fields serialVersionUID : TODO                                                                                            \n"
				+ "	 */                                                                                                                           \n"
				+ "	private static final long serialVersionUID = 4718778426892797938L;                                                            \n";
	}
	
	
	public String getFieldStr(PojoField pojoField){
		
		String fieldName = pojoField.getName();
		String fieldType = pojoField.getType();
		String desc = pojoField.getDesc();
		String detailDesc = pojoField.getDetailDesc();
		
		String typeStr = "";
		if(fieldType.startsWith("Str")||fieldType.startsWith("str")){
			
			typeStr = "String";
		}else if(fieldType.startsWith("Big")||fieldType.startsWith("big")){
			
			typeStr = "BigDecimal";
		}else if(fieldType.startsWith("int")||fieldType.startsWith("Int")){
			
			typeStr = "int";
		}else {
			
			typeStr = "";
		}
		
		String upperFieldName = fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
		String lowerFieldName = fieldName.substring(0, 1).toLowerCase()+fieldName.substring(1);
		String fieldStr = "/**"+desc+"*/ \n"
//						+"@JSONField(name = \""+upperFieldName+"\") \n"
						+"private "+typeStr+" "+lowerFieldName+"; \n";
		
		
		return fieldStr;
	}
	
	
	
}
