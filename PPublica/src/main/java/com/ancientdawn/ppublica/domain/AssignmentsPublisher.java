package com.ancientdawn.ppublica.domain;

import java.util.Set;
import java.util.HashSet;


public class AssignmentsPublisher extends Publisher {
	private Set<TimeSlot> mySlots;
	
	public AssignmentsPublisher(String firstName, String lastName, String username) {
		this(null, firstName, lastName, username);
	}
	
	public AssignmentsPublisher(Long id, String firstName, String lastName, String username) {
		super(id, firstName, lastName, username);
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		mySlots = new HashSet<TimeSlot>();
	}
	
	public AssignmentsPublisher() {
		this(null,null,null,null);
	}

	public Set<TimeSlot> getMySlots() {
		return mySlots;
	}
	
	public void setMySlots(Set<TimeSlot> mySlots) {
		this.mySlots = mySlots;
	}
	
	public void addSlot(TimeSlot slot) {
		mySlots.add(slot);
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj.getClass() != getClass())
			return false;
		
		ProfilePublisher other = (ProfilePublisher)obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		}
		
		else if (!id.equals(other.id)){
			return false;
		}
		
		if (firstName == null) {
			if (other.getFirstName() != null)
				return false;
		}
		
		else if (!firstName.equals(other.firstName)){
			return false;
		}
		
		if (lastName == null) {
			if (other.getLastName() != null)
				return false;
		}
		
		else if (!lastName.equals(other.lastName)){
			return false;
		}
		
		if (username == null) {
			if (other.getUsername() != null)
				return false;
		}
		
		else if (!username.equals(other.username)){
			return false;
		}
		
		
		return true;
		
	}

	
	@Override 
	public int hashCode() {
		final int prime = 13;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((mySlots == null) ? 0 : mySlots.hashCode());
		
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id: " + id + "\n");
		sb.append("username: " + username + "\n");
		sb.append("firstName: " + firstName + "\n");
		sb.append("lastName: " + lastName + "\n");
		sb.append("mySlots: " + mySlots + "\n");


		return sb.toString();
	}
}
