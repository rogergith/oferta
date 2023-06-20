package com.weavx.web.model;

public class Amount {
	
	private int amount;
	private int id;
	
	public Amount(int amount, int id) {
		super();
		this.amount = amount;
		this.id = id;
	}
	
	
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Amount [amount=" + amount + ", id=" + id + "]";
	}
	
	

}
