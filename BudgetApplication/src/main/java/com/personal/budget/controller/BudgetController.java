package com.personal.budget.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BudgetController {
	
	@RequestMapping("/budget")
	public String budget() {
		return "budget";
	}

}
