package com.ancientdawn.ppublica.security;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	private String secret = "syegrfguyb875q8u23874qry487g";
	
	/*
	 * unsuccessful: returns null : NO EXCEPTION THROWN
	 * 
	 */

	public ParsedUser parseToken(String token) {
		try {
			Claims body = (Claims)(Jwts.parser()
					.setSigningKey(secret)
					.parse(token)
					.getBody());
			
			ParsedUser user = new ParsedUser();
			user.setUsername(body.getSubject());
			user.setId(Long.parseLong((String)body.get("userId")));
			user.setAuthorities((String)body.get("role"));
			
			return user;
			
		} catch(JwtException | ClassCastException f) {
			return null;
		}
	}
	
	
	public String generateToken(ParsedUser user) {
		Claims claims = Jwts.claims().setSubject(user.getUsername());
		
		claims.put("userId", user.getId().toString());
		claims.put("role", user.getAuthorities());
		
		return Jwts.builder()
				   .setClaims(claims)
				   .signWith(SignatureAlgorithm.HS512, secret)
				   .compact();
	}
}
