package com.becoder.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.becoder.ResourceNotFoundException;
import com.becoder.dto.CategoryDto;
import com.becoder.dto.CategoryResponse;
import com.becoder.entity.Category;
import com.becoder.repository.CategoryRepository;
import com.becoder.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public Boolean saveCategory(CategoryDto categoryDto) {
		Category category = mapper.map(categoryDto, Category.class);
		
		if(ObjectUtils.isEmpty(category.getId())) {			
			category.setIsDeleted(false);
//			category.setCreatedBy(1);
//			category.setCreatedOn(new Date());
		} else {
			updateCategory(category);
		}
		Category saveCategory = categoryRepo.save(category);
		// Check whether the category returned as null or saved successfully! 
		if(ObjectUtils.isEmpty(saveCategory)) {
			return false;
		}
		return true;
	}

	private void updateCategory(Category category) {
		
		Optional<Category> findById = categoryRepo.findById(category.getId());
		if(findById.isPresent()) {
			Category existingCategory = findById.get();
			category.setIsDeleted(existingCategory.getIsDeleted());
			category.setCreatedBy(existingCategory.getCreatedBy());
			category.setCreatedOn(existingCategory.getCreatedOn());
            // update below with logged in user	
//			category.setUpdatedBy(1);
//			category.setUpdatedOn(new Date());
		}
	}

	@Override
	public List<CategoryDto> getAllCategories() {
		List<Category> categories = categoryRepo.findByIsDeletedFalse();
		
		List<CategoryDto> categoryDtoList = categories.stream().map(cat -> mapper.map(cat, CategoryDto.class)).toList();
		return categoryDtoList;
	}
	
	@Override
	public List<CategoryResponse> getAllActiveCategories() {
		List<Category> categories = categoryRepo.findByIsActiveTrueAndIsDeletedFalse();
		
		List<CategoryResponse> categoryResponseList = categories.stream().map(cat -> mapper.map(cat, CategoryResponse.class)).toList();
		return categoryResponseList;
	}
	
	@Override
	public CategoryDto getCategoryById(Integer id) throws Exception {
		Category category = categoryRepo.findByIdAndIsDeletedFalse(id)
					.orElseThrow(() -> new ResourceNotFoundException("Category not found with id="+id));
		if(!ObjectUtils.isEmpty(category)) {
			return mapper.map(category, CategoryDto.class);
		}
		return null;
	}
	
	@Override
	public Boolean deleteCategory(Integer id) {
		Optional<Category> findByCategory = categoryRepo.findById(id);
		
		if(findByCategory.isPresent()) {
			Category category = findByCategory.get();
			category.setIsDeleted(true);
			categoryRepo.save(category);
			return true;
		}
		
		return false;
	}

}
