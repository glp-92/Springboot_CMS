package com.blog.blog.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.blog.blog.model.pojo.Post;
import com.blog.blog.model.request.CreatePost;
import com.blog.blog.model.request.EditPost;

public interface PostService {
	Post createPost(CreatePost request); //Al devolver directamente el contenido del post te puede redirigir a la pagina
	Post editPost(EditPost request);
	List<Post> getAllPosts();
	boolean deletePost(String postId);
	Map<String, Object> getPostsFiltered(String keyword, int page, boolean reverse);
	Post getPostByUri(String postSlug);
	List<String> uploadImages(List<MultipartFile> imageList, List<String> imagenameList) throws IllegalStateException, IOException;
	// Post getPostByCategorie(String categorieId);*/
}
