package com.blog.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.model.pojo.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {}