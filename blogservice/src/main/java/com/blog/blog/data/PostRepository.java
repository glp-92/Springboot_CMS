package com.blog.blog.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.blog.model.pojo.Post;

public interface PostRepository extends JpaRepository<Post, Long>{

}
