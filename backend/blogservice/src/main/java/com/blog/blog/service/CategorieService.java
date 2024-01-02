package com.blog.blog.service;

import java.util.List;

import com.blog.blog.model.pojo.Categorie;
import com.blog.blog.model.request.CreateCategorie;
import com.blog.blog.model.request.EditCategorie;

public interface CategorieService {
	Categorie createCategorie(CreateCategorie request);
	Categorie editCategorie(EditCategorie request);
	boolean deleteCategorie(String categorieId);
	Categorie getCategorie(String categorieName);
	List<Categorie> getAllCategories();
}
