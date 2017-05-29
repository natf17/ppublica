package com.ancientdawn.ppublica.security;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.ancientdawn.ppublica.service.PublisherService;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private PublisherService publisherService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		// populate a parsed user
		ParsedUser user = new ParsedUser();
		UserDetails userDetails = ((UserDetails)authentication.getPrincipal());
		String username = userDetails.getUsername();
		Long id = publisherService.readPublisher(username).getId();
		StringBuilder authorities = new StringBuilder();
		StringBuilder header = new StringBuilder();
		String token = null;
		
		
		for(GrantedAuthority authority : authentication.getAuthorities()) {
			authorities.append(authority);
			authorities.append(",");
		}
		// remove last comma
		authorities.deleteCharAt(authorities.length() - 1);
		
		System.out.println("ABOUT TO GENERATE JWT...");
		System.out.println(authorities.toString());
		
		user.setUsername(username);
		user.setId(id);
		user.setAuthorities(authorities.toString());
		
		token = jwtUtil.generateToken(user);
		
		System.out.println(token);
		
		// also set cookie
		Cookie toBeSet = new Cookie("access_token", token);
		toBeSet.setMaxAge(200);
		response.addCookie(toBeSet);
		
		header.append("Bearer<");
		header.append(token);
		header.append(">");
		
		System.out.println(header.toString());

		
		
		response.addHeader("Authentication", header.toString());
	
		

	}

}
