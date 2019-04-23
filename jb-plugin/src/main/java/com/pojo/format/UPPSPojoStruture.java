package com.pojo.format;

import java.util.List;

public class UPPSPojoStruture {
	
	private String transCode;
	private String transName;
	private String modelName;
	
	private List<UPPSPojoField> reqPojoFieldList;
	
	private List<UPPSPojoField> respPojoFieldList;

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

}
