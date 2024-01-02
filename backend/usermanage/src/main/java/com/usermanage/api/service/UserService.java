package com.usermanage.api.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.usermanage.api.model.pojo.User;
import com.usermanage.api.model.request.ActivateUser;
import com.usermanage.api.model.request.SetUserRole;

public interface UserService {
	User assignRoleToUser(SetUserRole request);
	User activateUser(ActivateUser request);
	List<User> getAllUsers();
	UserDetails loadUserByUsername(String username);
	boolean deleteUser(String username);
}
