package com.weavx.web.model;

public class CsvFileModel {

	String email;
	String name;
	String lastname;
	String payment_type;
	String phone;
	String payment_details;
	String lang;
	

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPayment_type() {
		return payment_type;
	}
	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}
	public String getPayment_details() {
		return payment_details;
	}
	public void setPayment_details(String payment_details) {
		this.payment_details = payment_details;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	@Override
	public String toString() {
		return "CsvFileModel [\n\temail = " + email + ", \n\tname = " + name + ", \n\tlastname = " + lastname
				+ ", \n\tpayment_type = " + payment_type + ", \n\tphone = " + phone + ", \n\tpayment_details = "
				+ payment_details + ", \n\tlang = " + lang + "\n]";
	}	
}
