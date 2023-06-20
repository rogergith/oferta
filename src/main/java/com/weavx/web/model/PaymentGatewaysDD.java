package com.weavx.web.model;

public class PaymentGatewaysDD {

	private int id;
	private String name;
	private boolean enabled;

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
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	@Override
	public String toString() {
		return "PaymentGatewaysDD [id=" + id + ", name=" + name + ", enabled=" + enabled + "]";
	}
	
}
