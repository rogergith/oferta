package com.weavx.web.model;

public class ListFund {
	private int id;
	private String businessCode;
	private boolean fundDefault;
	private long validFrom;
	private long validTo;
	private String name;

	public ListFund(int id, String businessCode,boolean fundDefault, long validFrom, long validTo, String name) {
		super();
		this.id = id;
		this.businessCode = businessCode;
		this.fundDefault = fundDefault;
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBusinessCode() {
		return businessCode;
	}
	public void setbusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}
	public boolean isFundDefault() {
		return fundDefault;
	}
	public void setFundDefault(boolean fundDefault) {
		this.fundDefault = fundDefault;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "ListFund [id=" + id + ", businessCode=" + businessCode + ", fundDefault=" + fundDefault + ", validFrom="
				+ validFrom + ", validTo=" + validTo + ", name=" + name + "]";
	}
	
}