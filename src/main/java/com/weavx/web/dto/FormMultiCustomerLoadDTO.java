package com.weavx.web.dto;

import org.springframework.web.multipart.MultipartFile;

public class FormMultiCustomerLoadDTO {
	
	
	Number funds;
	Number amount;
	String source;
	String medium;
	MultipartFile customers;

	public Number getFunds() {
		return funds;
	}
	public void setFunds(Number funds) {
		this.funds = funds;
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
	public String getMedium() {
		return medium;
	}
	public void setMedium(String medium) {
		this.medium = medium;
	}
	public MultipartFile getCustomers() {
		return customers;
	}
	public void setCustomers(MultipartFile customers) {
		this.customers = customers;
	}
	
	
	
	
	
	
	
	
	
	
	

}
