package com.weavx.web.model;

public class TxDetail {
	
	private Number fundId;
	private Number amount;
	private boolean splitPayment;
	
	public Number getFundId() {
		return fundId;
	}
	public void setFundId(Number fundId) {
		this.fundId = fundId;
	}
	public Number getAmount() {
		return amount;
	}
	public void setAmount(Number amount) {
		this.amount = amount;
	}
	public boolean isSplitPayment() {
		return splitPayment;
	}
	public void setSplitPayment(boolean splitPayment) {
		this.splitPayment = splitPayment;
	}
	
	@Override
	public String toString() {
		return "TxDetail [fundId=" + fundId + ", amount=" + amount + ", splitPayment=" + splitPayment +  "]";
	}

	

	
}
