package com.personal.budget.controller;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.personal.budget.model.Expense;
import com.personal.budget.model.ExpenseDTO;
import com.personal.budget.model.User;
import com.personal.budget.service.ExpenseService;
import com.personal.budget.service.UserService;

@Controller
public class AnalysisController {
	
	private final ExpenseService service;
	private final UserService userService;
	
	public AnalysisController(ExpenseService service, UserService userService) {
		this.service = service;
		this.userService = userService;
	}

	@GetMapping("/analysis")
	public String getGraph(Model model, HttpServletRequest request) {
		
		if (request.getUserPrincipal() == null) {
			model.addAttribute("expenses", new ArrayList<>());
		} else {
			Long userId = userService.findByUsername(request.getUserPrincipal().getName()).get().getId();
			model.addAttribute("expenses", service.findExpensesByYearForUser(LocalDate.now().getYear(), userId));
		}
		
		model.addAttribute("currentYear", LocalDate.now().getYear());
		model.addAttribute("previousYear", LocalDate.now().getYear() - 1);
		model.addAttribute("nextYear", LocalDate.now().getYear() + 1);
		
		model.addAttribute("currentMonth", LocalDate.now().getMonth().toString());
		
		return "line-chart";
	}
}
