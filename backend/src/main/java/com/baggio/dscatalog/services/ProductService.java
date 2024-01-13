package com.baggio.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baggio.dscatalog.dto.ProductDTO;
import com.baggio.dscatalog.entities.Product;
import com.baggio.dscatalog.repositories.ProductRepository;
import com.baggio.dscatalog.services.exceptions.DatabaseException;
import com.baggio.dscatalog.services.exceptions.ResourceNotFoundException;
import com.baggio.dscatalog.util.Constants;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAll(Pageable pageable) {
		Page<Product> page = productRepository.findAll(pageable); 
		return page.map(product -> new ProductDTO(product));		
	}
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> productOpt =  productRepository.findById(id);
		Product product = productOpt.orElseThrow(() -> new ResourceNotFoundException(Constants.ENTIDADE_NAO_ENCONTRADA));
		
		return new ProductDTO(product, product.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO productDTO) {
		Product product = 
				new Product(null, productDTO.getName(), productDTO.getDescription(), productDTO.getPrice(), null, null);
		product = productRepository.save(product);
		
		return new ProductDTO(product);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO productDTO) {
		try {
			Product product = productRepository.getReferenceById(id);
			product.setName(productDTO.getName());
			product = productRepository.save(product);
			
			return new ProductDTO(product);		
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(Constants.ENTIDADE_NAO_ENCONTRADA);
		}
	}

	public void delete(Long id) {
		if(!productRepository.existsById(id)) {
			throw new ResourceNotFoundException(Constants.ENTIDADE_NAO_ENCONTRADA);
		}
		
		try {
			productRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(Constants.FALHA_DE_INTEGRIDADE);
		}
	}
	
	
}
