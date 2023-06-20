package com.weavx.web.model;

public class TxUserData {
	
	private String name;
	private String lastname;
	private Number country;
	private String stateText;
	private String cityText;
	private String address;
	private String postcode;
	private String email;
	private String phone;
	private int sourceCode;
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public Number getCountry() {
		return country;
	}
	public void setCountry(Number country) {
		this.country = country;
	}
	public String getStateText() {
		return stateText;
	}
	public void setStateText(String stateText) {
		this.stateText = stateText;
	}
	public String getCityText() {
		return cityText;
	}
	public void setCityText(String cityText) {
		this.cityText = cityText;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(int sourceCode) {
		this.sourceCode = sourceCode;
	}
	@Override
	public String toString() {
		return "TxUserData [name=" + name + ", lastname=" + lastname + ", country=" + country + ", stateText="
				+ stateText + ", cityText=" + cityText + ", address=" + address + ", postcode=" + postcode + ", email="
				+ email + ", phone=" + phone + ", sourceCode=" + sourceCode + "]";
	}
	
}