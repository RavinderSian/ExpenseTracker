package com.personal.budget.repository;

import com.personal.budget.model.Expense;

public interface ExpenseRepository <T,ID> {

	Expense save(T expense);
	void deleteById(ID id);

}
