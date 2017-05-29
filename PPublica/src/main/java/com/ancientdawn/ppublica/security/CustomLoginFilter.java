package com.ancientdawn.ppublica.security;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONObject;



public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {
	
	public CustomLoginFilter() {
		super();
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		
        System.out.println("parsing json....");
		
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
		    
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) { /*report an error*/ }

		ObjectMapper mapper = new ObjectMapper();
		CustomUsernamePasswordObject credentials = null;
		try {
			credentials = mapper.readValue(jb.toString(), CustomUsernamePasswordObject.class);
		} catch (IOException e) {
			throw new RuntimeException("JSON PARSE EXCEPTION");
		}
		System.out.println(credentials.getUsername());
		System.out.println(credentials.getPassword());
		
		
		String username = credentials.getUsername();
		String password = credentials.getPassword();

		if (username == null) {
			username = "";
		}

		if (password == null) {
			password = "";
		}

		username = username.trim();

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
				username, password);
		
		System.out.println("authRequest:" + authRequest);
		System.out.println("authenticationManager" + this.getAuthenticationManager());		
		return this.getAuthenticationManager().authenticate(authRequest);
	}

	
	// never called
	@Override
	protected String obtainUsername(HttpServletRequest request) {
		return null;
	}
	
	// never called
	@Override
	protected String obtainPassword(HttpServletRequest request) {
		return null;
	}
	
	
	
	// unsuccessfulauthentication(arg1,2,3,4)
	// default: clear context; calls failureHandler.onAuthenticationFailure(1,2,3)
	// default handler: SimpleUrlAuthenticationFailureHandler
	
	// else: successfulAuthentication(1,2,3,4)
	// by default: sets authentication in context
	// and : successHandler.onAuthenticationSuccess(1,2,3)
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
		super.unsuccessfulAuthentication(request, response, e);
		System.out.println("AUTHENTICATOOIN FAILED");
		e.printStackTrace();
	}
	
}
