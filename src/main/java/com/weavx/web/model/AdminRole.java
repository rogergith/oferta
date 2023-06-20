package com.weavx.web.model;

import java.util.Date;

public class AdminRole {
	
	private int id;
	private String name;
	private String Description;
	
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
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public AdminRole(int id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		Description = description;
	}
	
	public AdminRole() {
		super();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		AdminRole other = (AdminRole) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "AdminRole [id=" + id + ", name=" + name + ", Description=" + Description + "]";
	}
	
	
	
	
	
}
