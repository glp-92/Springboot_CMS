package com.blog.blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.blog.model.pojo.User;
import com.blog.blog.model.request.EditUser;
import com.blog.blog.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	
	private final UserService service;
	
	@GetMapping("/author")
	public ResponseEntity<User> getAuthor (
			@RequestParam String username) {
		try {
			User author = service.getUser(username);
			if (author != null) {
	            return ResponseEntity.status(HttpStatus.OK).body(author);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }
		} catch (Exception e) {
			// System.err.println("Error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PutMapping("/author")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<User> editAuthor (
			@RequestBody EditUser request) {
		try {
			User author = service.editUser(request);
			if (author != null) {
				return ResponseEntity.status(HttpStatus.OK).body(author);
			}
			else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		} catch (Exception e) {
			// System.err.println("Error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
}
