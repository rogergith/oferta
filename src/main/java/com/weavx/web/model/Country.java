package com.weavx.web.model;

import java.util.Comparator;

public class Country {
	
	private String shortName;
	private String name;
	private int id;
	
	public Country(String shortName, String name, int id) {
		super();
		this.shortName = shortName;
		this.name = name;
		this.id = id;
	}
	
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Country other = (Country) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	public static class CustomComparator implements Comparator<Country> {
	    @Override
	    public int compare(Country c1, Country c2) {
	        return c1.getName().compareTo(c2.getName());
	    }
	}
}
