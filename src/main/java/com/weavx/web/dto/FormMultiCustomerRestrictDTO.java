package com.weavx.web.dto;

import org.springframework.web.multipart.MultipartFile;

public class FormMultiCustomerRestrictDTO {
	
	
	MultipartFile customers;

	
	public MultipartFile getCustomers() {
		return customers;
	}
	public void setCustomers(MultipartFile customers) {
		this.customers = customers;
	}
	

}
