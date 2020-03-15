package com.flowdaq.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.flowdaq.app.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	public Page<Customer> findAll(Pageable p);

	public List<Customer> findAll();

	public List<Customer> findAllByDistributorId(Long distributorId);
	
	public Optional<Customer> findByUsername(String username);
	
	public List<Customer> findAllByRelatedTo(Long customerId);
}
