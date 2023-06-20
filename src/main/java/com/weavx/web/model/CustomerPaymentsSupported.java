package com.weavx.web.model;

import java.util.ArrayList;
import java.util.stream.Stream;

public class CustomerPaymentsSupported {
	
	private int id;
	private int externalPaymentId;
	private Boolean enabled;
	
	public CustomerPaymentsSupported(int id, int epi, Boolean enabled) {
		super();
		this.id = id;
		this.externalPaymentId = epi;
		this.enabled = enabled;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getExternalPaymentId() {
		return externalPaymentId;
	}

	public void setExternalPaymentId(int externalPaymentId) {
		this.externalPaymentId = externalPaymentId;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	
	
}
