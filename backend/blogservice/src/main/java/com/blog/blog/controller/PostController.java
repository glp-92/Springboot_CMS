package com.blog.blog.controller;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.multipart.MultipartFile;

import com.blog.blog.model.dto.FullPostDto;
import com.blog.blog.model.pojo.Post;
import com.blog.blog.model.request.CreatePost;
import com.blog.blog.model.request.EditPost;
import com.blog.blog.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostController {
	
	private final PostService service;
	
	@GetMapping("/post")
	public ResponseEntity<Map<String, Object>> getFilteredPosts (
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "page", required = true) int page,
			@RequestParam(name = "reverse", required = false) boolean reverse
			) {
		try {
			if (keyword != null) {
				if (keyword.length() > 20 || !keyword.matches("[a-zA-Z0-9]+")) {
		            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		        }
			}
			Map<String, Object> posts = service.getPostsFiltered(keyword, page, reverse);
			if (!posts.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.OK).body(posts);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }
		} catch (Exception e) {
			//System.err.println("Error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/post/{postSlug}")
	public ResponseEntity<FullPostDto> getPostByUri (
			@PathVariable String postSlug
			) {
		try {
			Post post = service.getPostByUri(postSlug);
			if (post != null) {
				return ResponseEntity.status(HttpStatus.OK).body(new FullPostDto(post));
			} 
			else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		} catch (Exception e) {
			//System.err.println("Error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping("/post")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Post> createPost (
			@RequestBody CreatePost request) {
		try {
			Post post = service.createPost(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(post);
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PutMapping("/post")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Post> editPost (
			@RequestBody EditPost request) {
		try {
			Post post = service.editPost(request);
			if (post != null) {
				return ResponseEntity.status(HttpStatus.OK).body(post);
			} 
			else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		} catch (Exception e) {
			//System.err.println("Error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping("/post/{postId}/image-upload")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<String>> storeImages (
			@PathVariable String postId,
			@RequestParam("imageList") List<MultipartFile> imageList,
			@RequestParam("imagenameList") List<String> imagenameList
		) {
		try {
			List<String> paths = service.uploadImages(imageList, imagenameList);
			return ResponseEntity.status(HttpStatus.OK).body(paths);
		} catch (Exception e) {
			// System.err.println("Error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@DeleteMapping("/post/{postId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<String> deletePost (
			@PathVariable String postId) {
		try {
            boolean deleted = service.deletePost(postId);
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
