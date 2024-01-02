package com.authserver2.api.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.authserver2.api.model.pojo.User;
import com.authserver2.api.model.request.CreateUser;
import com.authserver2.api.model.request.LoginUser;
import com.authserver2.api.service.AuthenticationService;
import com.authserver2.api.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
	
	private final UserService userService;
	private final AuthenticationService authService;
	
	@PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody CreateUser request) {
		try {
			User user = userService.register(request);
			if (user != null) {
	            return ResponseEntity.status(HttpStatus.OK).body(user);
	        } else {
	            return ResponseEntity.status(HttpStatus.CONFLICT).build();
	        }
		} catch (Exception e) {
			//System.err.println("Error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
    }
	
	@PostMapping("/login")
	public ResponseEntity<Void> login(@RequestBody @Valid LoginUser request) {
		try {
			String token = authService.authenticate(request.getUsername(), request.getPassword());
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + token);
			return ResponseEntity.status(HttpStatus.OK).headers(headers).build();
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}	
}