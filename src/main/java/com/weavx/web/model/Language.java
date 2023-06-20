package com.weavx.web.model;

import java.io.Serializable;

public class Language implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5664470150017914101L;
	private int id;
	private boolean isDefault;
	private String locale;
	private String description;
	public Language() {}
	public Language(int id, boolean isDefault, String locale, String description) {
		super();
		this.id = id;
		this.isDefault = isDefault;
		this.locale = locale;
		this.description = description;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
