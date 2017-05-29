package com.ancientdawn.ppublica.security.authorization;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.ancientdawn.ppublica.security.AuthenticatedPublisher;

@Component
public class AuthorizationUtility {
	
	public boolean check(Authentication authentication, Long id) {
		System.out.println("AUTHORIZATION UTILITY:   ");
		
		Long userId = ((AuthenticatedPublisher)(authentication.getPrincipal())).getId();
		Collection<GrantedAuthority> auths = ((AuthenticatedPublisher)(authentication.getPrincipal())).getAuthorities();
		
		
		System.out.println(userId);
		System.out.println("Granted authorities.......");
		
		for(GrantedAuthority aut: auths) {
			System.out.println(aut);
		}
		return userId.equals(id);
	}
	
	public boolean allowUnauthorized(Authentication authentication) {
		String type = authentication.getPrincipal().toString();
		
		System.out.println("====ALLOW UNAUTHORIZED====   type:" + type);
		return true;
	}

}
