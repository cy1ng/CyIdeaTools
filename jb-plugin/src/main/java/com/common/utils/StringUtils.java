package com.common.utils;

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

}
