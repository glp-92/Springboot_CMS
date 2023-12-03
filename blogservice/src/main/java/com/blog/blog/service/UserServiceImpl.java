package com.blog.blog.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.blog.blog.data.UserRepository;
import com.blog.blog.model.pojo.User;
import com.blog.blog.model.request.EditUser;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Override //From userDetails
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

	@Override
	public User getUser(String username) {
		return userRepo.findByUsername(username).get();
	}

	@Override
	public User editUser(EditUser request) {
		Optional<User> user_opt = userRepo.findByUsername(request.getUsername());
		User user = user_opt.get();
		if (user == null) {
			return null;
		}
		if (request.getName() != null) {
			user.setName(request.getName());
		}
		if (request.getBio() != null) {
			user.setBio(request.getBio());
		}
		if (request.getPicture() != null) {
			user.setPicture(request.getPicture());
		}
		return userRepo.save(user);
	}

	
}
