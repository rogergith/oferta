package com.weavx.web.model;

public class TransactionUpdate {

	private Long affiliateId;
	private String dateFilter;
	private String paymentReference;

	public TransactionUpdate() {
	}

	public long getAffiliateId() {
		return affiliateId;
	}

	public void setAffiliateId(long affiliateId) {
		this.affiliateId = affiliateId;
	}

	public String getDateFilter() {
		return dateFilter;
	}

	public void setDateFilter(String dateFilter) {
		this.dateFilter = dateFilter;
	}

	public String getPaymentReference() {
		return paymentReference;
	}

	public void setPaymentReference(String paymentReference) {
		this.paymentReference = paymentReference;
	}

	@Override
	public String toString() {
		return "TransactionUpdate [affiliateId=" + affiliateId + ", dateFilter=" + dateFilter + ", paymentReference="
				+ paymentReference + "]";
	}

}
