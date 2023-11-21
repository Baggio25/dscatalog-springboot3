package com.baggio.dscatalog.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baggio.dscatalog.entities.Category;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {
	
	@GetMapping
	public ResponseEntity<List<Category>> findAll() {
		List<Category> list = new ArrayList<Category>();
		list.add(new Category(1L, "Livros"));
		list.add(new Category(2L, "Eletrônicos"));
		list.add(new Category(1L, "Escolar"));
		
		return ResponseEntity.ok(list);
	}

}
