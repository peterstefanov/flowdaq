package com.flowdaq.app.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowdaq.app.model.User;
import com.flowdaq.app.model.response.CustomerItem;
import com.flowdaq.app.model.response.CustomerResponse;
import com.flowdaq.app.service.customer.CustomerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class FacilityController {

	private CustomerService customerService;
	
	public FacilityController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@GetMapping(value = "/facilities/{customerId}")
	public CustomerResponse getFacilities(@PathVariable Long customerId) {

		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<CustomerItem> result = null;
		CustomerResponse response = new CustomerResponse();
		try {
			result = customerService.findAllFacilities(customerId);
		} catch (Exception e) {
			log.error("Retrieving facilities error: ", e.getCause());
			response.setItems(Collections.EMPTY_LIST);
			return response;
		}
			      
		response.setItems(result);
		
		return response;
	}
}

