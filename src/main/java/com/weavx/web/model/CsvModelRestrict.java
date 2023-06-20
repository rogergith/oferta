package com.weavx.web.model;

public class CsvModelRestrict {

	String email;
	String name;
	String lastname;
	

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "CsvFileModel [email=" + email  + "]";
	}

	
}
