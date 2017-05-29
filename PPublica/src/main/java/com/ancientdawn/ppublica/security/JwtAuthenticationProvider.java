package com.ancientdawn.ppublica.security;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
	
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void additionalAuthenticationChecks(UserDetails arg0, UsernamePasswordAuthenticationToken arg1)
			throws AuthenticationException {

	}

	// returns authenticated user, which AbstractUserDetailsAuthenticationProvider sends to createSuccessAuthentication(.....)
	// throws AuthenticationException if authentication fails
	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		
		// expected arguments when called by AbstractUserDetailsAuthenticationProvider's authenticate()
		// which is called by JwtAuthenticationFilter's attemptAuthentication():
		
		// username = "NONE_PROVIDED
		// authentication -> only method that will return something useful: getToken() (after casting)
		System.out.println("--------in retrieveUser()... -------");

		JwtAuthenticationToken jwtAuthentication = (JwtAuthenticationToken)authentication;
		String token = jwtAuthentication.getToken();
		System.out.println("--------About to parse user... -------");
		ParsedUser parsedUser = jwtUtil.parseToken(token);
		
		
		if(parsedUser == null) {
			throw new JwtAuthenticationException("JwtToken is not valid");
		}
		
		System.out.println(parsedUser.getUsername());
		System.out.println(parsedUser.getId());
		System.out.println(parsedUser.getAuthorities());
		
		Collection<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(parsedUser.getAuthorities());
		
		return new AuthenticatedPublisher(parsedUser.getId(), parsedUser.getUsername(), token, authorityList);
		
	}
	
	
	/*
	 *  after calling retrieveUser(), the abstract provider's authenticate() calls createSuccessAuthentication()
	 *  it uses the return object of retrieveUser()...
	 *  
	 *  createSuccessAuthentication() creates and returns a new UsernamePasswordAuthenticationToken
	 *  which ends up as the following in our custom implementation:
	 *  
	 *  new UsernamePasswordAuthenticationToken(authenticatedPublisher, null, authorities extracted)
	 *  authenticatedPulisher: comes from retrieveUser and becomes the principal
	 *  null: authentication(the argument to retrieveUser).getCredentials()
	 *  
	 *  
	 *  the new token also has its setDetails(null) called
	 */
	
	/*
	 * after createSuccessAuthentication() control returns to Filter's attemptAuthentication()
	 */

}
