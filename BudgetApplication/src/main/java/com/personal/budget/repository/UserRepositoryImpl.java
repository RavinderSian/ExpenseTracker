package com.personal.budget.repository;

import org.springframework.jdbc.core.JdbcTemplate;

import com.personal.budget.model.User;

public class UserRepositoryImpl implements UserRepository <User, Long> {

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
	public Long findIdByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

}
