package com.personal.budget.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class User {
	
	
	private Long id;
	
	@NotBlank
	private String username;
	
	@NotBlank
	private String password;
	
	private String authority;
	
	@Email
	@NotBlank
	private String email;

}
