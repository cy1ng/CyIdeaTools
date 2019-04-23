package com.excel.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ParserTest {

	public static void main(String[] args) {
//		genMerInfoFile();
//		genMerParamsFile();
		genMerAccountFile();
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
	
	public static String leftFix(String str,int length,char fixChar){
		
		 //左补0
		int strLength = str.length();
		if(strLength<length){
			for(int i = 0;i<(length -strLength);i++){
				str = fixChar+str;
			}
		}
		return str;
	}
	
	
	
	
	public static void genMerInfoFile(){
		
		try {
			
			String bid = "";
			String phone = "";
			InputStream is = new FileInputStream("C://Users//lipf07529//Desktop//二批次//a.xlsx");
			
			File merInfoFile = new File("C://Users//lipf07529//Desktop//二批次//merInfo20180614.sql");
			FileOutputStream out = new FileOutputStream(merInfoFile);
			String merInfoStr = null;
			
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
			// 获取每一个工作薄
			for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
				XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
				if (xssfSheet == null) {
					continue;
				}
				// 获取当前工作薄的每一行
				 for (int rowNum = 3; rowNum <= xssfSheet.getLastRowNum();
				 rowNum++) {
//				for (int rowNum = 3; rowNum <= 4; rowNum++) {
					
					XSSFRow xssfRow = xssfSheet.getRow(rowNum);
					String acctNo = null;
					String merId = null;
					String merName = null;
					String payCode = null;
					String refundCode = null;
					String userNo = null;
					String userFixStr = "20180614000000";//后面是9位序号
					

					if (xssfRow != null) {
						// 读取第一列数据
						XSSFCell accountCell = xssfRow.getCell(0);
						acctNo = getValue(accountCell);
						// 读取第二列数据
						XSSFCell merchantIDCell = xssfRow.getCell(1);
						merId = getValue(merchantIDCell);

						XSSFCell simpleNameCell = xssfRow.getCell(3);
						merName = getValue(simpleNameCell);

						XSSFCell payCodeCell = xssfRow.getCell(4);
						payCode = getValue(payCodeCell);

						XSSFCell refundCell = xssfRow.getCell(5);
						refundCode = getValue(refundCell);

					}
					
					userNo = userFixStr+"000000"+acctNo.substring(acctNo.length()-3);
					
					String newMerInfoStr = "INSERT INTO merchant_info (\"MER_ID\", \"MER_CODE\", \"MER_NAME\", \"MER_STATUS\", \"MER_TYPE\", \"SUP_MER_ID\", \"USER_ID\", \"USER_NAME\", \"MER_LINK_NAME\", \"MER_LINK_PHONE\", \"MER_LINK_TEL\", \"MER_LINK_EMAIL\", \"TASK_LINK_QQ_GROUPID\", \"TASK_LINK_MOBILE_GROUPID\", \"MER_CITECODE\", \"MER_CITENAME\", \"MER_DOMAIN\", \"MER_OPENDATE\", \"MER_OPNEUSER\", \"HANDLE_ORG_ID\", \"HANDLE_USER_ID\", \"CHECK_ORG_ID\", \"CHECK_OUSER_ID\", \"SETT_TYPE\", \"MER_SIGN_TYPE\", \"MER_CERT\", \"PUBLIC_KEY\", \"MER_CERTSN\", \"PUBKEY_STARTDATE\", \"PUBKEY_ENDDATE\", \"FEE_FLAG\", \"FEE_RT_FLAG\", \"RESERVE\", \"REMARK\", \"MEMO\", \"TIMESTAMP\", \"EXTFLD1\", \"EXTFLD2\", \"EXTFLD3\") VALUES ('"+merId+"', '"+merId+"', '"+merName+"', '0', '1', 'G4000311000018', '"+userNo+"', '"+merName+"', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '20180615', 'SysAdmin', NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, '20180615', NULL, NULL, NULL);";
					merInfoStr = merInfoStr + newMerInfoStr+"\n";
				}
				break;
			}
			
			out.write(merInfoStr.getBytes());
			out.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
	}
	
	public static void genMerParamsFile(){
		
		try {
			
			String bid = "";
			String phone = "";
			InputStream is = new FileInputStream("C://Users//lipf07529//Desktop//二批次//a.xlsx");
			
			File merInfoFile = new File("C://Users//lipf07529//Desktop//二批次//merParams20180614.sql");
			FileOutputStream out = new FileOutputStream(merInfoFile);
			String merStr = null;
			
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
			// 获取每一个工作薄
			for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
				XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
				if (xssfSheet == null) {
					continue;
				}
				// 获取当前工作薄的每一行
				 for (int rowNum = 3; rowNum <= xssfSheet.getLastRowNum();
				 rowNum++) {
//				for (int rowNum = 3; rowNum <= 4; rowNum++) {
					
					XSSFRow xssfRow = xssfSheet.getRow(rowNum);
					String acctNo = null;
					String merId = null;
					String merName = null;
					String payCode = null;
					String refundCode = null;
					String userNo = null;
					String userFixStr = "20180614000000";//后面是9位序号
					

					if (xssfRow != null) {
						// 读取第一列数据
						XSSFCell accountCell = xssfRow.getCell(0);
						acctNo = getValue(accountCell);
						// 读取第二列数据
						XSSFCell merchantIDCell = xssfRow.getCell(1);
						merId = getValue(merchantIDCell);

						XSSFCell simpleNameCell = xssfRow.getCell(3);
						merName = getValue(simpleNameCell);

						XSSFCell payCodeCell = xssfRow.getCell(4);
						payCode = getValue(payCodeCell);

						XSSFCell refundCell = xssfRow.getCell(5);
						refundCode = getValue(refundCell);

					}
					
					userNo = acctNo.substring(acctNo.length()-3);
					
					String newMerParamsStr =""
							+"INSERT INTO MER_PARAMS (\"ID\", \"MER_ID\", \"MER_CODE\", \"MER_NAME\", \"MER_PARAM_MOLD\", \"MER_PARAM_VALUE\", \"MER_PARAM_INFO\", \"MER_PARAM_STATUS\", \"CREATE_TIME\", \"UPDATE_TIME\", \"EXTFLD1\", \"EXTFLD2\", \"EXTFLD3\") VALUES ('2018061500000100000"+userNo+"', '"+merId+"', '"+merId+"', '"+merName+"', 'A00', 'A0', '结算周期', '0', '20180615', '20180615', NULL, NULL, NULL);\n"
							+"INSERT INTO MER_PARAMS (\"ID\", \"MER_ID\", \"MER_CODE\", \"MER_NAME\", \"MER_PARAM_MOLD\", \"MER_PARAM_VALUE\", \"MER_PARAM_INFO\", \"MER_PARAM_STATUS\", \"CREATE_TIME\", \"UPDATE_TIME\", \"EXTFLD1\", \"EXTFLD2\", \"EXTFLD3\") VALUES ('2018061500000200000"+userNo+"', '"+merId+"', '"+merId+"', '"+merName+"', 'A01', 'A0', '手续费周期', '0', '20180615', '20180615', NULL, NULL, NULL);\n"
							+"INSERT INTO MER_PARAMS (\"ID\", \"MER_ID\", \"MER_CODE\", \"MER_NAME\", \"MER_PARAM_MOLD\", \"MER_PARAM_VALUE\", \"MER_PARAM_INFO\", \"MER_PARAM_STATUS\", \"CREATE_TIME\", \"UPDATE_TIME\", \"EXTFLD1\", \"EXTFLD2\", \"EXTFLD3\") VALUES ('2018061500000300000"+userNo+"', '"+merId+"', '"+merId+"', '"+merName+"', 'A02', 'b3b6fc83bf4f40ae99ec8eaf73671407', '手续费规则', '0', '20180615', '20180615', NULL, NULL, NULL);\n"
							+"INSERT INTO MER_PARAMS (\"ID\", \"MER_ID\", \"MER_CODE\", \"MER_NAME\", \"MER_PARAM_MOLD\", \"MER_PARAM_VALUE\", \"MER_PARAM_INFO\", \"MER_PARAM_STATUS\", \"CREATE_TIME\", \"UPDATE_TIME\", \"EXTFLD1\", \"EXTFLD2\", \"EXTFLD3\") VALUES ('2018061500000400000"+userNo+"', '"+merId+"', '"+merId+"', '"+merName+"', 'A07', '"+payCode+"', '支付子交易代码', '0', '20180615', '20180615', NULL, NULL, NULL);\n"
							+"INSERT INTO MER_PARAMS (\"ID\", \"MER_ID\", \"MER_CODE\", \"MER_NAME\", \"MER_PARAM_MOLD\", \"MER_PARAM_VALUE\", \"MER_PARAM_INFO\", \"MER_PARAM_STATUS\", \"CREATE_TIME\", \"UPDATE_TIME\", \"EXTFLD1\", \"EXTFLD2\", \"EXTFLD3\") VALUES ('2018061500000500000"+userNo+"', '"+merId+"', '"+merId+"', '"+merName+"', 'A08', '"+refundCode+"', '退款子交易代码', '0', '20180615', '20180615', NULL, NULL, NULL);\n"
							+"\n";
							
					merStr = merStr + newMerParamsStr+"\n";
				}
				break;
			}
			
			out.write(merStr.getBytes());
			out.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void genMerAccountFile(){
		
		try {
			
			String bid = "";
			String phone = "";
			InputStream is = new FileInputStream("C://Users//lipf07529//Desktop//二批次//a.xlsx");
			
			File merInfoFile = new File("C://Users//lipf07529//Desktop//二批次//merAccount20180614.sql");
			FileOutputStream out = new FileOutputStream(merInfoFile);
			String merStr = null;
			
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
			// 获取每一个工作薄
			for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
				XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
				if (xssfSheet == null) {
					continue;
				}
				// 获取当前工作薄的每一行
				 for (int rowNum = 3; rowNum <= xssfSheet.getLastRowNum();
				 rowNum++) {
//				for (int rowNum = 3; rowNum <= 4; rowNum++) {
					
					XSSFRow xssfRow = xssfSheet.getRow(rowNum);
					String acctNo = null;
					String merId = null;
					String merName = null;
					String payCode = null;
					String refundCode = null;
					String userNo = null;
					String userFixStr = "20180614000000";//后面是9位序号
					

					if (xssfRow != null) {
						// 读取第一列数据
						XSSFCell accountCell = xssfRow.getCell(0);
						acctNo = getValue(accountCell);
						// 读取第二列数据
						XSSFCell merchantIDCell = xssfRow.getCell(1);
						merId = getValue(merchantIDCell);

						XSSFCell simpleNameCell = xssfRow.getCell(3);
						merName = getValue(simpleNameCell);

						XSSFCell payCodeCell = xssfRow.getCell(4);
						payCode = getValue(payCodeCell);

						XSSFCell refundCell = xssfRow.getCell(5);
						refundCode = getValue(refundCell);

					}
					
					userNo = acctNo.substring(acctNo.length()-3);
					
					String newMerParamsStr =""
							+"INSERT INTO MER_ACCOUNT (\"ID\", \"MER_ID\", \"MER_CODE\", \"MER_NAME\", \"MER_ACCT_TYPE\", \"ACCT_SENCE\", \"MER_ACCT_NO\", \"MER_ACCT_NAME\", \"MER_SUB_ACCT_NO\", \"INOUT_FLAG\", \"INHERIT\", \"ACCRUAL_FLAG\", \"CR_RATE\", \"OVERDRAFT_RATE\", \"MER_ACCT_STATUS\", \"CREATE_TIME\", \"UPDATE_TIME\", \"EXTFLD1\", \"EXTFLD2\", \"EXTFLD3\") VALUES ('2018061500001100000"+userNo+"', '"+merId+"', '"+merId+"', '"+merName+"', '1', '0', NULL, '银行手续费收入账户', NULL, NULL, NULL, NULL, NULL, NULL, '0', '20180307', '20180423', '1111', NULL, NULL);\n" 
							+"INSERT INTO MER_ACCOUNT (\"ID\", \"MER_ID\", \"MER_CODE\", \"MER_NAME\", \"MER_ACCT_TYPE\", \"ACCT_SENCE\", \"MER_ACCT_NO\", \"MER_ACCT_NAME\", \"MER_SUB_ACCT_NO\", \"INOUT_FLAG\", \"INHERIT\", \"ACCRUAL_FLAG\", \"CR_RATE\", \"OVERDRAFT_RATE\", \"MER_ACCT_STATUS\", \"CREATE_TIME\", \"UPDATE_TIME\", \"EXTFLD1\", \"EXTFLD2\", \"EXTFLD3\") VALUES ('2018061500001200000"+userNo+"', '"+merId+"', '"+merId+"', '"+merName+"', '0', '1', NULL, '手续费支出账户', NULL, NULL, NULL, NULL, NULL, NULL, '0', '20180307', '20180315', NULL, NULL, NULL);\n"
							+"INSERT INTO MER_ACCOUNT (\"ID\", \"MER_ID\", \"MER_CODE\", \"MER_NAME\", \"MER_ACCT_TYPE\", \"ACCT_SENCE\", \"MER_ACCT_NO\", \"MER_ACCT_NAME\", \"MER_SUB_ACCT_NO\", \"INOUT_FLAG\", \"INHERIT\", \"ACCRUAL_FLAG\", \"CR_RATE\", \"OVERDRAFT_RATE\", \"MER_ACCT_STATUS\", \"CREATE_TIME\", \"UPDATE_TIME\", \"EXTFLD1\", \"EXTFLD2\", \"EXTFLD3\") VALUES ('2018061500001300000"+userNo+"', '"+merId+"', '"+merId+"', '"+merName+"', '0', '2', NULL, '客户手续费收入账户', NULL, NULL, NULL, NULL, NULL, NULL, '0', '20180307', '20180315', NULL, NULL, NULL);\n"
							+"INSERT INTO MER_ACCOUNT (\"ID\", \"MER_ID\", \"MER_CODE\", \"MER_NAME\", \"MER_ACCT_TYPE\", \"ACCT_SENCE\", \"MER_ACCT_NO\", \"MER_ACCT_NAME\", \"MER_SUB_ACCT_NO\", \"INOUT_FLAG\", \"INHERIT\", \"ACCRUAL_FLAG\", \"CR_RATE\", \"OVERDRAFT_RATE\", \"MER_ACCT_STATUS\", \"CREATE_TIME\", \"UPDATE_TIME\", \"EXTFLD1\", \"EXTFLD2\", \"EXTFLD3\") VALUES ('2018061500001400000"+userNo+"', '"+merId+"', '"+merId+"', '"+merName+"', '0', '3', '"+acctNo+"', '商户结算户', NULL, NULL, NULL, NULL, NULL, NULL, '0', '20180307', '20180315', NULL, NULL, NULL);\n"
							+"INSERT INTO MER_ACCOUNT (\"ID\", \"MER_ID\", \"MER_CODE\", \"MER_NAME\", \"MER_ACCT_TYPE\", \"ACCT_SENCE\", \"MER_ACCT_NO\", \"MER_ACCT_NAME\", \"MER_SUB_ACCT_NO\", \"INOUT_FLAG\", \"INHERIT\", \"ACCRUAL_FLAG\", \"CR_RATE\", \"OVERDRAFT_RATE\", \"MER_ACCT_STATUS\", \"CREATE_TIME\", \"UPDATE_TIME\", \"EXTFLD1\", \"EXTFLD2\", \"EXTFLD3\") VALUES ('2018061500001500000"+userNo+"', '"+merId+"', '"+merId+"', '"+merName+"', '0', '4', '"+acctNo+"', '商户待清算户', NULL, NULL, NULL, NULL, NULL, NULL, '0', '20180307', '20180315', NULL, NULL, NULL);\n"
							+"INSERT INTO MER_ACCOUNT (\"ID\", \"MER_ID\", \"MER_CODE\", \"MER_NAME\", \"MER_ACCT_TYPE\", \"ACCT_SENCE\", \"MER_ACCT_NO\", \"MER_ACCT_NAME\", \"MER_SUB_ACCT_NO\", \"INOUT_FLAG\", \"INHERIT\", \"ACCRUAL_FLAG\", \"CR_RATE\", \"OVERDRAFT_RATE\", \"MER_ACCT_STATUS\", \"CREATE_TIME\", \"UPDATE_TIME\", \"EXTFLD1\", \"EXTFLD2\", \"EXTFLD3\") VALUES ('2018061500001600000"+userNo+"', '"+merId+"', '"+merId+"', '"+merName+"', '0', '5', '"+acctNo+"', '商户退款账户', NULL, NULL, NULL, NULL, NULL, NULL, '0', '20180307', '20180315', NULL, NULL, NULL);\n"
							+"\n";
							
					merStr = merStr + newMerParamsStr+"\n";
				}
				break;
			}
			
			out.write(merStr.getBytes());
			out.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	

}
