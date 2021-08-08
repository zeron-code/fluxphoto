package org.fluxphoto.api;

import java.util.Map;

import org.fluxphoto.security.AuthenticateRequest;
import org.fluxphoto.security.JwtAuthenticationFilter;
import org.fluxphoto.security.JwtProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

/**
 * Authentication controller that will return a JWT token upon successful authentication
 */
@RestController("authController")
public class AuthController {

	private final JwtProvider jwtProvider;
	
	private final ReactiveAuthenticationManager authenticationManager;
	
	public AuthController(JwtProvider jwtProvider, ReactiveAuthenticationManager authenticationManager) {
		this.jwtProvider = jwtProvider;
		this.authenticationManager = authenticationManager;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/authenticate")
	public Mono<ResponseEntity> authenticate(@RequestBody Mono<AuthenticateRequest> authRequest) {

		Mono<Authentication> authentication = authRequest.flatMap(req -> authenticationManager.authenticate( 
				new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()))
		);
		
		Mono m = authentication
			.map(auth -> jwtProvider.createToken(auth))
			.map(jwt -> {
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.add(HttpHeaders.AUTHORIZATION, JwtAuthenticationFilter.BEARER_PREFIX + jwt);

				Map<String, String> tokenBody = Map.of("token", jwt);
				return new ResponseEntity(tokenBody, httpHeaders, HttpStatus.OK);
			});
	
		return m;
	}
}
