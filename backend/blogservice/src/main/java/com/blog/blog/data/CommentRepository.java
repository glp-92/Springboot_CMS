package com.blog.blog.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.blog.model.pojo.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
