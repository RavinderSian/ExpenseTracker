package com.personal.budget.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.personal.budget.model.User;
import com.personal.budget.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository repository;
	
	public UserServiceImpl(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public User save(User user) {
		return repository.save(user); 
	}

	@Override
	public void deleteById(Long id) {
		repository.deleteById(id);
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return repository.findByUsername(username);
	}

	@Override
	public Optional<User> findById(Long id) {
		return repository.findById(id);
	}

}
