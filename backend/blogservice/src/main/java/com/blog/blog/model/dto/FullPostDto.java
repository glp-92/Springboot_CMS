package com.blog.blog.model.dto;

import java.util.Date;
import java.util.Set;

import com.blog.blog.model.pojo.Categorie;
import com.blog.blog.model.pojo.Comment;
import com.blog.blog.model.pojo.Post;
import com.blog.blog.model.pojo.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FullPostDto {
	private Long id;
	private String title;
	private String slug;
	private String excerpt;
	private String content;
	private Date date;
	private String featuredImage;
	private Boolean featuredPost;
	private Set<Categorie> categories;
	private User users;
	private Set<Comment> comments;
	public FullPostDto(Post post) {
	    this.id = post.getId();
	    this.title = post.getTitle();
	    this.slug = post.getSlug();
	    this.excerpt = post.getExcerpt();
	    this.content = post.getContent();
	    this.date = post.getDate();
	    this.featuredImage = post.getFeaturedImage();
	    this.featuredPost = post.getFeaturedPost();
	    this.categories = post.getCategories();
	    this.users = post.getUsers();
	    this.comments = post.getComments();
	  }
}
