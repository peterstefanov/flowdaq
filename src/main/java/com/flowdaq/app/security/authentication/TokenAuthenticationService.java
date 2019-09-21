package com.flowdaq.app.security.authentication;

import java.util.Optional;

import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import com.flowdaq.app.model.User;
import com.flowdaq.app.security.jwt.JwtTokenService;
import com.flowdaq.app.service.user.UserService;

@Service
public class TokenAuthenticationService implements UserAuthenticationService {

	private UserService userService;
	private JwtTokenService tokens;

	public TokenAuthenticationService(UserService userService, JwtTokenService tokens) {
		this.userService = userService;
		this.tokens = tokens;
	}
	
	@Override
	public Optional<User> findByToken(String token) {
		Optional<User> user = null;
		try {
			user = Optional.of(tokens.verify(token)).map(map -> map.get("username")).flatMap(userService::findByUsername);
		} catch(Exception e) {
			throw e;
		}
				
		if(user.isPresent()) {
			MDC.put("user", user.get().getUsername());
			user.get().setToken(token);
		}
		return user;
	}
}
