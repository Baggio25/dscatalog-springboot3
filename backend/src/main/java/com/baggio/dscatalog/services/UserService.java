package com.baggio.dscatalog.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baggio.dscatalog.dto.RoleDTO;
import com.baggio.dscatalog.dto.UserDTO;
import com.baggio.dscatalog.dto.UserInsertDTO;
import com.baggio.dscatalog.dto.UserUpdateDTO;
import com.baggio.dscatalog.entities.Role;
import com.baggio.dscatalog.entities.User;
import com.baggio.dscatalog.projections.UserDetailsProjection;
import com.baggio.dscatalog.repositories.RoleRepository;
import com.baggio.dscatalog.repositories.UserRepository;
import com.baggio.dscatalog.services.exceptions.DatabaseException;
import com.baggio.dscatalog.services.exceptions.ResourceNotFoundException;
import com.baggio.dscatalog.util.Constants;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAll(Pageable pageable) {
		Page<User> page = userRepository.findAll(pageable); 
		return page.map(user -> new UserDTO(user));		
	}
	
	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> userOpt =  userRepository.findById(id);
		User user = userOpt.orElseThrow(() -> new ResourceNotFoundException(Constants.RECURSO_NAO_ENCONTRADO));
		
		return new UserDTO(user);
	}

	@Transactional
	public UserDTO insert(UserInsertDTO userDTO) {
		User user = new User();
		copyDtoToEntity(userDTO, user);
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		
		user = userRepository.save(user);
		return new UserDTO(user);
	}

	@Transactional
	public UserDTO update(Long id, UserUpdateDTO userDTO) {
		try {
			User user = userRepository.getReferenceById(id);
			copyDtoToEntity(userDTO, user);
			user = userRepository.save(user);
			
			return new UserDTO(user);		
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(Constants.RECURSO_NAO_ENCONTRADO);
		}
	}

	public void delete(Long id) {
		if(!userRepository.existsById(id)) {
			throw new ResourceNotFoundException(Constants.RECURSO_NAO_ENCONTRADO);
		}
		
		try {
			userRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(Constants.FALHA_DE_INTEGRIDADE);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<UserDetailsProjection> result = userRepository.searchUserAndRolesByEmail(username);
		
		if(result.size() == 0) {
			throw new UsernameNotFoundException("User not found");
		}
		
		User user = new User();
		user.setEmail(username);
		user.setPassword(result.get(0).getPassword());
		
		for(UserDetailsProjection projection : result) {
			user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
		}
	
		return user;
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
