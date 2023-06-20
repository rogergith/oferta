package com.weavx.web.model;

import java.util.ArrayList;

public class CustomerUser {
	private int userId;
	private String firstName;
	private String lastName;
	private String email;
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
	private String userAccessToken;
	private long createdAt;
	private ArrayList<AdminRole> roleFunctions;
	private String ipAddress;
	private String phoneNumber;
	private boolean signed;
	private long signedAt;
	private String signatureUrl;
	private String nameBefore;
	private String lastNameBefore;
	private String phoneBefore;
	private int countryIdBefore;
	private int preferredLanguage;

	public static final String LOGIN = "LOGIN";
	public static final String ADD_ATTENDEE = "ADD_ATTENDEE";
	public static final String ADD_ATTENDEEs = "ADD_ATTENDEES";
	public static final String COMPLIMENTARY = "COMPLIMENTARY";
	public static final String OTHER = "OTHER";
	public static final String DISCOUNT = "DISCOUNT";
	public static final String REPORTS = "REPORTS";
	public static final String WHILELIST = "WHILELIST";
	public static final String SOURCES = "SOURCES";
	public static final String GLOBAL_REPORT = "GLOBAL_REPORT";
	public static final String RESTRICT_EVENT = "RESTRICT_EVENT";
	public static final String BLACKLIST_QUERY = "BLACKLIST_QUERY";
	public static final String BLACKLIST_CLEAN = "BLACKLIST_CLEAN";
	public static final String USERS_QUERY = "USERS_QUERY";
	public static final String MODIFY_EMAIL = "MODIFY_EMAIL";
	public static final String MODIFY_USER_DATA = "MODIFY_USER_DATA";
	public static final String SEND_PURCHASE_RECEIPT = "SEND_PURCHASE_RECEIPT";
	public static final String SEND_MAGIC_LINK = "SEND_MAGIC_LINK";
	public static final String SEND_EMAIL_PASSWORD_RESET = "SEND_EMAIL_PASSWORD_RESET";
	public static final String TX_QUERY = "TX_QUERY";
	public static final String INVALIDATE_TRANSACTION = "INVALIDATE_TRANSACTION";
	public static final String USERS_ACCESS_QUERY = "USERS_ACCESS_QUERY";
	public static final String AUDIT_QUERY = "AUDIT_QUERY";
	public static final String EVENT_ASSETS = "EVENT_ASSETS";
	public static final String REGISTER_DOCUMENT_SIGNATURE = "REGISTER_DOCUMENT_SIGNATURE";
	public static final String REFRESH_REPORTING_TABLES = "REFRESH_REPORTING_TABLES"; 
	public static final String VIEW_REGISTERED_ONLINE_USERS = "VIEW_REGISTERED_ONLINE_USERS";
	public static final String VIEW_AMOUNT = "VER_MONTO";
	public static final String ADD_CARD = "AGREGAR_TARJETA";
	public static final String ADD_NOTE = "AGREGAR_NOTA";
	public static final String HOJA_VIDA_MENU = "HOJA_VIDA_MENU";
	public static final String CANCEL_TX = "CANCEL_TX";
	public static final String EXPIRED_ACCESS = "EXPIRED_ACCESS";
	public static final String REPORT_AFFILIATE = "REPORT_AFFILIATE";
	public static final String REPORT_AFFILIATE_UPDATE = "REPORT_AFFILIATE_UPDATE";
	public static final String VIEW_SALES_ADMIN = "VIEW_SALES_ADMIN";
	public static final String VIEW_SALES_AGENT = "VIEW_SALES_AGENT";


	public String getSignatureUrl() {
		return signatureUrl;
	}

	public void setSignatureUrl(String signatureUrl) {
		this.signatureUrl = signatureUrl;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phone) {
		this.phoneNumber = phone;
	}

	public String getIpAddress() {
		return ipAddress;
	}
	
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public  long getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}
	
	public String getUserAccessToken() {
		return userAccessToken;
	}
	public void setUserAccessToken(String userAccessToken) {
		this.userAccessToken = userAccessToken;
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
		
	public boolean isSigned() {
		return signed;
	}

	public void setSigned(boolean signed) {
		this.signed = signed;
	}

	public long getSignedAt() {
		return signedAt;
	}

	public void setSignedAt(long signedAt) {
		this.signedAt = signedAt;
	}

	public ArrayList<AdminRole> getRoleFunctions() {
		return roleFunctions;
	}
	public void setRoleFunctions(ArrayList<AdminRole> roleFunctions) {
		this.roleFunctions = roleFunctions;
	}
	public CustomerUser(int userId, String firstName, String lastName, int genderId, String birthDate,
			String profileimage, int stateId, int cityId, int customerId, String stateText, String citytext,
			String password, int countryId, int id, String userAccessToken) {
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
		this.userAccessToken = userAccessToken;
	}
	
	public CustomerUser() {
		super();
	}
	
	public boolean userHasAccess(String adminFunction) {
		AdminRole adminRole = new AdminRole();
		adminRole.setName(adminFunction);
		return roleFunctions.contains(adminRole);
	}

	public String getNameBefore() {
		return nameBefore;
	}

	public void setNameBefore(String nameBefore) {
		this.nameBefore = nameBefore;
	}

	public String getLastNameBefore() {
		return lastNameBefore;
	}

	public void setLastNameBefore(String lastNameBefore) {
		this.lastNameBefore = lastNameBefore;
	}

	public String getPhoneBefore() {
		return phoneBefore;
	}

	public void setPhoneBefore(String phoneBefore) {
		this.phoneBefore = phoneBefore;
	}

	public int getCountryIdBefore() {
		return countryIdBefore;
	}

	public void setCountryIdBefore(int countryIdBefore) {
		this.countryIdBefore = countryIdBefore;
	}

	public int getPreferredLanguage() {
		return preferredLanguage;
	}

	public void setPreferredLanguage(int preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

	@Override
	public String toString() {
		return "CustomerUser [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
				+ email + ", genderId=" + genderId + ", birthDate=" + birthDate + ", profileimage=" + profileimage
				+ ", stateId=" + stateId + ", cityId=" + cityId + ", customerId=" + customerId + ", stateText="
				+ stateText + ", citytext=" + citytext + ", password=" + password + ", countryId=" + countryId + ", id="
				+ id + ", userAccessToken=" + userAccessToken + ", createdAt=" + createdAt + ", roleFunctions="
				+ roleFunctions + ", ipAddress=" + ipAddress + ", phoneNumber=" + phoneNumber + ", signed=" + signed
				+ ", signedAt=" + signedAt + ", signatureUrl=" + signatureUrl + ", nameBefore=" + nameBefore
				+ ", lastNameBefore=" + lastNameBefore + ", phoneBefore=" + phoneBefore + ", countryIdBefore="
				+ countryIdBefore + ", preferredLanguage=" + preferredLanguage + "]";
	}
	
}
