package com.personal.budget.controller;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.personal.budget.model.Expense;
import com.personal.budget.service.ExpenseService;
import com.personal.budget.service.UserService;

@Controller
public class BudgetController {
	
	private final ExpenseService service;
	private final UserService userService;
	
	//private ConverterFactory converter = ConverterFactory<String, Cateory>;

	public BudgetController(ExpenseService service, UserService userService) {
		this.service = service;
		this.userService = userService;
	}
	
	@GetMapping("/budget")
	public String budget(Model model, HttpServletRequest request) {
		
		model.addAttribute("expenses", service.findAll());
		//model.addAttribute("loggedInUser", request.getUserPrincipal().getName());
		
		return "budget";
	}
	
	@GetMapping("/addexpensedate")
	public String addExpenseDate(Model model, HttpServletRequest request) {
		
		return "addexpensedate";
	}
	
	@PostMapping("/addexpensedate")
	public String addExpenseDate(@ModelAttribute Date date, Model model, HttpServletRequest request,
			 RedirectAttributes redirectAttributes) {
		
		String loggedInUsername = request.getUserPrincipal().getName();
		
		Expense expense = new Expense();
		
		expense.setPurchaseDate(new Timestamp(date.getTime()));
		expense.setUserId(userService.findByUsername(loggedInUsername).get().getId());
		
		model.addAttribute("newExpense", expense);
		
		return "addexpense";
	}
	
	@PostMapping("/addexpense")
	public String addExpense(Model model, @ModelAttribute Expense newExpense, HttpServletRequest request) {
		
		service.save(newExpense);
		
		return "redirect:/budget";
	}

}
