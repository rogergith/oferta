package com.weavx.web.model;

import java.util.ArrayList;

public class ExternalPaymentType {

	private int id;
	private String name;
	private String urlLogo;
	
	public ExternalPaymentType (int id, String name, String url) {
		super();
		this.id = id;
		this.name = name;
		this.urlLogo = url;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrlLogo() {
		return urlLogo;
	}
	public void setUrlLogo(String urlLogo) {
		this.urlLogo = urlLogo;
	}

	@Override
	public String toString() {
		return "ExternalPaymentType [id=" + id + ", name=" + name + ", urlLogo=" + urlLogo + "]";
	}	
	
}
