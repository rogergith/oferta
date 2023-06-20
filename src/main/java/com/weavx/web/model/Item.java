package com.weavx.web.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Item {
	long id;
	long customerId;
	long applicationId;
	long date;
	String language;
	String status;
	double amount;
	String source_type;
	String source;
	int countryId;
	String countryName;
	int continentId;
	String continentName;
	int scheduled;
	String campaing;
	String medium;
	List<FundDash> funds;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	
	public long getApplicationId() {
		return applicationId;
	}
	
	public void setApplicationId(long applicationId) {
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
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public String getSource_type() {
		return source_type;
	}
	
	public void setSource_type(String source_type) {
		this.source_type = source_type;
	}
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}	
			
	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public int getContinentId() {
		return continentId;
	}

	public void setContinentId(int continentId) {
		this.continentId = continentId;
	}

	public String getContinentName() {
		return continentName;
	}

	public void setContinentName(String continentName) {
		this.continentName = continentName;
	}

	public int getScheduled() {
		return scheduled;
	}
	
	public void setScheduled(Boolean scheduled) {
		this.scheduled = scheduled == false ? 0 : 1;
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
	
	public List<FundDash> getFunds() {
		return funds;
	}
	
	public void setFunds(List<FundDash> funds) {
		this.funds = funds;
	}
	
	@Override
	public String toString() {
		return "Item [id=" + id + ", customerId=" + customerId + ", applicationId=" + applicationId + ", date=" + date
				+ ", language=" + language + ", status=" + status + ", amount=" + amount + ", source_type="
				+ source_type + ", source=" + source + ", countryId=" + countryId + ", countryName=" + countryName
				+ ", continentId=" + continentId + ", continentName=" + continentName + ", scheduled=" + scheduled
				+ ", campaing=" + campaing + ", medium=" + medium + ", funds=" + funds + "]";
	}

	public String getDateAsDate() {	
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(new Date(this.date));
	}
	
	public String getDateAsWeek() {	
		
		DateFormat df = new SimpleDateFormat("yyyy-ww");
		return df.format(new Date(this.date));
	}
	
	public String getDateAsHours() {	
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH");
		return df.format(new Date(this.date));
	}
	

}
