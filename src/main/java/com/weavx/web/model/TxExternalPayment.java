package com.weavx.web.model;

public class TxExternalPayment {
	
	private int externalPaymentDataId;
	private String externalPaymentDataTxt;
	
	public TxExternalPayment(int externalPaymentDataId, String externalPaymentDataTxt) {
		super();
		this.externalPaymentDataId = externalPaymentDataId;
		this.externalPaymentDataTxt = externalPaymentDataTxt;
	}

	public int getExternalPaymentDataId() {
		return externalPaymentDataId;
	}

	public void setExternalPaymentDataId(int externalPaymentDataId) {
		this.externalPaymentDataId = externalPaymentDataId;
	}

	public String getExternalPaymentDataTxt() {
		return externalPaymentDataTxt;
	}

	public void setExternalPaymentDataTxt(String externalPaymentDataTxt) {
		this.externalPaymentDataTxt = externalPaymentDataTxt;
	}
	
	

}
