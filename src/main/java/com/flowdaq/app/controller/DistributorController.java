package com.flowdaq.app.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowdaq.app.model.Role;
import com.flowdaq.app.model.User;
import com.flowdaq.app.model.response.DistributorResponse;
import com.flowdaq.app.model.response.UserItem;
import com.flowdaq.app.service.distributor.DistributorService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class DistributorController {

	@Autowired
	private DistributorService distributorService;

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
}
