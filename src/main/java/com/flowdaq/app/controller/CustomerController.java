package com.flowdaq.app.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowdaq.app.model.User;
import com.flowdaq.app.model.response.CustomerItem;
import com.flowdaq.app.model.response.CustomerResponse;
import com.flowdaq.app.model.response.DeliveryItem;
import com.flowdaq.app.model.response.DeliveryResponse;
import com.flowdaq.app.service.customer.CustomerService;
import com.flowdaq.app.service.delivery.DeliveryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {

	private CustomerService customerService;
	private DeliveryService deliveryService;
	
	private final String DATE_FORMAT = "MM-dd-yyyy";
	
	@Autowired
	public CustomerController(CustomerService customerService, DeliveryService deliveryService) {
		this.customerService = customerService;
		this.deliveryService = deliveryService;
	}

	@GetMapping(value = "/customers/{distributorId}")
	public CustomerResponse getCustomers(@PathVariable Long distributorId) {

		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<CustomerItem> result = null;
		try {
			result = customerService.findAllByDistributorId(distributorId);
		} catch (Exception e) {
			log.error("Retrieving customers error: ", e);
		}
			      
		CustomerResponse response = new CustomerResponse();
		response.setItems(result);
		
		return response;
	}
	
	@GetMapping(value = "/customers/delivery/{customerId}")
	public DeliveryResponse getDeliveries(@PathVariable Long customerId) {

		
		DeliveryResponse response = new DeliveryResponse();
		List<DeliveryItem> result = null;
		try {
			result = deliveryService.findAllByCustomerId(customerId);
		} catch (Exception e) {
			log.error("Retrieving deliveries error: ", e);
			response.setItems(Collections.EMPTY_LIST);
			return response;
		}

		response.setItems(result);
		
		return response;
	}
}
