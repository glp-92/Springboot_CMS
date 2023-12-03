package com.gateway.router.service;

public interface TokenService {
	String getTokenFrom(String bearerToken);
	String getSubjectFrom(String token);
}
