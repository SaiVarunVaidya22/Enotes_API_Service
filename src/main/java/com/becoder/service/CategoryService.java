package com.becoder.service;

import java.util.List;

import com.becoder.dto.CategoryDto;
import com.becoder.dto.CategoryResponse;

public interface CategoryService {
	
	public Boolean saveCategory(CategoryDto category);
	
	public List<CategoryDto> getAllCategories();
	
	public List<CategoryResponse> getAllActiveCategories();

	public CategoryDto getCategoryById(Integer id);

	public Boolean deleteCategory(Integer id);
	
}
