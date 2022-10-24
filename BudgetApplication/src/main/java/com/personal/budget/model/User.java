package com.personal.budget.model;

import javax.validation.constraints.Email;

import lombok.Data;

@Data
public class User {
	
	
	private Long id;
	private String username;
	private String password;
	private String authority;
	
	@Email
	private String email;

}
