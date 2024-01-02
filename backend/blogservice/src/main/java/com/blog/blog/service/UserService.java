package com.blog.blog.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.blog.blog.model.pojo.User;
import com.blog.blog.model.request.EditUser;

public interface UserService {
	UserDetails loadUserByUsername(String username);
	User getUser(String username);
	User editUser(EditUser request);
}
