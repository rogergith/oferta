package com.weavx.web.model;

import java.util.ArrayList;

public class ResponseUsers {

	private int returnCode;
	private String returnMessage;
	private ArrayList<RestrictedUser> result;
	
	public int getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMessage() {
		return returnMessage;
	}
	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}
	public ArrayList<RestrictedUser> getResult() {
		return result;
	}
	public void setResult(ArrayList<RestrictedUser> result) {
		this.result = result;
	}
	
	
}
