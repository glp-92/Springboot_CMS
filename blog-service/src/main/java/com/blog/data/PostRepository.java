package com.blog.data;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.model.pojo.Categorie;
import com.blog.model.pojo.Post;

public interface PostRepository extends JpaRepository<Post, Long>{
	List<Post> findByCategoriesContaining(Categorie categorie);
	List<Post> findByTitleContainingOrContentContainingIgnoreCase(String keyTitle, String keyContent);
	List<Post> findByCategoriesContainingAndTitleContainingOrContentContainingIgnoreCase(Categorie categorie, String keyTitle, String keyContent);
	Page<Post> findAllByIdIn(Iterable<Long> postIds, Pageable pageable);
	Post findBySlug(String postSlug);
}
