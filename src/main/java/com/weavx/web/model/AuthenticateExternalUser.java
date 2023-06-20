package com.weavx.web.model;

public class AuthenticateExternalUser {
	
	int identityProviderId;
	String accessTokenExternal;
	
	public int getIdentityProviderId() {
		return identityProviderId;
	}
	public void setIdentityProviderId(int identityProviderId) {
		this.identityProviderId = identityProviderId;
	}
	public String getAccessTokenExternal() {
		return accessTokenExternal;
	}
	public void setAccessTokenExternal(String accessTokenExternal) {
		this.accessTokenExternal = accessTokenExternal;
	}
	
	
	
	
	
}
