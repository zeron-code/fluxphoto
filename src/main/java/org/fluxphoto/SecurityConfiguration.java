package org.fluxphoto;

import org.fluxphoto.security.JwtAuthenticationFilter;
import org.fluxphoto.security.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@EnableWebFluxSecurity
public class SecurityConfiguration {

	@Bean
	public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = 
				new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
		authenticationManager.setPasswordEncoder(passwordEncoder);
		return authenticationManager;
	}
	
	@Bean
	public MapReactiveUserDetailsService userDetailsService() {
        
		// The 'admin' user
		UserDetails admin =
			User.withUsername("admin").passwordEncoder(p -> passwordEncoder().encode(p))
				.password("admin")
				.roles("ADMIN")
				.build();
		
		// The 'demo' user
		UserDetails demo = 
			User.withUsername("demo").passwordEncoder(p -> passwordEncoder().encode(p))
				.password("demo")
				.roles("USER", "DEMO")
				.build();

        return new MapReactiveUserDetailsService(admin, demo);
    }
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, JwtProvider jwtProvider) {
		
		return http
				.csrf().disable()
				.formLogin().disable()
				.httpBasic().disable()
				.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
				.authorizeExchange()
					.pathMatchers("/authenticate").permitAll()
					.pathMatchers("/**").hasRole("ADMIN")
				.anyExchange().authenticated()
				.and()
				.addFilterAt(new JwtAuthenticationFilter(jwtProvider), SecurityWebFiltersOrder.HTTP_BASIC)
				.build();
    }	
}
