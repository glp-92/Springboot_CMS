package com.usermanage.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.usermanage.api.model.pojo.User;
import com.usermanage.api.model.request.ActivateUser;
import com.usermanage.api.model.request.SetUserRole;
import com.usermanage.api.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserManageController {
	
	private final UserService userService;
	
	@PostMapping("/role")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<User> setUserRole(@RequestBody SetUserRole request) {
		try {
			User user = userService.assignRoleToUser(request);
			if (user != null) {
				return ResponseEntity.status(HttpStatus.OK).body(user);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		} catch (Exception e) {
			//System.err.println("Error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping("/activate")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<User> setActiveStatusToUser(@RequestBody ActivateUser request) {
		try {
			System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
			String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
			if (authenticatedUsername.equals(request.getUsername())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			User user = userService.activateUser(request);
			if (user != null) {
				return ResponseEntity.status(HttpStatus.OK).body(user);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		} catch (Exception e) {
			// System.err.println("Error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
