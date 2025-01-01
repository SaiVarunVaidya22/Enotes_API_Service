package com.becoder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.ResourceNotFoundException;
import com.becoder.dto.CategoryDto;
import com.becoder.dto.CategoryResponse;
import com.becoder.service.CategoryService;
import com.becoder.util.CommonUtil;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

	@Autowired
	CategoryService categoryService;
	
	@PostMapping("/save")
	public ResponseEntity<?> saveCategory(@Valid @RequestBody CategoryDto categoryDto) {
		
		Boolean saveCategory = categoryService.saveCategory(categoryDto);
		if(saveCategory) {
			return CommonUtil.createBuildResponseMessage("Saved Successfully", HttpStatus.CREATED);
		}
		return CommonUtil.createErrorResponseMessage("Failed t o Save", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/")
	public ResponseEntity<?> getAllCategory() {
		List<CategoryDto> Categories = categoryService.getAllCategories();
		if(CollectionUtils.isEmpty(Categories)) {
			return ResponseEntity.noContent().build();	
		}
		return CommonUtil.createBuildResponse(Categories, HttpStatus.OK);
	}
	
	@GetMapping("/active")
	public ResponseEntity<?> getAllActiveCategory() {
		List<CategoryResponse> Categories = categoryService.getAllActiveCategories();
		if(CollectionUtils.isEmpty(Categories)) {
			return ResponseEntity.noContent().build();	
		}
		return CommonUtil.createBuildResponse(Categories, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id) throws Exception {
		CategoryDto category = categoryService.getCategoryById(id);
		if(ObjectUtils.isEmpty(category)) {
			return CommonUtil.createErrorResponseMessage("Internal server error",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return CommonUtil.createBuildResponse(category, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id) {
		Boolean deleted = categoryService.deleteCategory(id);
		if(deleted) {
			return CommonUtil.createBuildResponseMessage("Category with id deleted", HttpStatus.OK);
		}
		return CommonUtil.createErrorResponseMessage("Category not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
