package com.personal.budget.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.personal.budget.model.Expense;
import com.personal.budget.repository.ExpenseRepository;

@Service
public class ExpenseServiceImpl implements ExpenseService {
	
	private final ExpenseRepository repository;
	
	public ExpenseServiceImpl(ExpenseRepository repository) {
		this.repository = repository;
	}

	@Override
	public Expense save(Expense expense) {
		return repository.save(expense);
	}

	@Override
	public void deleteById(Long id) {
		repository.deleteById(id);
	}

	@Override
	public Optional<Expense> findById(Long id) {
		return repository.findById(id);
	}

	@Override
	public List<Expense> findAll() {
		return repository.findAll();
	}
	
}
