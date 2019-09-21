package com.flowdaq.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.flowdaq.app.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	public Page<Customer> findAll(Pageable p);

	public List<Customer> findAll();

	public List<Customer> findAllByDistributorId(Long distributorId);
}
