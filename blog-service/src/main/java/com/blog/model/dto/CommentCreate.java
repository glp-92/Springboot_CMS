package com.blog.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreate {
	@NotEmpty(message="nombre no puede estar vacio")
	@Size(min = 3, max = 30)
	@Pattern(regexp = "[a-zA-Z0-9]+", message="inyeccion detectada")
	private String name;
	@NotEmpty(message="email no puede estar vacio")
	@Email(message="formato no validod e email")
	@Size(min = 3, max = 30)
	private String email;
	@NotEmpty(message = "comentario no puede estar vacío")
    @Size(max = 50, message = "El comentario no puede exceder los 100 caracteres")
	@Pattern(regexp = "[a-zA-Z0-9]+", message="inyeccion detectada")
	private String comment;
	private Long postId;
}
