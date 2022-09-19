package com.personal.budget.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.personal.budget.model.Expense;

@Repository
public class ExpenseRepositoryImpl implements ExpenseRepository<Expense, Long> {

	private final JdbcTemplate jdbcTemplate;
	
	public ExpenseRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Expense save(Expense expense) {
		return null;
	}

	@Override
	public void deleteById(Long id) {
		jdbcTemplate.update("DELETE FROM expenses WHERE id=?", id);
	}

}
