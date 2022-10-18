package com.personal.budget.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.personal.budget.service.ExpenseService;

@Controller
public class BudgetController {
	
	@Autowired
	private ExpenseService service;
	
	@RequestMapping("/budget")
	public String budget(Model model) {
		
		model.addAttribute("expenses", service.findAll());
		
		return "budget";
	}

}
