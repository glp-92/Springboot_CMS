package com.blog.model.pojo;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class User {
	
	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column
	@JsonIgnore
	private String sub; //subject from JWT
	
	@Column
	private String username;
	
	@Column
	private String picture;
	
	@Column
	private String bio;
	
	@OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
	// @JsonIgnore
	@JsonBackReference 
    private Set<Post> posts;

}