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

import com.personal.budget.mappers.UserRowMapper;
import com.personal.budget.model.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {

	private final JdbcTemplate jdbcTemplate;
	
	public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public User save(User user) {
		KeyHolder holder = new GeneratedKeyHolder();
		
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
				
				if (user.getUsername() == null) ps.setNull(1, Types.VARCHAR);
				else ps.setString(1, user.getUsername());
				
				if (user.getPassword() == null) ps.setNull(2, Types.VARCHAR);
				else ps.setString(2, user.getPassword());
				
				if (user.getEmail() == null) ps.setNull(3, Types.VARCHAR);
				else ps.setString(3, user.getEmail());
				
				return ps;
			}
		}, holder);
		
		Number newId = (Long) holder.getKeys().get("id");
		
		user.setId(newId.longValue());
		return user;
	}

	@Override
	public void deleteById(Long id) {
		jdbcTemplate.update("DELETE FROM users WHERE id=?", id);
	}

	@Override
	public Optional<User> findByUsername(String username) {
		try {
			return Optional.of(jdbcTemplate.queryForObject("SELECT * FROM users WHERE username=?", 
					new UserRowMapper(), username));
		}catch (EmptyResultDataAccessException e) {
			log.info("Username not found in database");
			return Optional.empty();
		}
	}
	
	@Override
	public Optional<User> findById(Long id) {
		try {
			log.info("In the method");
		return Optional.of(jdbcTemplate.queryForObject("SELECT * FROM users WHERE id=?", 
				new UserRowMapper(), id));
		}
		catch (EmptyResultDataAccessException e) {
			log.info("Id not found in database");
			return Optional.empty();
		}
	}

}
