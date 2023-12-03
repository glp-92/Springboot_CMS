package com.blog.blog.model.pojo;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "post")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column
	@NotBlank(message = "field title cannot be empty!")
	private String title;
	
	@Column(unique = true) 
	@NotBlank(message = "field slug cannot be empty!")
	private String slug;
	
	@Column
	@NotBlank(message = "field excerpt cannot be empty!")
	private String excerpt;
	
	@Column
	@NotBlank(message = "field content cannot be empty!")
	private String content;
	
	@Column
	@NotNull(message = "field date cannot be empty!") //NotBlank no se aplica en campo date
	private Date date;
	
	@Column
	@NotBlank(message = "field featuredImage cannot be empty!")
	private String featuredImage;
	
	@Column
	@NotNull(message = "field featuredPost cannot be empty!") //NotBlank no se aplica en booleans
	private Boolean featuredPost;
	
	@ManyToMany
	@JoinTable(
	  name = "post_categorie", 
	  joinColumns = @JoinColumn(name = "post_id"), 
	  inverseJoinColumns = @JoinColumn(name = "categorie_id")
	 )
	@JsonManagedReference // the one that gets serialized normally
    private Set<Categorie> categories;
	
	@ManyToOne
	@JoinColumn(name = "users_id")
	@JsonManagedReference // the one that gets serialized normally
	private User users;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true) //orpahnremoval y cascadetype, si se elimina el post, desaparece el comentario
	@JsonManagedReference
	private Set<Comment> comments;
	
}
