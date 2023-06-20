package com.weavx.web.model;

public class AffiliateReport {
	private Number affiliate;
	private String affiliateName;
	private String paymentTypeAffiliate;
	private Number countAffiliationConvertion;
	private Number sumAffiliationBalance;
	private String dataPayment;
	private String paymentReference;
	private String affiliationPayed;
	private String affiliateEmail;
	private String transactionId;
	private String stripTrxId;
	private String userEmail;
	private String eventName;
	private String paymentType;
	private Number amount;
	private Number comission;
	private Number date;
	private String dateFormat;
	private String timeFormat;
	private String nameReferred;
	private String paymentReferred;
	private String typePaymentReferred;
	
 

	public Number getAffiliate() {
		return affiliate;
	}
	public void setAffiliate(Number affiliate) {
		this.affiliate = affiliate;
	}
	public String getAffiliateName() {
		return affiliateName;
	}
	public void setAffiliateName(String affiliateName) {
		this.affiliateName = affiliateName;
	}
	public String getPaymentTypeAffiliate() {
		return paymentTypeAffiliate;
	}
	public void setPaymentTypeAffiliate(String paymentTypeAffiliate) {
		this.paymentTypeAffiliate = paymentTypeAffiliate;
	}
	public Number getCountAffiliationConvertion() {
		return countAffiliationConvertion;
	}
	public void setCountAffiliationConvertion(Number countAffiliationConvertion) {
		this.countAffiliationConvertion = countAffiliationConvertion;
	}
	public Number getSumAffiliationBalance() {
		return sumAffiliationBalance;
	}
	public void setSumAffiliationBalance(Number sumAffiliationBalance) {
		this.sumAffiliationBalance = sumAffiliationBalance;
	}
	public String getDataPayment() {
		return dataPayment;
	}
	public void setDataPayment(String dataPayment) {
		this.dataPayment = dataPayment;
	}
	public String getPaymentReference() {
		return paymentReference;
	}
	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}
	public String getAffiliationPayed() {
		return affiliationPayed;
	}
	public void setAffiliationPayed(String affiliationPayed) {
		this.affiliationPayed = affiliationPayed;
	}
	public String getAffiliateEmail() {
		return affiliateEmail;
	}
	public void setAffiliateEmail(String affiliateEmail) {
		this.affiliateEmail = affiliateEmail;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getStripTrxId() {
		return stripTrxId;
	}
	public void setStripTrxId(String stripTrxId) {
		this.stripTrxId = stripTrxId;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public Number getAmount() {
		return amount;
	}
	public void setAmount(Number amount) {
		this.amount = amount;
	}
	public Number getComission() {
		return comission;
	}
	public void setComission(Number comission) {
		this.comission = comission;
	}
	public Number getDate() {
		return date;
	}
	public void setDate(Number date) {
		this.date = date;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public String getTimeFormat() {
		return timeFormat;
	}
	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}
	
	public String getNameReferred() {
		return nameReferred;
	}
	public void setNameReferred(String nameReferred) {
		this.nameReferred = nameReferred;
	}
	public String getPaymentReferred() {
		return paymentReferred;
	}
	public void setPaymentReferred(String paymentReferred) {
		this.paymentReferred = paymentReferred;
	}
	public String getTypePaymentReferred() {
		return typePaymentReferred;
	}
	public void setTypePaymentReferred(String typePaymentReferred) {
		this.typePaymentReferred = typePaymentReferred;
	}
	@Override
	public String toString() {
		return "AffiliateReport [affiliate=" + affiliate + ", affiliateName=" + affiliateName
				+ ", paymentTypeAffiliate=" + paymentTypeAffiliate + ", countAffiliationConvertion="
				+ countAffiliationConvertion + ", sumAffiliationBalance=" + sumAffiliationBalance + ", dataPayment="
				+ dataPayment + ", paymentReference=" + paymentReference + ", affiliationPayed=" + affiliationPayed
				+ ", affiliateEmail=" + affiliateEmail + ", transactionId=" + transactionId + ", stripTrxId="
				+ stripTrxId + ", userEmail=" + userEmail + ", eventName=" + eventName + ", paymentType=" + paymentType
				+ ", amount=" + amount + ", comission=" + comission + ", date=" + date + ", dateFormat=" + dateFormat
				+ ", timeFormat=" + timeFormat + ", nameReferred=" + nameReferred + ", paymentReferred="
				+ paymentReferred + ", typePaymentReferred=" + typePaymentReferred + "]";
	}
}
