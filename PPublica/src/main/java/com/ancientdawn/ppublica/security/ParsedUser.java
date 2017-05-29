package com.ancientdawn.ppublica.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class ParsedUser {
	// make sure there's no whitespace
	private String authorities;
	private Long id;
	private String username;
	
	public ParsedUser() {
		//
	}

	public String getAuthorities() {
		return authorities;
	}
	
	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}
	
	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
}
