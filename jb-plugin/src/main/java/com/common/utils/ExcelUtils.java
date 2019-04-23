package com.common.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ExcelUtils {

	/**
	 * 
	 * @param xssfRow
	 * @return
	 */
	public static String getValue(XSSFCell xssfCell) {

		if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfCell.getBooleanCellValue());
		} else if (xssfCell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
			DecimalFormat df = new DecimalFormat("0");
			String cellText = df.format(xssfCell.getNumericCellValue());
			return cellText;
		} else {
			return String.valueOf(xssfCell.getStringCellValue());
		}
	}
	
	/**
	 * 
	 * @param xssfRow
	 * @param num
	 * @return
	 */
	public static String getValueFromRow(XSSFRow xssfRow  ,int num) {

		XSSFCell cell = xssfRow.getCell(num);
		if (cell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
			DecimalFormat df = new DecimalFormat("0");
			String cellText = df.format(cell.getNumericCellValue());
			return cellText;
		} else {
			return String.valueOf(cell.getStringCellValue());
		}
	}
	
	/**
	 * 取某一个excel sheet页面
	 * @param xssfSheet
	 * @param row
	 * @param num
	 * @return
	 */
	public static String getValueFromSheet(XSSFSheet xssfSheet,int row ,int num) {

		XSSFRow xssfRow = xssfSheet.getRow(row);
		if(null == xssfRow){
			return "";
		}
		
		XSSFCell cell = xssfRow.getCell(num);
		if(null == cell){
			return "";
		}else{
			if (cell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
				return String.valueOf(cell.getBooleanCellValue());
			} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
				DecimalFormat df = new DecimalFormat("0");
				String cellText = df.format(cell.getNumericCellValue());
				return cellText;
			} else {
				return String.valueOf(cell.getStringCellValue());
			}
		}
		
	}
	
	
	/**
	 * 获取sheet页的某一列的值Value
	 * @param xssfSheet
	 * @param startRow
	 * @param endRow
	 * @param num
	 * @return
	 */
	public static List<String> getValueListFromSheet(XSSFSheet xssfSheet,int startRow ,int endRow,int num) {

		List<String> valueList = new ArrayList<String>();
		int lastRowNum = xssfSheet.getLastRowNum();
		if(endRow>lastRowNum){
			endRow = lastRowNum;
		}
		
		for(int rowNum = startRow;rowNum<=endRow;rowNum++){
			
			String value = "";
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			if(null == xssfRow){
				
			}else{
				
				XSSFCell cell = xssfRow.getCell(num);
				if(null == cell){
					
				}else{
					if (cell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
						value = String.valueOf(cell.getBooleanCellValue());
					} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
						DecimalFormat df = new DecimalFormat("0");
						value = df.format(cell.getNumericCellValue());
					} else {
						value = String.valueOf(cell.getStringCellValue());
					}
				}
				
				
			}
			//判断为空就不插入List
			if(!StringUtils.isBlank(value)){
				valueList.add(value);
			}
			
		}
		
		return valueList;
		
	}
	
}
