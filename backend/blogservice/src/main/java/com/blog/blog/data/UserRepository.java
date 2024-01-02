package com.blog.blog.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.blog.model.pojo.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional <User> findByUsername(String username);
	boolean existsByUsername(String username);
}
