package com.authserver2.api.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationFailedException extends AuthenticationException {
	
	private static final long serialVersionUID = 1L;

    public AuthenticationFailedException(String message) {
        super(message);
    }
}