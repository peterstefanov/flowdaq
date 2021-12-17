package com.flowdaq.app.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowdaq.app.model.Role;
import com.flowdaq.app.model.User;
import com.flowdaq.app.model.request.Login;
import com.flowdaq.app.model.response.Response;
import com.flowdaq.app.model.response.Response.ResponseStatusEnum;
import com.flowdaq.app.model.response.UserItem;
import com.flowdaq.app.security.jwt.TokenService;
import com.flowdaq.app.service.customer.CustomerService;
import com.flowdaq.app.service.user.UserService;
import com.google.common.collect.ImmutableMap;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/public")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CustomerService customerService;
	
	@PostMapping(value = "/auth", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response login(@RequestBody Login login, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Response resp = new Response();
		try {

			log.info("Hashed password: " + passwordEncoder.encode(login.getPassword()));
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
			Authentication authentication = this.authenticationManager.authenticate(authToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			User user = (User) userService.loadUserByUsername(login.getUsername());

			// Generate GWT
			String token = tokenService.expiring(ImmutableMap.of("username", login.getUsername()));

			UserItem respItem = new UserItem();

			respItem.setFirstName(user.getFirstName());
			respItem.setLastName(user.getLastName());
			respItem.setUserId(user.getUsername());
			respItem.setEmail(user.getEmailAddress());
			respItem.setToken(token);
			respItem.setRole(user.getRole().toString());
		    respItem.setDistributorName(user.getDistributor().getDistributorName());
		    respItem.setDistributorId(user.getDistributorId());		    
		    respItem.setCustomerCompany(StringUtils.EMPTY);
		    respItem.setCustomerId(9999L);
		    
		    if (user.getRole().toString().equalsIgnoreCase(Role.CUSTOMER.toString()) || user.getRole().toString().equalsIgnoreCase(Role.FACILITY.toString())) {
		    	respItem.setCustomerCompany(customerService.getCustomerCompany(user.getUsername()));
		    	respItem.setCustomerId(customerService.getCustomerId(user.getUsername()));
		    }
		    
			resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
			resp.setMessage("Login Success");
			resp.setItem(respItem);

		} catch (BadCredentialsException exception) {

			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage("Username and/or password invalid");

			log.error(exception.getMessage());

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (InternalAuthenticationServiceException exception) {

			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage(exception.getMessage());

			log.error(exception.getMessage());

			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
		
		return resp;
	}
}