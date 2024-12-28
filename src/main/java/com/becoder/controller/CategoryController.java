package com.becoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.entity.Category;
import com.becoder.service.CategoryService;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

	@Autowired
	CategoryService categoryService;
	
	@PostMapping("/save-category")
	public ResponseEntity<?> saveCategory(@RequestBody Category category) {
		Boolean saveCategory = categoryService.saveCategory(category);
		if(saveCategory) {
			return new ResponseEntity<>("Saved Successfully", HttpStatus.CREATED);			
		}
		return new ResponseEntity<>("Failed to Save",HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/category")
	public ResponseEntity<?> getAllCategory() {
		List<Category> Categories = categoryService.getAllCategories();
		if(CollectionUtils.isEmpty(Categories)) {
			return ResponseEntity.noContent().build();	
		}
		return new ResponseEntity<>(Categories, HttpStatus.OK);
	}
}
