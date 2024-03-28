package com.blog.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.blog.model.pojo.Post;
import com.blog.model.dto.PostCreate;
import com.blog.model.dto.PostEdit;

public interface PostService {
	Map<String, Object> getPostsFiltered(String keyword, int page, boolean reverse);
	Post getPostByUri(String postSlug);
	Post createPost(PostCreate request, String subject); //Al devolver directamente el contenido del post te puede redirigir a la pagina
	List<String> uploadImages(List<MultipartFile> imageList, List<String> imagenameList) throws IllegalStateException, IOException;
	Post editPost(PostEdit request);
	boolean deletePost(String postId);
}
