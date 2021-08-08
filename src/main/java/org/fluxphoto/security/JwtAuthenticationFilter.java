package org.fluxphoto.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

/**
 * The JWT authentication filter
 */
public class JwtAuthenticationFilter implements WebFilter {

	public static final String BEARER_PREFIX = "Bearer ";
	public static final int BEARER_LEN = BEARER_PREFIX.length();
	
	private final JwtProvider jwtProvider;

	public JwtAuthenticationFilter(JwtProvider jwtProvider) {
		this.jwtProvider = jwtProvider;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		String token = extractToken(exchange.getRequest());
		
		if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
			Authentication authentication = jwtProvider.getAuthentication(token);
			return chain.filter(exchange)
					.contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
		}
		return chain.filter(exchange);
	}

	private String extractToken(ServerHttpRequest request) {
		String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(BEARER_LEN);
		}
		
		return null;
	}	
}
