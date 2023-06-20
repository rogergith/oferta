package com.weavx.web.model;

public class FundLanguage {
	
	int fundId;
	int langugId;
	
	public FundLanguage(int fundId, int langugeId) {
		super();
		this.fundId = fundId;
		this.langugId = langugeId;
	}

	public int getFundId() {
		return fundId;
	}

	public void setFundId(int propertyId) {
		this.fundId = propertyId;
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
		FundLanguage o2 = (FundLanguage) o;
		
		return this.getFundId() == o2.getFundId() && 
				this.getLangugeId() == o2.getLangugeId();
	}
	
	

}
