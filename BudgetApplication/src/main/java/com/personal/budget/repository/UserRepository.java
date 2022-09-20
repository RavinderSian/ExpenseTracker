package com.personal.budget.repository;

import com.personal.budget.model.User;

public interface UserRepository <T, ID>  {

	User save(User user);
	void deleteById(Long id);
	ID findIdByUsername(String username);
}
 