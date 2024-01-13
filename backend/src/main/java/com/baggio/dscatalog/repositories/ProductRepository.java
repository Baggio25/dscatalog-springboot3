package com.baggio.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baggio.dscatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
