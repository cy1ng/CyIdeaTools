package com.pojo.format;

public class UPPSPojoField {

	private String name;
	private String type;
	private String length;
	private String precision;//精度
	private Boolean isNotNull;
	private String modelName;
	private String upperNode;
	private String desc;
	private String detailDesc;
	private String checkClassName;
	private boolean isReq = false;//请求报文还是应答报文
	
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
	
	public boolean isReq() {
		return isReq;
	}

	public void setReq(boolean isReq) {
		this.isReq = isReq;
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

		return "name = [" + name + "] type = [" + type + "] length=["+length+"] precision = ["+precision+"] desc = [" + desc
				+ "] detailDesc = [" + detailDesc + "] checkClassName = [" +checkClassName+ "] isNotNull = ["+isNotNull+"] isReq = ["+isReq+"]";
	}
}