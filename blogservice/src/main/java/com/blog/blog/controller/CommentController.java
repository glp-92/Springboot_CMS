package com.blog.blog.controller;

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

import com.blog.blog.model.pojo.Comment;
import com.blog.blog.model.request.CreateComment;
import com.blog.blog.model.request.EditComment;
import com.blog.blog.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommentController {

	private final CommentService service;
	
	@GetMapping("/comment")
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
			//System.err.println("Error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping("/comment")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Comment> createComment (
			@RequestBody CreateComment request) {
		try {
			Comment comment = service.createComment(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(comment);
		} catch (Exception e) {
			//System.err.println("Error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PutMapping("/comment")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Comment> editComment (
			@RequestBody EditComment request) {
		try {
			Comment comment = service.editComment(request);
			if (comment != null) {
				return ResponseEntity.status(HttpStatus.OK).body(comment);
			}
			else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		} catch (Exception e) {
			//System.err.println("Error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@DeleteMapping("/comment/{commentId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
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
