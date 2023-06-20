package com.weavx.web.model;

public class UserAccess {
	
	private int id;
	private String email;
	private Number createdAt;
	private String customerName;
	private String applicationName;
	private int customerId;
	private int  applicationId;
	private String ipAddress;
	private String userAgent;
	private int tokenStatusId;
	private String tokenStatusName;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Number getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Number createdAt) {
		this.createdAt = createdAt;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
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
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public int getTokenStatusId() {
		return tokenStatusId;
	}
	public void setTokenStatusId(int tokenStatusId) {
		this.tokenStatusId = tokenStatusId;
	}
	public String getTokenStatusName() {
		return tokenStatusName;
	}
	public void setTokenStatusName(String tokenStatusName) {
		this.tokenStatusName = tokenStatusName;
	}
	
	
	
}
