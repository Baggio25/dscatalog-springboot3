package com.baggio.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baggio.dscatalog.dto.CategoryDTO;
import com.baggio.dscatalog.dto.ProductDTO;
import com.baggio.dscatalog.entities.Category;
import com.baggio.dscatalog.entities.Product;
import com.baggio.dscatalog.repositories.CategoryRepository;
import com.baggio.dscatalog.repositories.ProductRepository;
import com.baggio.dscatalog.services.exceptions.DatabaseException;
import com.baggio.dscatalog.services.exceptions.ResourceNotFoundException;
import com.baggio.dscatalog.util.Constants;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAll(Pageable pageable) {
		Page<Product> page = productRepository.findAll(pageable); 
		return page.map(product -> new ProductDTO(product));		
	}
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> productOpt =  productRepository.findById(id);
		Product product = productOpt.orElseThrow(() -> new ResourceNotFoundException(Constants.RECURSO_NAO_ENCONTRADO));
		
		return new ProductDTO(product, product.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO productDTO) {
		Product product = new Product();
		copyDtoToEntity(productDTO, product);
		product = productRepository.save(product);
		
		return new ProductDTO(product);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO productDTO) {
		try {
			Product product = productRepository.getReferenceById(id);
			copyDtoToEntity(productDTO, product);
			product = productRepository.save(product);
			
			return new ProductDTO(product);		
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(Constants.RECURSO_NAO_ENCONTRADO);
		}
	}

	public void delete(Long id) {
		if(!productRepository.existsById(id)) {
			throw new ResourceNotFoundException(Constants.RECURSO_NAO_ENCONTRADO);
		}
		
		try {
			productRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(Constants.FALHA_DE_INTEGRIDADE);
		}
	}
	
	private void copyDtoToEntity(ProductDTO productDTO, Product product) {
		product.setName(productDTO.getName());
		product.setDescription(productDTO.getDescription());
		product.setDate(productDTO.getDate());
		product.setImgUrl(productDTO.getImgUrl());
		product.setPrice(productDTO.getPrice());
		
		product.getCategories().clear();
		
		for (CategoryDTO categoryDTO : productDTO.getCategories()) {
			Category category = categoryRepository.getReferenceById(categoryDTO.getId());
			product.getCategories().add(category);
		}
	}

	
}
