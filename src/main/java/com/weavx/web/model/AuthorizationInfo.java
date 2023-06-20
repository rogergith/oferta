package com.weavx.web.model;

public class AuthorizationInfo {
	private String accountId;
	private String authCode;
	private String transId;
	private String txId;
	private String responseCode;
	private String messageResult;
	
	public String getMessageResult() {
		return messageResult;
	}
	public void setMessageResult(String messageResult) {
		this.messageResult = messageResult;
	}
	
	public String getTxId() {
		return txId;
	}
	public void setTxId(String txId) {
		this.txId = txId;
	}
	
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	@Override
	public String toString() {
		return "AuthorizationInfo [accountId=" + accountId + ", authCode=" + authCode + ", transId=" + transId
				+ ", responseCode=" + responseCode + "]";
	}
	
}
