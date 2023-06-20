package com.weavx.web.model;

public class DataItem {

	String label;
	double quantity;
	double amount;
	double quantityPercentage;
	double amountPercentage;
	
	public DataItem(String label, double quantity, double amount, double quantityPercentage, double amountPercentage) {
		this.label =label;
		this.quantity = quantity;
		this.amount = amount;
		this.quantityPercentage = quantityPercentage;
		this.amountPercentage = amountPercentage;
	}


	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public double getQuantity() {
		return quantity;
	}
	
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public double getQuantityPercentage() {
		return quantityPercentage;
	}
	
	public void setQuantityPercentage(double quantityPercentage) {
		this.quantityPercentage = quantityPercentage;
	}
	
	public double getAmountPercentage() {
		return amountPercentage;
	}
	
	public void setAmountPercentage(double amountPercentage) {
		this.amountPercentage = amountPercentage;
	}
	
	@Override
	public String toString() {
		return "DataItem [label=" + label + ", quantity=" + quantity + ", amount=" + amount + ", quantityPercentage="
				+ quantityPercentage + ", amountPercentage=" + amountPercentage + "]";
	}	

}
