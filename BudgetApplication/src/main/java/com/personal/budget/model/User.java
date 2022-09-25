package com.personal.budget.model;

import javax.validation.constraints.Email;

import lombok.Data;

@Data
public class User {
	
	
	private Long id;
	private String username;
	private String password;
	
	@Email
	private String email;

}
