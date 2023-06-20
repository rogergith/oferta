package com.weavx.web.model;

import com.weavx.web.utils.PaymentStatuses;

public class CustomerTransaction {

	private int id;
	private long txDate;
	private String fund;
	private String cardMasked;
	private String cardBrand;
	private Number amount;
	private String status;
	private Boolean isScheduled;
	private String internationalId; 
	private String paymentInfo;
	private String activeEvent;
	private int currentEvent;
	private String accessDateTxt;
	private Long accessDateFrom;
	private Long accessDateTo;
	private int accessDays;
	private Boolean splitPayment;
	private Number discount;
	private Boolean subscription;
	private Integer subscriptionStatusId;

	
	public CustomerTransaction() {
		super();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getTxDate() {
		return txDate;
	}
	public void setTxDate(long txDate) {
		this.txDate = txDate;
	}
	public String getFund() {
		return fund;
	}
	public void setFund(String fund) {
		this.fund = fund;
	}
	public String getCardMasked() {
		return cardMasked;
	}
	public void setCardMasked(String cardMasked) {
		this.cardMasked = cardMasked;
	}
	public String getCardBrand() {
		return cardBrand;
	}
	public void setCardBrand(String cardBrand) {
		this.cardBrand = cardBrand;
	}
	public Number getAmount() {
		return amount;
	}
	public void setAmount(Number amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public String getActiveEvent() {
		return activeEvent;
	}
	public void setActiveEvent(String activeEvent) {
		this.activeEvent = activeEvent;
	}

	public void setStatus(String status) {
		if(PaymentStatuses.PAYMENT_IN_PROGRESS.getStatus().equals(status)) {
			this.status = "In progress";
		} else if(PaymentStatuses.PAYMENT_DENIED.getStatus().equals(status)) {
			this.status = "Denied";
		}  else if(PaymentStatuses.ABORTED.getStatus().equals(status)) {
			this.status = "Cancelled";
		} else if(PaymentStatuses.CHARGED.getStatus().equals(status)) {
			this.status = "Charged";
		} else if(PaymentStatuses.FAILED_TX.getStatus().equals(status)) {
			this.status = "Failed Transaction";
		} else if(PaymentStatuses.SUCCESS_TX.getStatus().equals(status)) {
			this.status = "Success";
		} else if(PaymentStatuses.USER_EXPIRED_ACCESS.getStatus().equals(status)) {
			this.status = "Access Expired";
		} else if(PaymentStatuses.IN_BLACKLIST.getStatus().equals(status)) {
			this.status = "Blacklist";
		} else if(PaymentStatuses.NOT_PROVISIONED.getStatus().equals(status)) {
			this.status = "Not Provisioned";
		}else if(PaymentStatuses.FRAUD_REPORTED.getStatus().equals(status)) {
			this.status = "Reported Wire Fraud";
		}else if(PaymentStatuses.CANCEL_REFUND.getStatus().equals(status)) {
			this.status = "Cancel with Refund";
		}else if(PaymentStatuses.SPLIT_SUCCESS.getStatus().equals(status)) {
			this.status = "Split Payment Success";
		}else if(PaymentStatuses.SPLIT_IN_PROGRESS.getStatus().equals(status)) {
			this.status = "Split Payment in Progress";
		}else if(PaymentStatuses.SPLIT_ABORTED.getStatus().equals(status)) {
			this.status = "Split Payment Cancelled";
		}else if(PaymentStatuses.SPLIT_EXPIRED_ACCESS.getStatus().equals(status)) {
			this.status = "Split Payment Access Expired";
		}else if(PaymentStatuses.SPLIT_FRAUD_REPORTED.getStatus().equals(status)) {
			this.status = "Split Payment Reported Wire Fraud";
		}else if(PaymentStatuses.SPLIT_REFUND.getStatus().equals(status)) {
			this.status = "Split Payment Cancel with Refund";
		}else if(PaymentStatuses.SUBSCRIPTION_CANCEL.getStatus().equals(status)) {
			this.status = "Subscription Cancelled";
		}
		else {
			this.status = "Error";
		}
	}
	public Boolean getIsScheduled() {
		return isScheduled;
	}
	public void setIsScheduled(Boolean isScheduled) {
		this.isScheduled = isScheduled;
	}
	public String getInternationalId() {
		return internationalId;
	}
	public void setInternationalId(String internationalId) {
		this.internationalId = internationalId;
	}
	public String getPaymentInfo() {
		return paymentInfo;
	}
	public void setPaymentInfo(String paymentInfo) {
		
		this.paymentInfo = paymentInfo;
	}
	public int getCurrentEvent() {
		return currentEvent;
	}

	public void setCurrentEvent(int currentEvent) {
		this.currentEvent = currentEvent;
	}

	public String getAccessDateTxt() {
		return accessDateTxt;
	}

	public void setAccessDateTxt(String accessDateTxt) {
		this.accessDateTxt = accessDateTxt;
	}

	public Long getAccessDateFrom() {
		return accessDateFrom;
	}

	public void setAccessDateFrom(Long accessDateFrom) {
		this.accessDateFrom = accessDateFrom;
	}

	public Long getAccessDateTo() {
		return accessDateTo;
	}

	public void setAccessDateTo(Long accessDateTo) {
		this.accessDateTo = accessDateTo;
	}

	public int getAccessDays() {
		return accessDays;
	}

	public void setAccessDays(int accessDays) {
		this.accessDays = accessDays;
	}

	public Boolean getSplitPayment() {
		return splitPayment;
	}

	public void setSplitPayment(Boolean splitPayment) {
		this.splitPayment = splitPayment;
	}

	public Number getDiscount() {
		return discount;
	}

	public void setDiscount(Number discount) {
		this.discount = discount;
	}

	public Boolean getSubscription() {
		return subscription;
	}

	public void setSubscription(Boolean subscription) {
		this.subscription = subscription;
	}

	public Integer getSubscriptionStatusId() {
		return subscriptionStatusId;
	}

	public void setSubscriptionStatusId(Integer subscriptionStatusId) {
		this.subscriptionStatusId = subscriptionStatusId;
	}
	
	
}