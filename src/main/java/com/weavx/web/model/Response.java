package com.weavx.web.model;

import java.util.HashMap;

public class Response {

	private int returnCode;
	private String returnMessage;
	private HashMap<String, Object> result;
	
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
	public HashMap<String, Object> getResult() {
		return result;
	}
	public void setResult(HashMap<String, Object> result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "Response [returnCode=" + returnCode + ", returnMessage=" + returnMessage + ", result=" + result + "]";
	}
	
	
}
