package com.ancientdawn.ppublica.domain;


public class Publisher {
	protected Long id;
	protected String username;
	protected String firstName;
	protected String lastName;	
	
	public Publisher(String firstName, String lastName, String username) {
		this(null, firstName, lastName, username);
	}
	
	public Publisher(Long id, String firstName, String lastName, String username) {
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public Publisher() {
		this(null,null,null);
	}
	
	public Long getId() {
		return id;
	}
	
	// set automatically by database, not user
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getFirstName() {
		return firstName;
	
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj.getClass() != getClass())
			return false;
		
		Publisher other = (Publisher)obj;
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
		
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id: " + id + "\n");
		sb.append("username: " + username + "\n");
		sb.append("firstName: " + firstName + "\n");
		sb.append("lastName: " + lastName + "\n");


		return sb.toString();
	}
}
