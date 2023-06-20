package com.weavx.web.model;



public class Property {

	private int propertyId;
	private int langId;
	private int customerId;
	private String propertyValue;
	
	public Property(int propertyId, int langId, int customerId, String propertyValue) {
		super();
		this.propertyId = propertyId;
		this.langId = langId;
		this.customerId = customerId;
		this.propertyValue = propertyValue;
	}

	public int getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}

	public int getLangId() {
		return langId;
	}

	public void setLangId(int langId) {
		this.langId = langId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	
	
}