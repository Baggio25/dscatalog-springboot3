package com.baggio.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baggio.dscatalog.dto.CategoryDTO;
import com.baggio.dscatalog.entities.Category;
import com.baggio.dscatalog.repositories.CategoryRepository;
import com.baggio.dscatalog.services.exceptions.DatabaseException;
import com.baggio.dscatalog.services.exceptions.ResourceNotFoundException;
import com.baggio.dscatalog.util.Constants;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAll(Pageable pageable) {
		Page<Category> page = categoryRepository.findAll(pageable); 
		return page.map(category -> new CategoryDTO(category));		
	}
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> categoryOpt =  categoryRepository.findById(id);
		Category category = categoryOpt.orElseThrow(() -> new ResourceNotFoundException(Constants.RECURSO_NAO_ENCONTRADO));
		
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
			throw new ResourceNotFoundException(Constants.RECURSO_NAO_ENCONTRADO);
		}
	}

	public void delete(Long id) {
		if(!categoryRepository.existsById(id)) {
			throw new ResourceNotFoundException(Constants.RECURSO_NAO_ENCONTRADO);
		}
		
		try {
			categoryRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(Constants.FALHA_DE_INTEGRIDADE);
		}
	}
	
	
}
