package com.authserver2.api.service;

import com.authserver2.api.model.pojo.User;

public interface TokenService {
	String getTokenFrom(String bearerToken);
	String getSubjectFrom(String token);
	String generateToken(User user);
}
