package com.blog.blog.model.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditPost {
	private String postId;
	private String title;
	private String excerpt;
	private String content;
	private String date;
	private String featuredImage;
	private Boolean featuredPost;
	private List<Long> categoryIds; // Lista de identificadores de categorías
}
