package com.baggio.dscatalog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baggio.dscatalog.dto.CategoryDTO;
import com.baggio.dscatalog.entities.Category;
import com.baggio.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	public List<CategoryDTO> findAll() {
		List<Category> categories = categoryRepository.findAll(); 
		return categories.stream().map(category -> new CategoryDTO(category)).toList();
		
	}
	
	public CategoryDTO findById(Long id) {
		Optional<Category> categoryOpt =  categoryRepository.findById(id);
		return new CategoryDTO(categoryOpt.get());
	}
	
	
}
