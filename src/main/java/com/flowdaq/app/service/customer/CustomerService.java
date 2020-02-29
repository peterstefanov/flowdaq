package com.flowdaq.app.service.customer;

import java.util.List;

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
}
