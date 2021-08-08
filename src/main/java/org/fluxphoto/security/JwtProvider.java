package org.fluxphoto.security;

import static java.util.stream.Collectors.joining;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
	
	private static final String ROLES_KEY = "roles";
	
	@Value("${jwt.secretKey}")
	private String secretKeyText;
	
	@Value("${jwt.expireMillis}")
	private int expireMillis;
	
	private SecretKey secretKey;

	@PostConstruct
	protected void init() {
		String secret = Base64.getEncoder().encodeToString(secretKeyText.getBytes());
		secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Create the JWT token from the authentication
	 * 
	 * @param authentication
	 * @return
	 */
	public String createToken(Authentication authentication) {
		
		String username = authentication.getName();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		
		Claims claims = Jwts.claims().setSubject(username);
		claims.put(ROLES_KEY, authorities.stream().map(GrantedAuthority::getAuthority).collect(joining(",")));
		Date now = new Date();
		Date validity = new Date(now.getTime() + expireMillis);
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(validity)
				.signWith(secretKey, SignatureAlgorithm.HS256)
				.compact();
	}

	/**
	 * Extract the authentication from the JWT token
	 * 
	 * @param token
	 * @return
	 */
	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
		
		Collection<? extends GrantedAuthority> authorities = 
				AuthorityUtils.commaSeparatedStringToAuthorityList(claims.get(ROLES_KEY).toString());
		
		User principal = new User(claims.getSubject(), "", authorities);
		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	/**
	 * @param token
	 * @return
	 */
	public boolean validateToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
			if (claims.getBody().getExpiration().before(new Date())) {
				return false;
			}
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;	
		}
	}
}