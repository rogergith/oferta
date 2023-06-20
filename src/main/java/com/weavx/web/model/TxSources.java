package com.weavx.web.model;

public class TxSources {
	private int id;
	private String name;
	private String key;
	
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
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	@Override
	public String toString() {
		return "TxSources [id=" + id + ", name=" + name + ", key=" + key + "]";
	}
	
	
}
