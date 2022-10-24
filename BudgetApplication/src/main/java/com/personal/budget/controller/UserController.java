package com.personal.budget.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.personal.budget.model.User;
import com.personal.budget.repository.UserRepository;

@Controller
@RequestMapping("user/")
public class UserController {
	
	private final UserRepository userRepository;

	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("newuser")
	public String registerUser(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}

	@PostMapping("newuser")
	public String registerUser(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model, 
			HttpServletResponse httpServletResponse){
		
		if (bindingResult.hasErrors()) {
			httpServletResponse.setStatus(400);
			return "register";
		}
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(user);

		return "redirect:/";
	}
	
}
