package com.flowdaq.app.service.user;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.flowdaq.app.model.User;

public interface UserService extends UserDetailsService {

	public User save(User user);
	
	public User update(User user);

	public User findByUsernameAndPassword(String id, String password);

	public Optional<User> findByUsername(String username);
	
	public Optional<User> findByEmailAddress(String email);
	
	public Optional<User> findByDistributorId(Long distributorId);
	
	public boolean isValidPassword(User user, String password);
	
	public boolean isPasswordTheSame(User user, String password);

	public void deleteUser(User user);
}
