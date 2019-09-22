package com.flowdaq.app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.flowdaq.app.model.Cooler;
import com.flowdaq.app.model.Customer;
import com.flowdaq.app.model.User;
import com.flowdaq.app.model.response.CustomerItem;
import com.flowdaq.app.model.response.CustomerResponse;
import com.flowdaq.app.service.customer.CustomerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomersController {

	private CustomerService customerService;
	
	public CustomersController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@RequestMapping(value = "/customers/{distributorId}", method = RequestMethod.GET)
	public CustomerResponse getCustomersByPage(@PathVariable Long distributorId) {

		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<CustomerItem> result = processResult(customerService.findAllByDistributorId(distributorId));
	      
		CustomerResponse response = new CustomerResponse();
		response.setItems(result);
		
		return response;
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
		return result;
	}
}
