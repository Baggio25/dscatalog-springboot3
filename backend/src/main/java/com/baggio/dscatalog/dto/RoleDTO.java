package com.baggio.dscatalog.dto;

import java.util.HashSet;
import java.util.Set;

import com.baggio.dscatalog.entities.Role;

public class RoleDTO {

	private Long id;
	private String authority;	
	private Set<UserDTO> users = new HashSet<>();

	public RoleDTO() {

	}

	public RoleDTO(Long id, String authority) {
		this.id = id;
		this.authority = authority;
	}

	public RoleDTO(Role role) {
		id = role.getId();
		authority = role.getAuthority();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public Set<UserDTO> getUsers() {
		return users;
	}
	
}
