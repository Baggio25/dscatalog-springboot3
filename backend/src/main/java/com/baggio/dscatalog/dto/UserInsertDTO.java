package com.baggio.dscatalog.dto;

import com.baggio.dscatalog.services.validation.UserInsertValid;

import jakarta.validation.constraints.NotBlank;

@UserInsertValid
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
