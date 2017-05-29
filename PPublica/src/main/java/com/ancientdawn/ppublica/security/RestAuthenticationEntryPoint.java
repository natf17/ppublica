package com.ancientdawn.ppublica.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException auth)
			throws IOException, ServletException {

		System.out.println("###### RestAuthenticationEntryPoint****");
		System.out.println(SecurityContextHolder.getContext());
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized haha");
	}

}
