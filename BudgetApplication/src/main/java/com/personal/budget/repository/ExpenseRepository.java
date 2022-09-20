package com.personal.budget.repository;

import com.personal.budget.model.Expense;

public interface ExpenseRepository {

	Expense save(Expense expense);
	void deleteById(Long id);

}
