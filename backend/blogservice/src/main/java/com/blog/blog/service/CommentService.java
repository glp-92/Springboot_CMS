package com.blog.blog.service;

import java.util.List;

import com.blog.blog.model.pojo.Comment;
import com.blog.blog.model.request.CreateComment;
import com.blog.blog.model.request.EditComment;

public interface CommentService {
	Comment createComment(CreateComment request);
	Comment editComment(EditComment request);
	boolean deleteComment(String commentId);
	List<Comment> getCommentsFromPost(String postId);
	List<Comment> getAllComments();
}
