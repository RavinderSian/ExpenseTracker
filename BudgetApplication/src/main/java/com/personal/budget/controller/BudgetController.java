package com.personal.budget.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.personal.budget.model.Expense;
import com.personal.budget.service.ExpenseService;

@Controller
public class BudgetController {
	
	@Autowired
	private ExpenseService service;
	
	@GetMapping("/budget")
	public String budget(Model model, HttpServletRequest request) {
		
		model.addAttribute("expenses", service.findAll());
		model.addAttribute("newExpense", new Expense());
		//model.addAttribute("loggedInUser", request.getUserPrincipal().getName());
		
		return "budget";
	}
	
	@PostMapping("/addexpense")
	public String budget(@RequestBody Expense expense, Model model, HttpServletRequest request) {
		
		String loggedInUsername = request.getUserPrincipal().getName();
		
		return "budget";
	}

}
