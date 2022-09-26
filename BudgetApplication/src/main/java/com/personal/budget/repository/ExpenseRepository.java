package com.personal.budget.repository;

import java.util.Optional;

import com.personal.budget.model.Expense;

public interface ExpenseRepository {

	Expense save(Expense expense);
	void deleteById(Long id);
	Optional<Expense> findById(Long id);

}
