package com.personal.budget.repository;

import org.springframework.jdbc.core.JdbcTemplate;

import com.personal.budget.mappers.UserRowMapper;
import com.personal.budget.model.User;

public class UserRepositoryImpl implements UserRepository {

	private final JdbcTemplate jdbcTemplate;
	
	public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public User save(User user) {
		return null;
	}

	@Override
	public void deleteById(Long id) {
		jdbcTemplate.update("DELETE FROM expenses WHERE id=?", id);
	}

	@Override
	public User findUserByUsername(String username) {
		return jdbcTemplate.queryForObject("SELECT * FROM employees WHERE username=?", 
				new UserRowMapper(), username);
	}

}
