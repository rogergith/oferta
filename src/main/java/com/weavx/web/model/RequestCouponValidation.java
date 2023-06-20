package com.weavx.web.model;


public class RequestCouponValidation {
	
	private int applicationId;
	private String methodName;
	private String accessToken;
	private CouponParameters parameters;
	
	public int getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public CouponParameters getParameters() {
		return parameters;
	}
	public void setParameters(CouponParameters parameters) {
		this.parameters = parameters;
	}
	
	

}
