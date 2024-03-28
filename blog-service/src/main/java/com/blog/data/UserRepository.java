package com.blog.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.model.pojo.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional <User> findBySub(String sub);
	boolean existsBySub(String sub);
}
