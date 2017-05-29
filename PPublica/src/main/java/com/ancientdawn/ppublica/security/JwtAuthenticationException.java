package com.ancientdawn.ppublica.security;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5671825040975832180L;

	public JwtAuthenticationException(String msg) {
		super(msg);
	}

}
