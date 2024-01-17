package com.baggio.dscatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baggio.dscatalog.dto.RoleDTO;
import com.baggio.dscatalog.dto.UserDTO;
import com.baggio.dscatalog.entities.Role;
import com.baggio.dscatalog.entities.User;
import com.baggio.dscatalog.repositories.RoleRepository;
import com.baggio.dscatalog.repositories.UserRepository;
import com.baggio.dscatalog.services.exceptions.DatabaseException;
import com.baggio.dscatalog.services.exceptions.ResourceNotFoundException;
import com.baggio.dscatalog.util.Constants;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAll(Pageable pageable) {
		Page<User> page = userRepository.findAll(pageable); 
		return page.map(user -> new UserDTO(user));		
	}
	
	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> userOpt =  userRepository.findById(id);
		User user = userOpt.orElseThrow(() -> new ResourceNotFoundException(Constants.ENTIDADE_NAO_ENCONTRADA));
		
		return new UserDTO(user);
	}

	@Transactional
	public UserDTO insert(UserDTO userDTO) {
		User user = new User();
		copyDtoToEntity(userDTO, user);
		user = userRepository.save(user);
		
		return new UserDTO(user);
	}

	@Transactional
	public UserDTO update(Long id, UserDTO userDTO) {
		try {
			User user = userRepository.getReferenceById(id);
			copyDtoToEntity(userDTO, user);
			user = userRepository.save(user);
			
			return new UserDTO(user);		
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(Constants.ENTIDADE_NAO_ENCONTRADA);
		}
	}

	public void delete(Long id) {
		if(!userRepository.existsById(id)) {
			throw new ResourceNotFoundException(Constants.ENTIDADE_NAO_ENCONTRADA);
		}
		
		try {
			userRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(Constants.FALHA_DE_INTEGRIDADE);
		}
	}
	
	private void copyDtoToEntity(UserDTO userDTO, User user) {
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setEmail(userDTO.getEmail());
		
		user.getRoles().clear();
		for(RoleDTO roleDTO : userDTO.getRoles()) {
			Role role = roleRepository.getReferenceById(roleDTO.getId());
			user.getRoles().add(role);
		}
	}

	
}
