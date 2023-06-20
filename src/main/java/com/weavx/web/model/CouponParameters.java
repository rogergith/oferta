package com.weavx.web.model;

public class CouponParameters {
	
	private String couponCode;
	private int purchaseAmount;
	private String applier;
	private boolean activateCoupon;
	private String userAgent;
	private String ipAddress;
	
	
	public CouponParameters(String couponCode, int purchaseAmount, String applier, boolean activateCoupon,
			String userAgent, String ipAddress) {
		super();
		this.couponCode = couponCode;
		this.purchaseAmount = purchaseAmount;
		this.applier = applier;
		this.activateCoupon = activateCoupon;
		this.userAgent = userAgent;
		this.ipAddress = ipAddress;
	}



	public String getCouponCode() {
		return couponCode;
	}



	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}



	public int getPurchaseAmount() {
		return purchaseAmount;
	}



	public void setPurchaseAmount(int purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}



	public String getApplier() {
		return applier;
	}



	public void setApplier(String applier) {
		this.applier = applier;
	}



	public boolean isActivateCoupon() {
		return activateCoupon;
	}



	public void setActivateCoupon(boolean activateCoupon) {
		this.activateCoupon = activateCoupon;
	}



	public String getUserAgent() {
		return userAgent;
	}



	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}



	public String getIpAddress() {
		return ipAddress;
	}



	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	
}
