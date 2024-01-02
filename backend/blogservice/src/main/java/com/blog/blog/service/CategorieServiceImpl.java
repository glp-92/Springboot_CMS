package com.blog.blog.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.blog.data.CategorieRepository;
import com.blog.blog.model.pojo.Categorie;
import com.blog.blog.model.pojo.Post;
import com.blog.blog.model.request.CreateCategorie;
import com.blog.blog.model.request.EditCategorie;

@Service
public class CategorieServiceImpl implements CategorieService {
	
	@Autowired
	private CategorieRepository repository;

	@Override
	public Categorie createCategorie(CreateCategorie request) {
		Set<Post> posts = new HashSet<Post>(); // No hashset para admitir duplicados
		Categorie categorie = Categorie.builder().name(request.getName()).slug(request.getSlug()).posts(posts).build();
		return repository.save(categorie);
	}

	@Override
	public Categorie getCategorie(String categorieName) {
		return repository.findByName(categorieName);
	}

	@Override
	public List<Categorie> getAllCategories() {
		return repository.findAll();
	}

	@Override
	public Categorie editCategorie(EditCategorie request) {
		if (request.getCategorieId() != null) {
			Optional<Categorie> categorie_opt = repository.findById(Long.valueOf(request.getCategorieId()));
			if (categorie_opt.isEmpty()) {
	            return null;
	        }
			Categorie categorie = categorie_opt.get();
			if (request.getName() != null) {
				categorie.setName(request.getName());
			}
			if (request.getSlug() != null) {
				categorie.setSlug(request.getSlug());
			}
			return repository.save(categorie);
		}
		return null;
	}

	@Override
	public boolean deleteCategorie(String categorieId) {
		if (repository.existsById(Long.valueOf(categorieId))) {
			repository.deleteById(Long.valueOf(categorieId));
			return !repository.existsById(Long.valueOf(categorieId));
	    } else {
	        return false;
	    }
		
	}

}
