package com.personal.budget.controller;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.personal.budget.model.Expense;
import com.personal.budget.model.User;
import com.personal.budget.service.ExpenseService;
import com.personal.budget.service.UserService;

@Controller
public class ExpenseController {
	
	private final ExpenseService service;
	private final UserService userService;
	
	public ExpenseController(ExpenseService service, UserService userService) {
		this.service = service;
		this.userService = userService;
	}
	
	@GetMapping("/budget")
	public String budget(Model model, HttpServletRequest request) {
		
		if (request.getUserPrincipal() == null) {
			model.addAttribute("expenses", new ArrayList<>());
		}else {
			Long userId = userService.findByUsername(request.getUserPrincipal().getName()).get().getId();
			model.addAttribute("expenses", service.findExpensesByYear(LocalDate.now().getYear()));
		}
		
		model.addAttribute("currentYear", LocalDate.now().getYear());
		model.addAttribute("previousYear", LocalDate.now().getYear() - 1);
		model.addAttribute("nextYear", LocalDate.now().getYear() + 1);
		model.addAttribute("expense", new Expense());

		model.addAttribute("user", new User());
		
		return "budget";
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/budget/{year}")
	public String budgetForYear(@PathVariable Integer year, Model model, HttpServletRequest request) {
		
		Long userId = userService.findByUsername(request.getUserPrincipal().getName()).get().getId();
		model.addAttribute("expenses", service.findExpensesByYear(year));
		
		model.addAttribute("expense", new Expense());
		model.addAttribute("currentYear", year);
		model.addAttribute("previousYear", year - 1);
		model.addAttribute("nextYear", year + 1);
		
		return "budget-year";
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@PostMapping("/addexpensedate")
	public String addExpenseDate(@ModelAttribute Expense expense, Model model, HttpServletRequest request,
			 RedirectAttributes redirectAttributes) {
		
		String loggedInUsername = request.getUserPrincipal().getName();
		
		Expense expense2 = new Expense();
		
		expense2.setPurchaseDate(expense.getPurchaseDate());
		expense2.setUserId(userService.findByUsername(loggedInUsername).get().getId());
		
		model.addAttribute("newExpense", expense2);
		
		return "addexpense";
	}
	
	@PostMapping("/addexpense")
	public String addExpense(Model model, @ModelAttribute Expense newExpense, HttpServletRequest request) {
		
		service.save(newExpense);
		return "redirect:/budget";
	}
	
}