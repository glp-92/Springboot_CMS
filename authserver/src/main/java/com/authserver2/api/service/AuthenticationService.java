package com.authserver2.api.service;

public interface AuthenticationService {
	String authenticate(String username, String password);
}
