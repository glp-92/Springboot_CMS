package com.authserver2.api.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.authserver2.api.model.pojo.User;
import com.authserver2.api.model.request.CreateUser;

public interface UserService {
	User register(CreateUser request);
	UserDetails loadUserByUsername(String username);
}
