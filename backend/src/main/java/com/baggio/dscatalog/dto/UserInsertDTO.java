package com.baggio.dscatalog.dto;

import jakarta.validation.constraints.NotBlank;

public class UserInsertDTO extends UserDTO{

	@NotBlank(message = "O campo [password] é obrigatório")
	private String password;

	public UserInsertDTO() {
		super(); 
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
