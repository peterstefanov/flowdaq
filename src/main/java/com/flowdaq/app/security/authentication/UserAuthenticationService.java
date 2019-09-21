package com.flowdaq.app.security.authentication;

import java.util.Optional;

import com.flowdaq.app.model.User;

public interface UserAuthenticationService {

	  /**
	   * Finds and validate a user by its JWT token.
	   *
	   * @param token 
	   * @return
	 * @throws Exception 
	   */
	  Optional<User> findByToken(String token);
}
