package com.flowdaq.app.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

	@RequestMapping(value = "/customers", method = RequestMethod.GET)
	public CustomerResponse getCustomersByPage() {

		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<CustomerItem> result = customerService.findAllByRole(principal);
	      
		CustomerResponse response = new CustomerResponse();
		response.setItems(result);
		
		return response;
	}
}
