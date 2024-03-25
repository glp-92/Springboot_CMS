package com.blog.blog.model.pojo;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "categorie")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Categorie {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(unique = true)
	@NotBlank(message = "field name cannot be empty!")
	private String name;
	
	@Column(unique = true)
	@NotBlank(message = "field slug cannot be empty!")
	private String slug;
	
	@ManyToMany(mappedBy = "categories")
	@JsonBackReference // is the back part of reference; it’ll be omitted from serialization
	//@JsonIgnore // Solo se serializara la lista de posts cuando se acceda a ella a traves de categorie
    private Set<Post> posts;
	
	@PreRemove //Funcion llamada previamente a remover una entidad por JPA
    private void onRemove() {
        for (Post post : posts) {
            post.getCategories().remove(this); // Remueve la categoria del Post, antes de ser esta eliminada
        }
    }
}
