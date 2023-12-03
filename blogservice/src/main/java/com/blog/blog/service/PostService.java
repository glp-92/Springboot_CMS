package com.blog.blog.service;

import java.util.List;

import com.blog.blog.model.pojo.Post;
import com.blog.blog.model.request.CreatePost;
import com.blog.blog.model.request.EditPost;

public interface PostService {
	Post createPost(CreatePost request); //Al devolver directamente el contenido del post te puede redirigir a la pagina
	Post editPost(EditPost request);
	List<Post> getAllPosts();
	boolean deletePost(String postId);
	/*Post getPostByAuthor(String author);
	Post getPostByCategorie(String categorieId);*/
}
