package com.authserver2.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.authserver2.api.data.UserRepository;
import com.authserver2.api.model.pojo.Role;
import com.authserver2.api.model.pojo.User;
import com.authserver2.api.model.request.CreateUser;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	public User register(CreateUser request) {
		if (userRepo.existsByUsername(request.getUsername())) {
	        return null;
	    }
	    User user = User.builder()
	        .email(request.getEmail())
	        .password(passwordEncoder.encode(request.getPassword()))
	        .username(request.getUsername())
	        .active(request.getActive())
	        .role(Role.USER)
	        .build();
		return userRepo.save(user);
    }
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


}
