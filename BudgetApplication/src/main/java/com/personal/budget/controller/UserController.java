package com.personal.budget.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.personal.budget.model.User;
import com.personal.budget.repository.UserRepository;

@RestController
@RequestMapping("/user/")
public class UserController {
	
	private final UserRepository userRepository;

	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@PostMapping("newuser")
	public ResponseEntity<?> registerUser(@RequestBody @Valid User user, BindingResult bindingResult, 
			HttpServletResponse httpServletResponse){
		
		if (bindingResult.hasErrors()) {
			httpServletResponse.setStatus(400);
			return new ResponseEntity<>("missing user", HttpStatus.BAD_REQUEST);
		}
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword()));
		user.setAuthority("USER");
		userRepository.save(user);

		return new ResponseEntity<>("Perfectly fine", HttpStatus.OK);
	}
	
}
