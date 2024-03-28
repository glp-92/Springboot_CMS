package com.blog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.model.pojo.Comment;
import com.blog.model.dto.CommentCreate;
import com.blog.model.dto.CommentEdit;
import com.blog.service.CommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommentController {

	private final CommentService service;
	
	@GetMapping("/comment")
	@PreAuthorize("permitAll()")
	public ResponseEntity<List<Comment>> getCommentByPostId (
			@RequestParam(required = false) String postId) {
		try {
			List<Comment> comments;
			if (postId != null) {
				comments = service.getCommentsFromPost(postId);
			}
			else {
				comments = service.getAllComments();
			}
			if (comments != null) {
	            return ResponseEntity.status(HttpStatus.OK).body(comments);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping("/comment")
	//@PreAuthorize("hasRole('ADMIN')")
	@PreAuthorize("permitAll()")
	public ResponseEntity<Comment> createComment (
			@Valid @RequestBody CommentCreate request) {
		try {
			Comment comment = service.createComment(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(comment);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PutMapping("/comment")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Comment> editComment (
			@RequestBody CommentEdit request) {
		try {
			Comment comment = service.editComment(request);
			if (comment != null) {
				return ResponseEntity.status(HttpStatus.OK).body(comment);
			}
			else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@DeleteMapping("/comment/{commentId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteComment (
			@PathVariable String commentId) {
		try {
            boolean deleted = service.deleteComment(commentId);
            if (deleted) {
            	return ResponseEntity.noContent().build();
            }
            else {
            	return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
        } 
	}
	
}
