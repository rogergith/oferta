package com.weavx.web.model;

import java.util.List;

import com.weavx.web.model.Item;

public class Data {
	
	List<Item> data;
	String resultCode;
	
	public List<Item> getData() {
		return data;
	}
	
	public void setData(List<Item> data) {
		this.data = data;
	}
	
	public String getResultCode() {
		return resultCode;
	}
	
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	
	@Override
	public String toString() {
		return "Data [data=" + data + ", resultCode=" + resultCode + "]";
	}
}
