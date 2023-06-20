package com.weavx.web.model;

public class Fund {
	private int fundId;
	private int langId;
	private String fundLabel;
	private String fundDescription;
	private long validFrom;
	private long validTo;
	
	public Fund(int fundId, int langId, String fundLabel, String fundDescription) {
		super();
		this.fundId = fundId;
		this.langId = langId;
		this.fundLabel = fundLabel;
		this.fundDescription = fundDescription;
	}
	public int getFundId() {
		return fundId;
	}
	public void setFundId(int fundId) {
		this.fundId = fundId;
	}
	public int getLangId() {
		return langId;
	}
	public void setLangId(int langId) {
		this.langId = langId;
	}
	public String getFundLabel() {
		return fundLabel;
	}
	public void setFundLabel(String fundLabel) {
		this.fundLabel = fundLabel;
	}
	public String getFundDescription() {
		return fundDescription;
	}
	public void setFundDescription(String fundDescription) {
		this.fundDescription = fundDescription;
	}
	public long getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(long validFrom) {
		this.validFrom = validFrom;
	}
	public long getValidTo() {
		return validTo;
	}
	public void setValidTo(long validTo) {
		this.validTo = validTo;
	}
	
	
}
