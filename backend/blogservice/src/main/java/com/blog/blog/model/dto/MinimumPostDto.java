package com.blog.blog.model.dto;

import java.util.Date;
import java.util.Set;

import com.blog.blog.model.pojo.Categorie;
import com.blog.blog.model.pojo.Post;
import com.blog.blog.model.pojo.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MinimumPostDto {
	private Long id;
	private String title;
	private String slug;
	private String excerpt;
	private Date date;
	private String featuredImage;
	private Set<Categorie> categories;
	private User users;
	public MinimumPostDto(Post post) {
	    this.id = post.getId();
	    this.title = post.getTitle();
	    this.slug = post.getSlug();
	    this.excerpt = post.getExcerpt();
	    this.date = post.getDate();
	    this.featuredImage = post.getFeaturedImage();
	    this.categories = post.getCategories();
	    this.users = post.getUsers();
	  }
}
