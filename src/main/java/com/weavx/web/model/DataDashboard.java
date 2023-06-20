package com.weavx.web.model;

import java.util.ArrayList;

public class DataDashboard {
	
	private int id;
	private int customerId;
	private int applicationId;
	private long date;
	private String language;
	private String status;
	private Number amount;
	private String source;
	private String campaing;
	private String medium;
	private ArrayList<FundDash> fundsDash;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public int getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Number getAmount() {
		return amount;
	}
	public void setAmount(Number amount) {
		this.amount = amount;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getCampaing() {
		return campaing;
	}
	public void setCampaing(String campaing) {
		this.campaing = campaing;
	}
	public String getMedium() {
		return medium;
	}
	public void setMedium(String medium) {
		this.medium = medium;
	}
	public ArrayList<FundDash> getFundsDash() {
		return fundsDash;
	}
	public void setFundsDash(ArrayList<FundDash> fundsDash) {
		this.fundsDash = fundsDash;
	}


}
