package com.flowdaq.app.service.customer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.flowdaq.app.model.Customer;
import com.flowdaq.app.model.response.CustomerItem;

public interface CustomerService {

	public Customer save(Customer customer);

	public Page<Customer> findAll(Pageable pagable);
	
	public List<Customer> findAll();
	
	public List<CustomerItem> findAllByDistributorId(Long distributorId);
	
	public void deleteAllByDistributorId(Long distributirId);

	public String getCustomerCompany(String username);

	public Optional<Customer> findById(Long customerId);

	public void deleteCustomerById(Long id);

	public List<CustomerItem> findAllFacilities(Long customerId);

	public Long getCustomerId(String username);
}
