package com.baggio.dscatalog.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.baggio.dscatalog.dto.UserDTO;
import com.baggio.dscatalog.dto.UserInsertDTO;
import com.baggio.dscatalog.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserResource {
	
	@Autowired
	private UserService userService;
	
	@GetMapping
	public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable) {
		Page<UserDTO> userPage = userService.findAll(pageable);		
		return ResponseEntity.ok(userPage);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
		UserDTO dto = userService.findById(id);		
		return ResponseEntity.ok(dto);
	}
	
	@PostMapping
	public ResponseEntity<UserDTO> insert(@RequestBody UserInsertDTO userDTO) {
		UserDTO userNewDTO = userService.insert(userDTO);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(userNewDTO.getId()).toUri();
		
		return ResponseEntity.created(uri).body(userNewDTO);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<UserDTO> update(
			@PathVariable Long id, @RequestBody UserDTO userDTO) {		
		userDTO = userService.update(id, userDTO);			
		return ResponseEntity.ok(userDTO);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<UserDTO> delete(@PathVariable Long id) {		
		userService.delete(id);			
		return ResponseEntity.noContent().build();
	}
}
