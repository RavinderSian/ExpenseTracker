package com.personal.budget.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
		
		KeyHolder holder = new GeneratedKeyHolder();
		
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement("INSERT INTO expenses (category, amount, user_id, description, purchase_date) VALUES (?, ?, ?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, expense.getCategory().toString());
				ps.setBigDecimal(2, expense.getAmount());
				return ps;
			}
		}, holder);
		
		Number newId = (Integer) holder.getKeys().get("id");
		
		expense.setId(newId.longValue());
		return expense;
		
	}

	@Override
	public void deleteById(Long id) {
		jdbcTemplate.update("DELETE FROM expenses WHERE id=?", id);
	}

}
