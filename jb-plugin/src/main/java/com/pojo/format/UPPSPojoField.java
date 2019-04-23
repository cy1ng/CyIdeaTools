package com.pojo.format;

public class UPPSPojoField {

	String transCode;
	String transName;
	String name;
	String type;
	String length;
	String precision;//精度
	Boolean isNotNull;
	String modelName;
	String upperNode;
	String desc;
	String detailDesc;
	String checkClassName;
	boolean isReq = false;//请求报文还是应答报文
	
	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public Boolean getIsNotNull() {
		return isNotNull;
	}

	public void setIsNotNull(Boolean isNotNull) {
		this.isNotNull = isNotNull;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getUpperNode() {
		return upperNode;
	}

	public void setUpperNode(String upperNode) {
		this.upperNode = upperNode;
	}

	public String getCheckClassName() {
		return checkClassName;
	}

	public void setCheckClassName(String checkClassName) {
		this.checkClassName = checkClassName;
	}
	
	public String getTransName() {
		return transName;
	}

	public void setTransName(String transName) {
		this.transName = transName;
	}

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