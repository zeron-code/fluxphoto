package org.fluxphoto.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler {

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<String> handleException(Exception ex) throws Exception {
		
		if (ex instanceof BadCredentialsException) {
			throw ex;
		}
		
		return ResponseEntity.internalServerError().build();
	}
}
