package com.ancientdawn.ppublica.domain;

import java.util.HashSet;
import java.util.Set;

public class WeekSchedule {
	private Long id;
	private String info;
	private String location;
	private Short cartId;
	private Set<Day> week;
	
	// defaults to weekly schedule
	public WeekSchedule() {
		this(null, null, null, null);
	}
	
	public WeekSchedule(Long id, String location, String info, Short cartId) {
		this.id = id;
		this.location = location;
		this.info = info;
		this.cartId = cartId;
		this.week = new HashSet<Day>();

	}
	
	public Long getId() {
		return id;
	}
	
	// set automatically by database, not user
	public void setId(Long id) {
		this.id = id;
	}
	
	public Set<Day> getWeek() {
		return week;
	}
	
	public void setWeek(Set<Day> week) {
		this.week = week;
	}
	
	public String getInfo() {
		return info;
	}
	
	public void setInfo(String info) {
		this.info = info;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public Short getCartId() {
		return cartId;
	}
	
	public void setCartId(Short cartId) {
		this.cartId = cartId;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj.getClass() != getClass())
			return false;
		
		WeekSchedule other = (WeekSchedule)obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		}
		
		else if (!id.equals(other.id)){
			return false;
		}
		
		
		return true;
		
	}

	
	@Override 
	public int hashCode() {
		final int prime = 71;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id: " + id + "\n");
		sb.append("location: " + location + "\n");
		sb.append("info: " + info + "\n");
		sb.append("cartId: " + cartId + "\n");
		sb.append("week: " + week + "\n");
		return sb.toString();
	}
	
}
