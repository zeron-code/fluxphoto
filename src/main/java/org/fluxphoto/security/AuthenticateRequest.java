package org.fluxphoto.security;

/**
 * A username and password authentication request model 
 */
public class AuthenticateRequest {

	private String username;
	private String password;
	
	public AuthenticateRequest() {}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
