package com.weavx.web.model;

public class CustomerNewUser {
	private int userId;
	private String firstName;
	private String lastName;
	private int genderId;
	private String birthDate;
	private String profileimage;
	private int stateId;
	private int cityId;
	private int customerId;
	private String stateText;
	private String citytext;
	private String password;
	private int countryId;
	private int id;
	private String email;
	private boolean payment;
	
	public boolean isPayment() {
		return payment;
	}
	public void setPayment(boolean payment) {
		this.payment = payment;
	}
	public CustomerNewUser(){}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
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
	public int getGenderId() {
		return genderId;
	}
	public void setGenderId(int genderId) {
		this.genderId = genderId;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getProfileimage() {
		return profileimage;
	}
	public void setProfileimage(String profileimage) {
		this.profileimage = profileimage;
	}
	public int getStateId() {
		return stateId;
	}
	public void setStateId(int stateId) {
		this.stateId = stateId;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getStateText() {
		return stateText;
	}
	public void setStateText(String stateText) {
		this.stateText = stateText;
	}
	public String getCitytext() {
		return citytext;
	}
	public void setCitytext(String citytext) {
		this.citytext = citytext;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public CustomerNewUser(int userId, String firstName, String lastName, int genderId, String birthDate,
			String profileimage, int stateId, int cityId, int customerId, String stateText, String citytext,
			String password, int countryId, int id, String email) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.genderId = genderId;
		this.birthDate = birthDate;
		this.profileimage = profileimage;
		this.stateId = stateId;
		this.cityId = cityId;
		this.customerId = customerId;
		this.stateText = stateText;
		this.citytext = citytext;
		this.password = password;
		this.countryId = countryId;
		this.id = id;
		this.email = email;
	}
	

}
