package com.weavx.web.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserInfo implements Cloneable{
	private long id;
	private String email;
	private Timestamp createdAt;
	private String firstName;
	private String lastName;
	private String password;
	private int genderId;
	private Date birthDate;
	private String profileImage;
	private int countryId;
	private String country;
	private int statedId;
	private String stateText;
	private int cityId;
	private String cityText;
	private String address;
	private String postalCode;
	private String phoneNumber;
	private String resetPassword;
	private String totalAmount;
	private String tags;
	private List<Map<String, Object>> activitiesList;
	private String sourceCode;
	private int preferredLanguage;
	private Integer statusSales;

	public UserInfo(long id, String email, Timestamp createdAt) {
		super();
		this.id = id;
		this.email = email;
		this.createdAt = createdAt;
	}
	public UserInfo(String email) {
		super();
		this.id = -1;
		this.email = email;
	}
	public UserInfo() {
		super();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getGenderId() {
		return genderId;
	}
	public void setGenderId(int genderId) {
		this.genderId = genderId;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	public int getStatedId() {
		return statedId;
	}
	public void setStatedId(int statedId) {
		this.statedId = statedId;
	}
	public String getStateText() {
		return stateText;
	}
	public void setStateText(String stateText) {
		this.stateText = stateText;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
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
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getResetPassword() {
		return resetPassword;
	}
	public void setResetPassword(String resetPassword) {
		this.resetPassword = resetPassword;
	}

	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public List<Map<String, Object>> getActivitiesList() {
		return activitiesList;
	}
	public void setActivitiesList(List<Map<String, Object>> activitiesList) {
		this.activitiesList = activitiesList;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	public int getPreferredLanguage() {
		return preferredLanguage;
	}
	public void setPreferredLanguage(int preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	public Integer getStatusSales() {
		return statusSales;
	}
	public void setStatusSales(Integer statusSales) {
		this.statusSales = statusSales;
	}
	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", email=" + email + ", createdAt=" + createdAt + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", password=" + password + ", genderId=" + genderId + ", birthDate="
				+ birthDate + ", profileImage=" + profileImage + ", countryId=" + countryId + ", country=" + country
				+ ", statedId=" + statedId + ", stateText=" + stateText + ", cityId=" + cityId + ", cityText="
				+ cityText + ", address=" + address + ", postalCode=" + postalCode + ", phoneNumber=" + phoneNumber
				+ ", resetPassword=" + resetPassword + ", totalAmount=" + totalAmount + ", tags=" + tags
				+ ", activitiesList=" + activitiesList + ", sourceCode=" + sourceCode + ", preferredLanguage="
				+ preferredLanguage + ", statusSales=" + statusSales + "]";
	}


}
