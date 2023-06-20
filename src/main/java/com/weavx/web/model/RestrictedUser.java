package com.weavx.web.model;

public class RestrictedUser {
	private int id;
	private int customerId;
	private int applicationId;
	private String name;
	private String lastName;
	private String email;
	private boolean active;
	
	
	
	public RestrictedUser(int id, int customerId, int applicationId, String name, String lastName, String email,
			boolean active) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.applicationId = applicationId;
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.active = active;
	}
	
	public RestrictedUser(int id, String name, String lastName, String email) {
		super();
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.email = email;
	}
	
	public RestrictedUser(int id, String name, String lastName, String email, boolean active) {
		super();
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.active = active;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public int getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	
}
