package com.ancientdawn.ppublica.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/* used as principal in UseramePasswordAuthenticationToken that provider's createSuccessAuthentication()
 * creates and return to attemptAuthentication() in filter
 */
public class AuthenticatedPublisher extends User {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3157886894981908185L;
	private String token;
	private Long id;

	public AuthenticatedPublisher(Long id, String username, String token,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, "", authorities); // password not saved

		this.token = token;
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getToken() {
		return token;
	}

}