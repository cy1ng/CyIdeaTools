package com.pojo.format;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.common.utils.StringUtils;

public class UPPSPojoCommonStr {

	/**
	 * 生成引入包注释
	 * @param pojoFileName
	 * @param transName
	 * @return
	 */
	public static String getImportStr(String packagePath,Boolean isReq){
		
		String reqOrRespStr = "";
		if(isReq){
			reqOrRespStr = "req";
		}else{
			reqOrRespStr = "resp";
		}
		
		String str = "package "+packagePath+"."+reqOrRespStr+";\n";
		
		str += "\n"
			+"import net.sf.oval.constraint.CheckWith;\n"
			+"import net.sf.oval.constraint.Digits;\n"
			+"import net.sf.oval.constraint.Length;\n"
			+"import net.sf.oval.constraint.NotNull;\n"
			+"\n"
			+"import java.io.Serializable;\n"
			+"import java.math.BigDecimal;\n"
			+"import java.util.List;\n";
		
		return str;
				
				
	}
	
	/*
	 * 生成接口类注释
	 */
	public static String getPojoDesc(String author,String transName,boolean isReq){
		 
		String reqOrRespStr = "";
		if(isReq){
			reqOrRespStr = "请求接口";
		}else{
			reqOrRespStr = "应答接口";
		}
		
		String dateStr = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date());
		String str = "/**\n"
                    +" * @author "+author+" "+author+"@hundsun.com\n"
                    +" * @description "+transName+reqOrRespStr+"\n"
                    +" * @date "+dateStr+"\n"
                    +" */\n";
		return str;
		
	}
	
	/**
	 * 
	 * @param transCode
	 * @param isReq
	 * @return
	 */
	public static String getClassDefine(String transCode,boolean isReq){
		
		String reqOrRespStr = "";
		if(isReq){
			reqOrRespStr = "Req";
		}else{
			reqOrRespStr = "Resp";
		}
		return "\n@Data\n"
			  +"public class "+reqOrRespStr+transCode+" implements Serializable {\n\n";
	}
	
	/**
	 * @param 根据属性定义类型生成Pojo的代码
	 * @return
	 */
	public static String getFieldStr(UPPSPojoField pojoField){
		
		String fieldName = pojoField.getName();
		String fieldType = pojoField.getType();
		String fieldLength = pojoField.getLength();
		String precision = pojoField.getPrecision();//精度
		Boolean isNotNull = pojoField.getIsNotNull();
		Boolean isReq = pojoField.isReq();
		
		
//		String upperNode = pojoField.getUpperNode();
		String checkClassName = pojoField.getCheckClassName();
		String desc = pojoField.getDesc();
//		String detailDesc = pojoField.getDetailDesc();
		
		
		String fieldStr ="    /**\n"
		                +"     * "+desc+"\n"
				        +"     */\n" ;
		
		String typeStr = "";
		if(isReq){
			
			//非空判断
			if(isNotNull) {
				fieldStr += "    @NotNullBlank(message = \""+desc+"\")\n";
			}else {
				//donothing
			}
			
			//长度校验
			if("CHAR".equalsIgnoreCase(fieldType)||"VARCHAR".equalsIgnoreCase(fieldType)){
				
				typeStr = "String";
				
				if(!StringUtils.isBlank(fieldLength)){
					fieldStr += "    @Length(max = "+fieldLength+", message = \""+desc+"\")\n";
				}else{
					//donothing
				}
				
			}else if("DOUBLE".equalsIgnoreCase(fieldType)||"NUMERIC".equals(fieldType)||"NUMBER".equals(fieldType)){
				
				typeStr = "BigDecimal";
				if(!StringUtils.isBlank(fieldLength)&&(!StringUtils.isBlank(precision))){
					fieldStr += "    @Digits(maxInteger = "+fieldLength+", maxFraction = "+precision+", message = \""+desc+"\")\n";
				}else{
					//donothing
				}
				
			}else if("INT".equals(fieldType)||"INTEGER".equals(fieldType)){
				typeStr = "Integer";
				//donothing
			}else{
				//donothing
			}
			
			//枚举值校验
			if(!StringUtils.isBlank(checkClassName)){
				fieldStr += "    @CheckWith(value = "+checkClassName+".class, message = \"请求报文格式错误，"+desc+"输入值不合法。\")\n";
			}
			
		}else {
			
			//长度校验
			if("CHAR".equalsIgnoreCase(fieldType)||"VARCHAR".equalsIgnoreCase(fieldType)){
				typeStr = "String";
			}else if("DOUBLE".equals(fieldType)){
				typeStr = "BigDecimal";
			}else if("INT".equals(fieldType)||"INTEGER".equals(fieldType)){
				typeStr = "Integer";
				//donothing
			}else{
				//donothing
			}
			
		}
		
		
		
		
		fieldStr += "    private "+typeStr+" "+fieldName+";\n";
		fieldStr +="\n";
		
		System.out.println(fieldStr);
		return fieldStr;
	}
	
	/**
	 * 生成pojo的所有属性
	 * @param pojoFieldList
	 * @return
	 */
	public static String getAllFieldStr(List<UPPSPojoField> pojoFieldList){
		
		String str = "";
		
		for(UPPSPojoField uPPSPojoField:pojoFieldList){
			str += getFieldStr(uPPSPojoField);
		}
		
		return str;
	}
	

	/**
	 * 
	 * @return
	 */
	public static String getEndClassStr(){
		
		return "\n}";
	}
}
