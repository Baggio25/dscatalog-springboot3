package com.baggio.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.baggio.dscatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

  /**
   *SELECT DISTINCT
        tb_product.id, 
        tb_product.name
    FROM
        tb_product
    INNER JOIN tb_product_category ON tb_product.id = tb_product_category.product_id
    WHERE
          tb_product_category.category_id IN (1,3) AND
          LOWER(tb_product.name) LIKE '%ma%'
    ORDER BY
          tb_product.name
   */

}
