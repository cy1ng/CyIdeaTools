package com.common.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 提供常用excel操作<br>
 *     <ul>
 *         <li></li>
 *     </ul>
 */

public class ExcelUtils {



    /**
     * sheet 复制，复制数据、如果同一个文件，复制样式，不同文件则只复制数据<br/>
     * 如果是同book中复制，建议使用workbook中的cloneSheet()方法<br/>
     *
     * <br/>建议用于 不同book间只复制数据
     * @throws Exception 
     *
     */
    public static void copySheet(Sheet srcSheet, Sheet desSheet) throws Exception {
        copySheet(srcSheet, desSheet, true, true, null);
    }

    /**
     * sheet 复制，如果同一个文件，复制样式，不同文件则不复制<br/>
     *
     * <br/>建议用于 同book中，只复制样式，不复制数据<br/>
     * eg: copySheet(srcSheet, desSheet, false)
     *
     * @param copyValueFlag 控制是否复制数据
     * @throws Exception 
     */
    public static void copySheet(Sheet srcSheet, Sheet desSheet, boolean copyValueFlag) throws Exception {
        copySheet(srcSheet, desSheet, copyValueFlag, true, null);
    }

    /**
     * sheet 复制，复制数据、样式<br/>
     *
     * <br/>建议用于 不同book间复制，同时复制数据和样式<br/>
     * eg: copySheet(srcSheet, desSheet, mapping)
     *
     * @param mapping 不同文件间复制时，如果要复制样式，必传，否则不复制样式
     * @throws Exception 
     */
    public static void copySheet(Sheet srcSheet, Sheet desSheet, StyleMapping mapping) throws Exception {
        copySheet(srcSheet, desSheet, true, true, mapping);
    }

    /**
     * sheet 复制,复制数据<br/>
     *
     *  <br/>建议用于 同book中，只复制数据，不复制样式<br/>
     *  eg: copySheet(srcSheet, desSheet, false, null)
     *
     * @param srcSheet
     * @param desSheet
     * @param copyStyleFlag
     * @param mapping
     * @throws Exception 
     */
    public static void copySheet(Sheet srcSheet, Sheet desSheet, boolean copyStyleFlag, StyleMapping mapping) throws Exception {
        copySheet(srcSheet, desSheet, true, copyStyleFlag, mapping);
    }

    /**
     * sheet 复制, 灵活控制是否控制数据、样式<br/>
     *
     * <br/>不建议直接使用
     *
     * @param copyValueFlag 控制是否复制数据
     * @param copyStyleFlag 控制是否复制样式
     * @param mapping       不同book中复制样式时，必传
     * @throws Exception 
     */
    public static void copySheet(Sheet srcSheet, Sheet desSheet, boolean copyValueFlag, boolean copyStyleFlag, StyleMapping mapping) throws Exception {
        if (srcSheet.getWorkbook() == desSheet.getWorkbook()) {
        }

        //合并区域处理
        copyMergedRegion(srcSheet, desSheet);

        //行复制
        Iterator<Row> rowIterator = srcSheet.rowIterator();

        int areadlyColunm = 0;
        while (rowIterator.hasNext()) {
            Row srcRow = rowIterator.next();
            Row desRow = desSheet.createRow(srcRow.getRowNum());
            copyRow(srcRow, desRow, copyValueFlag, copyStyleFlag, mapping);

            //调整列宽(增量调整)
            if (srcRow.getPhysicalNumberOfCells() > areadlyColunm) {
                for (int i = areadlyColunm; i < srcRow.getPhysicalNumberOfCells(); i++) {
                    desSheet.setColumnWidth(i, srcSheet.getColumnWidth(i));
                }
                areadlyColunm = srcRow.getPhysicalNumberOfCells();
            }
        }
    }

    /**
     * 复制行
     * @throws Exception 
     */
    public static void copyRow(Row srcRow, Row desRow) throws Exception {
        copyRow(srcRow, desRow, true, true, null);
    }

    /**
     * 复制行
     * @throws Exception 
     */
    public static void copyRow(Row srcRow, Row desRow, boolean copyValueFlag) throws Exception {
        copyRow(srcRow, desRow, copyValueFlag, true, null);
    }

    /**
     * 复制行
     * @throws Exception 
     */
    public static void copyRow(Row srcRow, Row desRow, StyleMapping mapping) throws Exception {
        copyRow(srcRow, desRow, true, true, mapping);
    }

    /**
     * 复制行
     * @throws Exception 
     */
    public static void copyRow(Row srcRow, Row desRow, boolean copyStyleFlag, StyleMapping mapping) throws Exception {
        copyRow(srcRow, desRow, true, copyStyleFlag, mapping);
    }

    /**
     * 复制行
     * @throws Exception 
     */
    public static void copyRow(Row srcRow, Row desRow,boolean copyValueFlag, boolean copyStyleFlag, StyleMapping mapping) throws Exception {
        Iterator<Cell> it = srcRow.cellIterator();
        while (it.hasNext()) {
            Cell srcCell = it.next();
            Cell desCell = desRow.createCell(srcCell.getColumnIndex());
            copyCell(srcCell, desCell, copyValueFlag, copyStyleFlag, mapping);
        }
    }

    /**
     * 复制区域（合并单元格）
     */
    public static void copyMergedRegion(Sheet srcSheet, Sheet desSheet) {
        int sheetMergerCount = srcSheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergerCount; i++) {
            desSheet.addMergedRegion(srcSheet.getMergedRegion(i));
            CellRangeAddress cellRangeAddress = srcSheet.getMergedRegion(i);
        }
    }

    /**
     * 复制单元格，复制数据，如果同文件，复制样式，不同文件则不复制样式
     * @throws Exception 
     */
    public static void copyCell(Cell srcCell, Cell desCell) throws Exception {
        copyCell(srcCell, desCell, true, true,null);
    }

    /**
     * 复制单元格， 如果同文件，复制样式，不同文件则不复制样式
     * @param copyValueFlag 控制是否复制数据
     * @throws Exception 
     */
    public static void copyCell(Cell srcCell, Cell desCell, boolean copyValueFlag) throws Exception {
        copyCell(srcCell, desCell, copyValueFlag, true, null);
    }

    /**
     * 复制单元格，复制数据,复制样式
     * @param mapping       不同文件间复制时，如果要复制样式，必传，否则不复制样式
     * @throws Exception 
     */
    public static void copyCell(Cell srcCell, Cell desCell,  StyleMapping mapping) throws Exception {
        copyCell(srcCell, desCell, true, true, mapping);
    }

    /**
     * 复制单元格，复制数据
     * @param copyStyleFlag 控制是否复制样式
     * @param mapping       不同文件间复制时，如果要复制样式，必传，否则不复制样式
     * @throws Exception 
     */
    public static void copyCell(Cell srcCell, Cell desCell, boolean copyStyleFlag, StyleMapping mapping) throws Exception {
        copyCell(srcCell, desCell, true, copyStyleFlag, mapping);
    }

    /**
     * 复制单元格
     * @param copyValueFlag 控制是否复制单元格的内容
     * @param copyStyleFlag 控制是否复制样式
     * @param mapping 不同文件间复制时，如果需要连带样式复制，必传，否则不复制样式
     * @throws Exception 
     */
    public static void copyCell(Cell srcCell, Cell desCell, boolean copyValueFlag, boolean copyStyleFlag, StyleMapping mapping) throws Exception {
        Workbook srcBook = srcCell.getSheet().getWorkbook();
        Workbook desBook = desCell.getSheet().getWorkbook();

        //复制样式
        //如果是同一个excel文件内，连带样式一起复制
        if (srcBook == desBook && copyStyleFlag) {
            //同文件，复制引用
            desCell.setCellStyle(srcCell.getCellStyle());
        } else if (copyStyleFlag) {
           //不同文件，通过映射关系复制
            if (null != mapping) {
                short desIndex = mapping.desIndex(srcCell.getCellStyle().getIndex());
                desCell.setCellStyle(desBook.getCellStyleAt(desIndex));
            }
        }

        //复制评论
        if (srcCell.getCellComment() != null) {
            desCell.setCellComment(srcCell.getCellComment());
        }

        
        //复制内容
        desCell.setCellType(srcCell.getCellType());

        if (copyValueFlag) {
            switch (srcCell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    desCell.setCellValue(srcCell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    desCell.setCellValue(srcCell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    desCell.setCellFormula(srcCell.getCellFormula());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    desCell.setCellValue(srcCell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    desCell.setCellValue(srcCell.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_BLANK:
                    //nothing to do
                    break;
                default:
                    break;
            }
        }

    }


    /**
     * 把一个excel中的styleTable复制到另一个excel中<br>
     * 如果是同一个excel文件，就不用复制styleTable了
     * @return StyleMapping 两个文件中styleTable的映射关系
     * @throws Exception 
     * @see StyleMapping
     */
    public static StyleMapping copyCellStyle(Workbook srcBook, Workbook desBook) throws Exception{
        if (null == srcBook || null == desBook) {
            throw new Exception("源excel 或 目标excel 不存在");
        }
        if (srcBook.equals(desBook)) {
            throw new Exception("不要使用此方法在同一个文件中copy style，同一个excel中复制sheet不需要copy Style");
        }
        if ((srcBook instanceof HSSFWorkbook && desBook instanceof XSSFWorkbook) ||
                (srcBook instanceof XSSFWorkbook && desBook instanceof HSSFWorkbook)) {
            throw new Exception("不支持在不同的版本的excel中复制样式）");
        }

        short[] src2des = new short[srcBook.getNumCellStyles()];
        short[] des2src = new short[desBook.getNumCellStyles() + srcBook.getNumCellStyles()];

        for(short i=0;i<srcBook.getNumCellStyles();i++){
            //建立双向映射
            CellStyle srcStyle = srcBook.getCellStyleAt(i);
            CellStyle desStyle = desBook.createCellStyle();
            src2des[srcStyle.getIndex()] = desStyle.getIndex();
            des2src[desStyle.getIndex()] = srcStyle.getIndex();

            //复制样式
            desStyle.cloneStyleFrom(srcStyle);
        }


        return new StyleMapping(des2src, src2des);
    }
    
    
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
		if(null == cell){
			return "";
		}else{
			if (cell.getCellType() == XSSFCell.CELL_TYPE_BOOLEAN) {
				return String.valueOf(cell.getBooleanCellValue());
			} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
				DecimalFormat df = new DecimalFormat("0");
				String cellText = df.format(cell.getNumericCellValue());
				return cellText;
			} else if (cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
				return "";
			} else {
				return String.valueOf(cell.getStringCellValue());
			}
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
			} else if (cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
				return "";
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
	
    

    /**
     * 存放两个excel文件中的styleTable的映射关系，以便于在复制表格时，在目标文件中获取到对应的样式
     */
    public static class StyleMapping {
        /**
         *
         */
        private short[] des2srcIndexMapping;
        /**
         *
         */
        private short[] src2desIndexMapping;

        /**
         * 不允许其他类创建此类型对象
         */
        private StyleMapping() {
        }

        public StyleMapping(short[] des2srcIndexMapping, short[] src2desIndexMapping) {
            this.des2srcIndexMapping = des2srcIndexMapping;
            this.src2desIndexMapping = src2desIndexMapping;
        }

        public short srcIndex(short desIndex) throws Exception {
            if (desIndex < 0 || desIndex >= this.des2srcIndexMapping.length) {
                throw new Exception("索引越界：源文件styleNum=" + this.des2srcIndexMapping.length + " 访问位置=" + desIndex);
            }
            return this.des2srcIndexMapping[desIndex];
        }

        /**
         * 根据源文件的style的index,获取目标文件的style的index
         * @param srcIndex 源excel中style的index
         * @return desIndex 目标excel中style的index
         * @throws Exception 
         */
        public short desIndex(short srcIndex) throws Exception {
            if (srcIndex < 0 || srcIndex >= this.src2desIndexMapping.length) {
                throw new Exception("索引越界：源文件styleNum=" + this.src2desIndexMapping.length + " 访问位置=" + srcIndex);
            }

            return this.src2desIndexMapping[srcIndex];
        }
    }


    public static void main(String args[]) throws Exception{
		
		String srcExcelPath = "C:\\Users\\lipf07529\\Desktop\\test\\HUNDSUN银行统一支付平台V4.0-收单业务对外发布-服务接口V0.0.0.2.xlsx";
		String destExcelPath = "C:\\Users\\lipf07529\\Desktop\\test\\HUNDSUN银行统一支付平台V4.0-收单业务对外发布-服务接口V0.0.0.3.xlsx";
		InputStream in = new FileInputStream(srcExcelPath);
		XSSFWorkbook book = new XSSFWorkbook(in);
		
		XSSFSheet sheet = book.getSheet("example");
		
		if(null != sheet){
			String value =ExcelUtils.getValueFromSheet(sheet, 2, 2);
			System.out.println(value);
		}
		
		XSSFSheet newSheet = book.createSheet("新增交易");
		ExcelUtils.copySheet(sheet, newSheet);
		
//		newSheet = book.cloneSheet(9);
		
		OutputStream out = new FileOutputStream(destExcelPath);
		book.write(out);
		out.close();
		in.close();
		
	}
}




