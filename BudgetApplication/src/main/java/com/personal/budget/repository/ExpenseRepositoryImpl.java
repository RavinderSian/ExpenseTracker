package com.personal.budget.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.personal.budget.mappers.ExpenseRowMapper;
import com.personal.budget.model.Expense;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ExpenseRepositoryImpl implements ExpenseRepository {

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
				
				if (expense.getCategory() == null) ps.setNull(1, Types.VARCHAR);
				else ps.setString(1, expense.getCategory().toString());
				
				ps.setBigDecimal(2, expense.getAmount());
				ps.setLong(3, expense.getUserId());
				ps.setString(4, expense.getDescription());
				ps.setTimestamp(5, expense.getPurchaseDate());
				
				return ps;
			}
		}, holder);
		
		Number newId = (Long) holder.getKeys().get("id");
		
		expense.setId(newId.longValue());
		return expense;
		
	}

	@Override
	public void deleteById(Long id) {
		jdbcTemplate.update("DELETE FROM expenses WHERE id=?", id);
	}
	
	@Override
	public Optional<Expense> findById(Long id) {
		try {
		return Optional.of(jdbcTemplate.queryForObject("SELECT * FROM expenses WHERE id=?", 
				new ExpenseRowMapper(), id));
		}
		catch (EmptyResultDataAccessException e) {
			log.info("Id not found in database");
			return Optional.empty();
		}
	}

}
