package com.blog.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.model.pojo.Categorie;
import com.blog.model.dto.CategorieCreate;
import com.blog.model.dto.CategorieEdit;
import com.blog.service.CategorieService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CategorieController {
	
	private final CategorieService service;
	
	@GetMapping("/categorie")
	public ResponseEntity<List<Categorie>> getCategories (
			@RequestParam(required = false) String name) {
		try {
			List<Categorie> categories = new ArrayList<>();
			if (name != null) {
				Categorie categorie = service.getCategorieByName(name);
				categories.add(categorie);
			}
			else {
				categories = service.getAllCategories();
			}
			if (categories != null) {
	            return ResponseEntity.status(HttpStatus.OK).body(categories);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping("/categorie")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Categorie> createCategorie (
			@RequestBody CategorieCreate request) {
		try {
			Categorie categorie = service.createCategorie(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(categorie);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PutMapping("/categorie")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Categorie> editCategorie (
			@RequestBody CategorieEdit request) {
		try {
			Categorie categorie = service.editCategorie(request);
			if (categorie != null) {
				return ResponseEntity.status(HttpStatus.OK).body(categorie);
			}
			else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@DeleteMapping("/categorie/{categorieId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteCategorie (
			@PathVariable String categorieId) {
		try {
            boolean deleted = service.deleteCategorie(categorieId);
            if (deleted) {
            	return ResponseEntity.noContent().build();
            }
            else {
            	return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
        } 
	}

}
