package com.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

/**
 * 测试用，用于打印出类中字段名-值
 * 
 * 要求：所有的字段必须提供get方法，get方法的命名规则：get + 字段名(首字母大写)
 * 如果字段没有get方法或者方法的名字不符合规则，则此字段的值不能显示输出。
 */
public class PrintBeanInfo {

	public static final String HUNDSUN_VERSION= "@system 9c公共系统模块   @version 1.0.0.1 @lastModiDate 20150909 @describe ";

	
	private static final Logger log = Logger.getLogger(PrintBeanInfo.class);
    /**
     * 打印Bean，仅针对数据库Bean 对象
     * 
     * @param aObj
     */
    public static Class<?> printBean(Object aObj,Class<?> _class,String printMsg){
    	if(_class==null){
    		log.info(printMsg);
    		return null;
    	}
    	Method[] methods = _class.getDeclaredMethods();
    	for(int i = 0;i<methods.length;i++){
    		if(methods[i].getName().startsWith("get")){
    		methods[i].setAccessible(true);
	    		Object object;
	    		try {
					object = methods[i].invoke(aObj);
					String n = methods[i].getName();
					printMsg +=" "+n.substring(3,n.length())+"="+object+",";
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
    		}
    	}
    	return printBean(aObj,_class.getSuperclass(),printMsg);
    }
    
    
    /**
     * bean对象打印
     */
    public static String getBeanPrintMsg(Object obj){
    	
    	StringBuffer printMsgBuff = new StringBuffer();
    	printMsgBuff.append("类名 ="+obj.getClass().getSimpleName()+" [");
    	Method[] methods = obj.getClass().getMethods();
    	for(int i = 0;i<methods.length;i++){
    		if(methods[i].getName().startsWith("get")){
    		methods[i].setAccessible(true);
	    		Object object;
	    		try {
					object = methods[i].invoke(obj);
					printMsgBuff.append(" "+methods[i].getName().substring(3,methods[i].getName().length())+"= ["+object+"]");
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
    		}
    	}
    	printMsgBuff.append(" ]");
    	return printMsgBuff.toString();
    }

}
