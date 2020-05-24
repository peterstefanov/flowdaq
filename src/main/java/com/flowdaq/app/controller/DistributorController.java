package com.flowdaq.app.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowdaq.app.model.Role;
import com.flowdaq.app.model.User;
import com.flowdaq.app.model.response.DeliveryItem;
import com.flowdaq.app.model.response.DeliveryResponse;
import com.flowdaq.app.model.response.DistributorResponse;
import com.flowdaq.app.model.response.UserItem;
import com.flowdaq.app.service.delivery.DeliveryService;
import com.flowdaq.app.service.distributor.DistributorService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class DistributorController {

	private DistributorService distributorService;
	private DeliveryService deliveryService;
	
	@Autowired
	public DistributorController(DistributorService distributorService, DeliveryService deliveryService) {
		this.distributorService = distributorService;
		this.deliveryService = deliveryService;
	}
	
	@GetMapping(value = "/distributors")
	public DistributorResponse getDistributorsList(HttpServletResponse response) {

		DistributorResponse resp = new DistributorResponse();
		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!principal.getRole().toString().equalsIgnoreCase(Role.ADMIN.toString())) {
			String messsage = "User is not authorized to view this resource";
			log.error(messsage);
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		} else {
			List<UserItem> result = distributorService.findAllDistributors();
			resp.setItems(result);
		}

		return resp;
	}
	
	@GetMapping(value = "/distributors/delivery/{distributorId}")
	public DeliveryResponse getDeliveriesForDistributor(@PathVariable Long distributorId) {

		
		DeliveryResponse response = new DeliveryResponse();
		List<DeliveryItem> result = null;
		try {
			result = deliveryService.findAllByDistributorId(distributorId);
		} catch (Exception e) {
			log.error("Retrieving deliveries error: ", e);
			response.setItems(Collections.EMPTY_LIST);
			return response;
		}

		response.setItems(result);
		
		return response;
	}
}
