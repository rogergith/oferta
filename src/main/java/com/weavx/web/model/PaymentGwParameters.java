package com.weavx.web.model;

public class PaymentGwParameters {

	private String creditCardNumber;
	private String creditCardExpiration;
	private String creditCardCode;
	private String orderDescription;
	private String cardId;
	private boolean savePaymentData;
	private String token;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	public String getCreditCardExpiration() {
		return creditCardExpiration;
	}
	public void setCreditCardExpiration(String creditCardExpiration) {
		this.creditCardExpiration = creditCardExpiration;
	}
	public String getCreditCardCode() {
		return creditCardCode;
	}
	public void setCreditCardCode(String creditCardCode) {
		this.creditCardCode = creditCardCode;
	}
	public String getOrderDescription() {
		return orderDescription;
	}
	public void setOrderDescription(String orderDescription) {
		this.orderDescription = orderDescription;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public boolean isSavePaymentData() {
		return savePaymentData;
	}
	public void setSavePaymentData(boolean savePaymentData) {
		this.savePaymentData = savePaymentData;
	}
	@Override
	public String toString() {
		return "PaymentGwParameters [creditCardNumber=" + creditCardNumber + ", creditCardExpiration="
				+ creditCardExpiration + ", creditCardCode=" + creditCardCode + ", orderDescription=" + orderDescription
				+ ", cardId=" + cardId + ", savePaymentData=" + savePaymentData + ", token=" + token + "]";
	}
	

}
