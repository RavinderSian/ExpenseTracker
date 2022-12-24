package com.personal.budget.service;

import java.util.List;
import java.util.Optional;

import com.personal.budget.model.Expense;

public interface ExpenseService {

	Expense save(Expense expense);
	void deleteById(Long id);
	Optional<Expense> findById(Long id);
	List<Expense> findAll();
	List<Expense> findByUserId(Long userId);
	List<Expense> findExpensesByYear(Integer year);

}
