package com.baggio.dscatalog.dto;

import java.util.HashSet;
import java.util.Set;

import com.baggio.dscatalog.entities.User;

public class UserDTO {

	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	
	private Set<RoleDTO> roles = new HashSet<>();
	
	public UserDTO() {
	}

	public UserDTO(Long id, String firstName, String lastName, String email, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	public UserDTO(User user) {
		id = user.getId();
		firstName = user.getFirstName();
		lastName = user.getLastName();
		email = user.getEmail();
		password = user.getPassword();
		
		user.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}
	
}
