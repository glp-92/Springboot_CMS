package com.blog.service;

import java.util.List;

import com.blog.model.pojo.Categorie;
import com.blog.model.dto.CategorieCreate;
import com.blog.model.dto.CategorieEdit;

public interface CategorieService {
	List<Categorie> getAllCategories();
	Categorie getCategorieByName(String categorieName);
	Categorie createCategorie(CategorieCreate request);
	Categorie editCategorie(CategorieEdit request);
	boolean deleteCategorie(String categorieId);
}
