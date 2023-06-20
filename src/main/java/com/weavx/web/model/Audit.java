package com.weavx.web.model;

public class Audit {
	private int id;
	private int customerId;
	private int applicationId;
	private int auditLevelId;
	private String auditLevel;
	private String what;
	private String who;
	private String ipAddress;
	private String userAgent;
	private String source;
	private String data;
	private long fromDate;
	private long toDate;
	private long createdAt;
	
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
	public int getAuditLevelId() {
		return auditLevelId;
	}
	public void setAuditLevelId(int auditLevelId) {
		this.auditLevelId = auditLevelId;
	}
	public String getAuditLevel() {
		return auditLevel;
	}
	public void setAuditLevel(String auditLevel) {
		this.auditLevel = auditLevel;
	}
	public String getWhat() {
		return what;
	}
	public void setWhat(String what) {
		this.what = what;
	}
	public String getWho() {
		return who;
	}
	public void setWho(String who) {
		this.who = who;
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
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public long getFromDate() {
		return fromDate;
	}
	public void setFromDate(long fromDate) {
		this.fromDate = fromDate;
	}
	public long getToDate() {
		return toDate;
	}
	public void setToDate(long toDate) {
		this.toDate = toDate;
	}
	
	public long getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}
	
	
	
}