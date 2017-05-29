package com.ancientdawn.ppublica.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

	/*
	 * only serves to wrap the token before full authentication takes place
	 */
	
	private static final long serialVersionUID = 5955908967439369018L;
	private String token;

	// constructor for so far unauthenticated user
	public JwtAuthenticationToken(String token) {
		super(null, null); // new UsernamePasswordAuthenticationToken(Obj principal, Obj credentials)
	    this.token = token;
	  }
	
	
	public String getToken() {
	    return token;
	    
	}

}
