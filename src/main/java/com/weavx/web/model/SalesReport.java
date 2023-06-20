package com.weavx.web.model;

public class SalesReport {
	
	private Number userId;
	private String agentEmail;
	private String userName;
	private Number userAgentId;
	private String userLastname;
	private String userEmail;
	private String countryName;
	private String countryCode;
	private String phoneNumber;
	private String preferredLanguage;
	private String agentAssigned;
	private Number daysAssigned;
	private Number date;
	private String dateFormat;
	private String timeFormat;
	private String sales;
	private String SaleStatus;
	private String leadType;
	private Number leadTypeId;
	
	public Number getUserId() {
		return userId;
	}
	public void setUserId(Number userId) {
		this.userId = userId;
	}
	public String getAgentEmail() {
		return agentEmail;
	}
	public void setAgentEmail(String agentEmail) {
		this.agentEmail = agentEmail;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Number getUserAgentId() {
		return userAgentId;
	}
	public void setUserAgentId(Number userAgentId) {
		this.userAgentId = userAgentId;
	}
	public String getUserLastname() {
		return userLastname;
	}
	public void setUserLastname(String userLastname) {
		this.userLastname = userLastname;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPreferredLanguage() {
		return preferredLanguage;
	}
	public void setPreferredLanguage(String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}
	public String getAgentAssigned() {
		return agentAssigned;
	}
	public void setAgentAssigned(String agentAssigned) {
		this.agentAssigned = agentAssigned;
	}
	public Number getDaysAssigned() {
		return daysAssigned;
	}
	public void setDaysAssigned(Number daysAssigned) {
		this.daysAssigned = daysAssigned;
	}
	public Number getDate() {
		return date;
	}
	public void setDate(Number date) {
		this.date = date;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public String getTimeFormat() {
		return timeFormat;
	}
	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}
	public String getSales() {
		return sales;
	}
	public void setSales(String sales) {
		this.sales = sales;
	}
	public String getSaleStatus() {
		return SaleStatus;
	}
	public void setSaleStatus(String saleStatus) {
		SaleStatus = saleStatus;
	}

	public String getLeadType() {
		return leadType;
	}
	public void setLeadType(String leadType) {
		this.leadType = leadType;
	}
	public Number getLeadTypeId() {
		return leadTypeId;
	}
	public void setLeadTypeId(Number leadTypeId) {
		this.leadTypeId = leadTypeId;
	}
	
	@Override
	public String toString() {
		return "SalesReport [userId=" + userId + ", agentEmail=" + agentEmail + ", userName=" + userName
				+ ", userAgentId=" + userAgentId + ", userLastname=" + userLastname + ", userEmail=" + userEmail
				+ ", countryName=" + countryName + ", countryCode=" + countryCode + ", phoneNumber=" + phoneNumber
				+ ", preferredLanguage=" + preferredLanguage + ", agentAssigned=" + agentAssigned + ", daysAssigned="
				+ daysAssigned + ", date=" + date + ", dateFormat=" + dateFormat + ", timeFormat=" + timeFormat
				+ ", sales=" + sales + ", SaleStatus=" + SaleStatus + ", leadType=" + leadType + ", leadTypeId="
				+ leadTypeId + "]";
	}
	
}
