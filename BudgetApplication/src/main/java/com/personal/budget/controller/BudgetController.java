package com.personal.budget.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BudgetController {
	
	@GetMapping("/budget")
	public String budget() {
		return "index.html";
	}
	

}
