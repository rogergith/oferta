package com.weavx.web.model;

public class TransactionStatus {

	private int id;
	private String name;
	
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
	@Override
	public String toString() {
		return "TransactionStatus [id=" + id + ", name=" + name + "]";
	}
	
	
}