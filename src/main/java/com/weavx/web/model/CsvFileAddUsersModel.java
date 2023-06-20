package com.weavx.web.model;

public class CsvFileAddUsersModel {

	String fullname;
	String email;
	String mobilephone;
	String sdigroup;

	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	public String getSdigroup() {
		return sdigroup;
	}
	public void setSdigroup(String sdigroup) {
		this.sdigroup = sdigroup;
	}
	@Override
	public String toString() {
		return "CsvFileAddUsersModel [fullname=" + fullname + ", email=" + email + ", mobilephone=" + mobilephone
				+ ", sdigroup=" + sdigroup + "]";
	}

}
