package com.blog.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostCreate {
	private String title;
	private String slug;
	private String excerpt;
	private String content;
	private String featuredImage;
	private Boolean featuredPost;
	private List<Long> categoryIds;
}
