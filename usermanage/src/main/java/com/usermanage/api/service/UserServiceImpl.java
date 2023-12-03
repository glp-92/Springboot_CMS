package com.usermanage.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usermanage.api.data.UserRepository;
import com.usermanage.api.model.pojo.User;
import com.usermanage.api.model.request.ActivateUser;
import com.usermanage.api.model.request.SetUserRole;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	
	@Autowired
	private UserRepository userRepo;
	
	
	public User assignRoleToUser(SetUserRole request) {
		User user = userRepo.findByUsername(request.getUsername()).orElse(null);
	    if (user == null) {
	        return user;
	    }
	    user.setRole(request.getRole());
	    return userRepo.save(user);
	}
	
	public List<User> getAllUsers() {
		return userRepo.findAll();
	}
	
	@Override
	public User activateUser(ActivateUser request) {
		User user = userRepo.findByUsername(request.getUsername()).orElse(null);
		if (user == null) {
	        return user;
	    }
		user.setActive(request.getActive());
		return userRepo.save(user);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
	
	@Override
	@Transactional
	public boolean deleteUser(String username) {
	    if (userRepo.existsByUsername(username)) {
	    	userRepo.delete(userRepo.findByUsername(username).get());
	        return !userRepo.existsByUsername(username);
	    } else {
	        return false;
	    }
	}

}
