package com.weavx.web.model;

public class EventFundSettings {
	
	private int id;
	private int fundId;
	private int allowDays;
	private long startDate;
	private long endDate;
	private boolean signatureRequired;
	
	public boolean isSignatureRequired() {
		return signatureRequired;
	}
	public void setSignatureRequired(boolean signatureRequired) {
		this.signatureRequired = signatureRequired;
	}
	public String getSignatureDocumentId() {
		return signatureDocumentId;
	}
	public void setSignatureDocumentId(String signatureDocumentId) {
		this.signatureDocumentId = signatureDocumentId;
	}
	private String signatureDocumentId;
	
	public int getId() {
		return id;
	}
	public void setId(int idConfig) {
		this.id = idConfig;
	}
	public int getFundId() {
		return fundId;
	}
	public void setFundId(int fundId) {
		this.fundId = fundId;
	}
	public int getAllowDays() {
		return allowDays;
	}
	public void setAllowDays(int allowDays) {
		this.allowDays = allowDays;
	}
	public long getStartDate() {
		return startDate;
	}
	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}
	public long getEndDate() {
		return endDate;
	}
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}	
	
	
}
