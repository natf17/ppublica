package com.ancientdawn.ppublica.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.ancientdawn.ppublica.exception.JwtTokenMissingException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	
	public JwtAuthenticationFilter() {
		super("/**");
	}
	
	/*@Override
	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		return true;
	}
	*/

	// throws AuthenticationException if authentication fails (thrown by provider)
	// the Authentication object it returns is sent to successfulAuthentication() method
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		System.out.println("attempt auth called: " + getAllowSessionCreation() + " " + this.getSuccessHandler());
		
		
		String header = request.getHeader("Authentication");
		
		Cookie[] cookies = request.getCookies();
		String authToken = "";
		
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				System.out.println(cookie.getName());
				if((cookie.getName()).equals("access_token")) {
					System.out.println("IN");
					authToken = cookie.getValue();
					System.out.println("COOKIE TOKEN:");
					System.out.println(authToken);
				}
			}
		}
				
		if(authToken == "") {
			if(header == null || !header.startsWith("Bearer")) {
				throw new JwtTokenMissingException("No JWT token found in request header OR cookie");					
			} else {
				authToken = header.substring(7);
				System.out.println("RECEIVED TOKEN:");
				authToken = authToken.substring(0,authToken.length() - 1);
				System.out.println(authToken);
			}
		}
	
		
		
		// JwtAuthenticationToken extends UsernamePasswordAuthenticationToken
		// UsernamePasswordAuthenticationToken implements Authentication interface
		JwtAuthenticationToken authRequest = new JwtAuthenticationToken(authToken);
		
		// default AuthenticationManager is ProviderManager
		// calling authenticate(Authentication auth) -> will go through AuthenticationProviders until one is found
		// that can successfully authenticate object
		System.out.println(getAuthenticationManager());
		return getAuthenticationManager().authenticate(authRequest);
		
	}
	
	/*
	 * if attemptAuthentication() completes....
	 * doFilter() uses its return object to pass on to successfulAuthentication(request, response, chain, authResult)
	 */
	
	@Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);

        // As this authentication is in HTTP header, after success we need to continue the request normally
        // and return the response as if the resource was not secured at all
        System.out.println("SECURITY COMTEXT"+SecurityContextHolder.getContext());
        chain.doFilter(request, response);
    }
	

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
System.out.println("doFilter");
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		if (!requiresAuthentication(request, response)) {
			System.out.println("no auth req");

			chain.doFilter(request, response);

			return;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Request is to process authentication");
		}

		Authentication authResult;

		try {
			authResult = attemptAuthentication(request, response);
			if (authResult == null) {
				// return immediately as subclass has indicated that it hasn't completed
				// authentication
				return;
			}
			//sessionStrategy.onAuthentication(authResult, request, response);
		}
		catch (InternalAuthenticationServiceException failed) {
			logger.error(
					"An internal error occurred while trying to authenticate the user.",
					failed);
			unsuccessfulAuthentication(request, response, failed);

			return;
		}
		catch (AuthenticationException failed) {
			
			System.out.println(request.getRequestURI() + "OR" + request.getContextPath());
			
			
			// Authentication failed
			System.out.println("###### Authemtication Failed - Filter caught exception****");
			System.out.println(SecurityContextHolder.getContext());
			
			
			Pattern p = Pattern.compile("^(?!.*edit).*$");
			Matcher m = p.matcher(request.getRequestURI());
			
			boolean isAMatch = m.matches();
			System.out.println(isAMatch);
			if(isAMatch) {
				chain.doFilter(request, response);
				return;
			} else {
				// contains edit at the end of the string
			}
			
			/*
			List<String> securedUrisWithoutEdit = new ArrayList<String>();
			unsecuredUris.add("/PPublica/");
			unsecuredUris.add("/PPublica");
			unsecuredUris.add("/PPublica/login");
			unsecuredUris.add("/PPublica/69th");
			unsecuredUris.add("/PPublica/69th/edit");
			unsecuredUris.add("/PPublica/publishers");
			unsecuredUris.add("/PPublica/schedules");

			if(unsecuredUris.contains(request.getRequestURI())) {
				System.out.println("OPtional");
				
				
				chain.doFilter(request, response);
				return;
				
				
			}
			
			*/
			
			unsuccessfulAuthentication(request, response, failed);

			return;
		}

		/*
		// Authentication success
		if (continueChainBeforeSuccessfulAuthentication) {
			chain.doFilter(request, response);
		}
		*/
		successfulAuthentication(request, response, chain, authResult);
		
	
	}	

	
}
