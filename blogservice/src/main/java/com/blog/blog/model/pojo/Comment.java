package com.blog.blog.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "comment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column
	@NotBlank(message = "field name cannot be empty!")
	private String name;
	
	@Column
	@NotBlank(message = "field email cannot be empty!")
	private String email;
	
	@Column
	@NotBlank(message = "field comment cannot be empty!")
	private String comment;
	
	@ManyToOne
    @JoinColumn(name = "post_id") // Nombre de la columna que contiene la clave foránea
	@JsonBackReference
	private Post post;
	
}
