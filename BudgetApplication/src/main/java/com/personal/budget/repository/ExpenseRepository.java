package com.personal.budget.repository;

import com.personal.budget.model.Expense;

public interface ExpenseRepository {

	Expense saveById(Long id);
	void deleteById(Long id);

}
