package com.flowdaq.app.service.user;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.flowdaq.app.model.Role;
import com.flowdaq.app.model.User;
import com.flowdaq.app.model.response.UserItem;

public interface UserService extends UserDetailsService {

	public User save(User user);

	public User findByUsernameAndPassword(String id, String password);

	public Optional<User> findByUsername(String username);
	
	public Optional<User> findByEmailAddress(String email);
	
	public boolean isValidPassword(User user, String password);
	
	public boolean isPasswordTheSame(User user, String password);
	
	public List<UserItem> findAllByUserType(Role role);
}
