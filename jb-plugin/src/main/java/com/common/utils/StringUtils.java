package com.common.utils;

import java.util.HashMap;
import java.util.Map;

import com.pojo.format.UPPSPojoField;

public class StringUtils {
	
	public static boolean isBlank(String str){
		
		boolean flag = false;
		if("".equals(str)||null == str){
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 
	 * @param str
	 * @param length
	 * @param fixChar
	 * @return
	 */
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

	/**
	 * 去空格，包括字符串中间的空格
	 * @param str
	 * @return
	 */
	public static String removeBlank(String str){
		
		String newStr = str.trim();
		newStr = newStr.replace(" ", "");
		return newStr;
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String getLength(String str){
		String length = null;
		
//		@Length(max = 8 message = "对账开始日期")
//		int startIndex = str.indexOf("max = ")+"max = ".length();
//		int endIndex = getNextIndex(str,startIndex," ");
//		//长度
//		length = str.substring(startIndex, endIndex).trim();
		
		int value1 = getNextDigit(str,"max =");
		length = ""+value1;
		return length;
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static Map<String,String> getBigDecimalLength(String str){
		
		//@Digits(maxInteger = 16, maxFraction = 2, message = "交易金额超限")
		
		Map<String,String> map = new HashMap<String,String>();
		
		int lengthStart = str.indexOf("maxInteger =")+"maxInteger =".length()+1;
		int lengthEnd = str.indexOf(",");
		
		int precisionStart = str.indexOf("maxFraction =")+"maxFraction =".length()+1;
		int precisionEnd = str.lastIndexOf(",");
		String length = str.substring(lengthStart,lengthEnd);
		String precision = str.substring(precisionStart,precisionEnd);
		map.put(length, precision);
		return map;
		
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static void getBigDecimalLength(String str,UPPSPojoField uPPSPojoField){
		
		//@Digits(maxInteger = 16, maxFraction = 2, message = "交易金额超限")
		
//		int lengthStart = str.indexOf("maxInteger =")+"maxInteger =".length()+1;
//		int lengthEnd = str.indexOf(",");
		
//		int precisionStart = str.indexOf("maxFraction =")+"maxFraction =".length()+1;
//		int precisionEnd = str.lastIndexOf(",");
//		String length = str.substring(lengthStart,lengthEnd);
//		String precision = str.substring(precisionStart,precisionEnd);
		
		int value1 = getNextDigit(str,"maxInteger =");
		int value2 = getNextDigit(str,"maxFraction =");
		String length = ""+value1;
		String precision = ""+value2;
		uPPSPojoField.setLength(length);
		uPPSPojoField.setPrecision(precision);
		
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String getCheckClassName(String str){
		
		//@CheckWith(value = CurrencyCheck.class, message = "币种不合法")
		//@CheckWith(value = EnumCashFlag.Check.class, message = "现转标志不合法")
		System.out.println(str);
		return str.substring(str.indexOf("value =")+"value =".length()+1, str.indexOf(","));
		
	}
	
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static void testLength(String str){
		
		//@Digits(maxInteger = 16, maxFraction = 2, message = "交易金额超限")
		
//		int lengthStart = str.indexOf("maxInteger =")+"maxInteger =".length()+1;
//		int lengthEnd = str.indexOf(",");
		
//		int precisionStart = str.indexOf("maxFraction =")+"maxFraction =".length()+1;
//		int precisionEnd = str.lastIndexOf(",");
//		String length = str.substring(lengthStart,lengthEnd);
//		String precision = str.substring(precisionStart,precisionEnd);
		
		int value1 = getNextDigit(str,"maxInteger =");
		int value2 = getNextDigit(str,"maxFraction =");
		String length = ""+value1;
		String precision = ""+value2;
		System.out.println("length=["+length+"] precision=["+precision+"]");
		
		String str1 = "@CheckWith(value = EnumCashFlag.Check.class, message = \"现转标志不合法\")";
		String name = getCheckClassName(str1);
		System.out.println(name);
	}
	
	
	public static void main(String args[]){
		
//		String lengthStr = "@Length(max = 1)";
//		String length = getLength(lengthStr);
//		System.out.println(length);
		
		String newlengthStr = "@Digits(maxInteger = 16, maxFraction = 2, message = \"交易金额超限\")";
		
		testLength(newlengthStr);
		
//		int value = getNextDigit(newlengthStr,"maxInteger =");
//		
//		System.out.println(value);
		
//		int startIndex = newlengthStr.indexOf("maxInteger =")+"maxInteger =".length();
//		System.out.println(startIndex);
//		int endIndex = getNextIndex(newlengthStr,startIndex,",");
//		System.out.println(endIndex);
//		System.out.println(newlengthStr.subSequence(startIndex, endIndex));
	
	
	}
	
	
	public static int getNextIndex(String str,int startIndex,String matchStr){
		
		int index = startIndex +str.substring(startIndex).indexOf(matchStr);
		return index;
	}

	
	/**
	 * 获取字符串中startIndex后的纯数字串。如果无，则返回-1
	 * @param str
	 * @param startIndex
	 * @return
	 */
	public static int getNextDigit(String str,String matchStr){
		
		int value = getNextDigit(str,str.indexOf(matchStr)+matchStr.length());
		return value;
	}
	
	
	/**
	 * 获取字符串中startIndex后的纯数字串。如果无，则返回-1
	 * @param str
	 * @param startIndex
	 * @return
	 */
	public static int getNextDigit(String str,int startIndex){
		
		int value = -1;
		String digitStr = "";
		boolean flag = false;
		for(int i= startIndex;i<= str.length();i++){
			
			//如果存在一位是数字，则记录，如果
			if(Character.isDigit(str.charAt(i))){
				digitStr += ""+str.charAt(i);
				flag = true;
			}else{
				
				if(flag){
					//已经截止了，不需要再进一步截取。直接break
					break;
				}else{
					//继续向下读取
				}
				
			}
		}
		if(!StringUtils.isBlank(digitStr)){
			value = Integer.parseInt(digitStr);
		}
		return value;
	}
	
}
