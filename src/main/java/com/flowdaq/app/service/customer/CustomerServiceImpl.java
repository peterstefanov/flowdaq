package com.flowdaq.app.service.customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.flowdaq.app.model.Cooler;
import com.flowdaq.app.model.Customer;
import com.flowdaq.app.model.Role;
import com.flowdaq.app.model.User;
import com.flowdaq.app.model.response.CustomerItem;
import com.flowdaq.app.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;
    
	public CustomerServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
	
	@Override
	public Customer save(Customer customer) {		
		return customerRepository.save(customer);
	}

	@Override
	public List<Customer> findAll() {
		return customerRepository.findAll();
	}

	@Override
	public List<Customer> findAllByDistributorId(Long distributorId) {
		return customerRepository.findAllByDistributorId(distributorId);
	}

	@Override
	public Page<Customer> findAll(Pageable pagebale) {
		return customerRepository.findAll(pagebale);
	}

	@Override
	public List<CustomerItem> findAllByRole(User principal) {

		Role role = principal.getRole();
		
		if (Role.ADMIN.toString().equalsIgnoreCase(role.toString())) {			
			return processResult(customerRepository.findAll());
		} else {
			Customer customer = new Customer();
			customer.setDistributorId(principal.getDistributorId());
			return processResult(customerRepository.findAll(org.springframework.data.domain.Example.of(customer)));
		}
	}

	private List<CustomerItem> processResult(List<Customer> list) {
		
		List<CustomerItem> result = new ArrayList<>();
		for (Customer item : list) {
			
			List<Cooler> coolers = item.getCoolers();
			CustomerItem resultItem = new CustomerItem();
			resultItem.setCustomerId(item.getId());
			resultItem.setCompanyName(item.getCompanyName());
			resultItem.setFull((int) coolers.stream().mapToLong(i -> i.getCurrentFull()).sum());
			resultItem.setEmpty((int) coolers.stream().mapToLong(i -> i.getCurrentEmpty()).sum());
			resultItem.setMax((int) coolers.stream().mapToLong(i -> i.getMaxBottleCount()).sum());
			resultItem.setCapacity(resultItem.getMax() - resultItem.getFull());
			resultItem.setDeliveryDate(new Date(System.currentTimeMillis()));
			resultItem.setCount(coolers.size());
			result.add(resultItem);
		}		
		//pagable.getContent()
		return result;
	}
}
