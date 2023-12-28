package com.baggio.dscatalog.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baggio.dscatalog.dto.CategoryDTO;
import com.baggio.dscatalog.entities.Category;
import com.baggio.dscatalog.repositories.CategoryRepository;
import com.baggio.dscatalog.services.exceptions.ResourceNotFoundException;
import com.baggio.dscatalog.util.Constants;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> categories = categoryRepository.findAll(); 
		return categories.stream().map(category -> new CategoryDTO(category)).toList();		
	}
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> categoryOpt =  categoryRepository.findById(id);
		Category category = categoryOpt.orElseThrow(() -> new ResourceNotFoundException(Constants.ENTIDADE_NAO_ENCONTRADA));
		
		return new CategoryDTO(category);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO categoryDTO) {
		Category category = new Category(null, categoryDTO.getName());
		category = categoryRepository.save(category);
		
		return new CategoryDTO(category);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
		try {
		Category category = categoryRepository.getReferenceById(id);
		category.setName(categoryDTO.getName());
		category = categoryRepository.save(category);
		
		return new CategoryDTO(category);		
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(Constants.ENTIDADE_NAO_ENCONTRADA);
		}
	}
	
	
}
