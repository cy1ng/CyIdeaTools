package com.pojo.format;

import java.util.List;

public class UPPSPojoStruture {
	
	private String transCode;
	private String transName;
	private String modelName;
	
	private List<UPPSPojoField> reqPojoFieldList;
	
	private List<UPPSPojoField> respPojoFieldList;
	
	private List<UPPSPojoStruture> uPPSPojoStrutureList;
	
	//List<JavaBean>  Object<JavaBean> 2种情况

	public List<UPPSPojoStruture> getUPPSPojoStrutureList() {
		return uPPSPojoStrutureList;
	}

	public void setUPPSPojoStrutureList(List<UPPSPojoStruture> uPPSPojoStrutureList) {
		this.uPPSPojoStrutureList = uPPSPojoStrutureList;
	}

	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	public String getTransName() {
		return transName;
	}

	public void setTransName(String transName) {
		this.transName = transName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public List<UPPSPojoField> getReqPojoFieldList() {
		return reqPojoFieldList;
	}

	public void setReqPojoFieldList(List<UPPSPojoField> reqPojoFieldList) {
		this.reqPojoFieldList = reqPojoFieldList;
	}

	public List<UPPSPojoField> getRespPojoFieldList() {
		return respPojoFieldList;
	}

	public void setRespPojoFieldList(List<UPPSPojoField> respPojoFieldList) {
		this.respPojoFieldList = respPojoFieldList;
	}

	public String toString(){
		return "transName = ["+transName+"] transCode = ["+transCode+"]"+"\n ReqPojo = \n" +printPojoFieldList(reqPojoFieldList) +"\n RespPojo = \n" +printPojoFieldList(respPojoFieldList);
	}
	
	public String printPojoFieldList(List<UPPSPojoField> uPPSPojoFieldList){
		
		if(null == uPPSPojoFieldList){
			return "{\n};";
		}else{
			String str ="{\n";
			for(UPPSPojoField uPPSPojoField:uPPSPojoFieldList){
				str += uPPSPojoField.toString()+"\n";
			}
			str += "\n};";
			return str;
			
		}
		
	}
	
	
}
