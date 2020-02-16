package com.flowdaq.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flowdaq.app.model.Role;
import com.flowdaq.app.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findOneByUsername(String username);

	Optional<User> findOneByUsernameAndPassword(String username, String password);

	Optional<User> findOneByEmailAddress(String emailAddress);

	Optional<User> findOneByDistributorId(Long distributorId);

	List<User> findAllByRole(Role role);
}
