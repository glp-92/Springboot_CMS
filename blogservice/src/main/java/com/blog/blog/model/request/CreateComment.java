package com.blog.blog.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateComment {
	private String name;
	private String email;
	private String comment;
	private Long postId;
}
