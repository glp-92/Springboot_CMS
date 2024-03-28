package com.blog.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.model.pojo.Categorie;


public interface CategorieRepository extends JpaRepository<Categorie, Long> {
	Categorie findByName(String name);
	List<Categorie> findAllById(Iterable<Long> ids);
}
