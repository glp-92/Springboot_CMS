package com.blog.blog.model.pojo;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
public class User implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column
	@JsonIgnore
	private String username;
	
	@Column
	@NotBlank(message = "field name cannot be empty!")
	private String name;
	
	@Column
	private String picture;
	
	@Column
	private String bio;
	
	@Column
	@JsonIgnore
	private boolean active;
	
	@OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
	// @JsonIgnore
	@JsonBackReference 
    private Set<Post> posts;
	
	@Column
	@JsonIgnore
	@Enumerated(EnumType.STRING)
    private Role role;

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return active;
	}

	@Override
	@JsonIgnore
	public String getPassword() {
		return null;
	}
	
}
