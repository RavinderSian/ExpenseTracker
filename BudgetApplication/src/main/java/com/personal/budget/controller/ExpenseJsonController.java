package com.personal.budget.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.personal.budget.service.ExpenseService;
import com.personal.budget.service.UserService;

@PreAuthorize("hasAuthority('USER')")
@RestController
public class ExpenseJsonController {
	
	private final ExpenseService service;
	private final UserService userService;
	
	public ExpenseJsonController(ExpenseService service, UserService userService) {
		this.service = service;
		this.userService = userService;
	}
	
	@GetMapping("/delete/{id}")
	public ResponseEntity<?> deleteExpense(@PathVariable Long id, HttpServletRequest request,
			 RedirectAttributes redirectAttributes) {
		
		service.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/search")
	public ResponseEntity<?> search(@RequestBody String searchString ,HttpServletRequest request) {
		
		Long userId = userService.findByUsername(request.getUserPrincipal().getName()).get().getId();

		service.getSearchResults(userId, searchString);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
