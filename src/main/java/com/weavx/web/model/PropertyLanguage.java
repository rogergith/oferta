package com.weavx.web.model;

public class PropertyLanguage {
	
	int propertyId;
	int langugId;
	
	public PropertyLanguage(int propertyId, int langugeId) {
		super();
		this.propertyId = propertyId;
		this.langugId = langugeId;
	}

	public int getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}

	public int getLangugeId() {
		return langugId;
	}

	public void setLangugeId(int langugeId) {
		this.langugId = langugeId;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		PropertyLanguage o2 = (PropertyLanguage) o;
		
		return this.getPropertyId() == o2.getPropertyId() && 
				this.getLangugeId() == o2.getLangugeId();
	}
	
	

}
