package com.blog.service;

import java.util.List;

import com.blog.model.pojo.Comment;
import com.blog.model.dto.CommentCreate;
import com.blog.model.dto.CommentEdit;

public interface CommentService {
	List<Comment> getAllComments();
	List<Comment> getCommentsFromPost(String postId);
	Comment createComment(CommentCreate request);
	Comment editComment(CommentEdit request);
	boolean deleteComment(String commentId);
}
