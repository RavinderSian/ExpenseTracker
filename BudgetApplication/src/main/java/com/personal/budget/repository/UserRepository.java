package com.personal.budget.repository;

import com.personal.budget.model.User;

public interface UserRepository {

	User save(User user);
	void deleteById(Long id);
	Long findIdByUsername(String username);
}
 