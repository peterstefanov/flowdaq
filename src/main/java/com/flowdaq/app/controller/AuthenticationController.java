package com.flowdaq.app.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.flowdaq.app.model.Distributor;
import com.flowdaq.app.model.Role;
import com.flowdaq.app.model.User;
import com.flowdaq.app.model.request.Login;
import com.flowdaq.app.model.response.Response;
import com.flowdaq.app.model.response.Response.ResponseStatusEnum;
import com.flowdaq.app.model.response.ResponseItem;
import com.flowdaq.app.security.jwt.TokenService;
import com.flowdaq.app.service.distributor.DistributorService;
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
	private DistributorService distributorService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping(value = "/auth", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Response login(@RequestBody Login login, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Response resp = new Response();
		try {

			// System.err.println("Hashed password: " + passwordEncoder.encode(login.getPassword()));
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(login.getUsername(),
					login.getPassword());
			Authentication authentication = this.authenticationManager.authenticate(authToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			User user = (User) userService.loadUserByUsername(login.getUsername());

			// Generate GWT
			String token = tokenService.expiring(ImmutableMap.of("username", login.getUsername()));

			ResponseItem respItem = new ResponseItem();

			respItem.setFirstName(user.getFirstName());
			respItem.setLastName(user.getLastName());
			respItem.setUserId(user.getUsername());
			respItem.setEmail(user.getEmailAddress());
			respItem.setToken(token);
			respItem.setRole(user.getRole().toString());

			if (Role.ADMIN.toString().equalsIgnoreCase(user.getRole().toString())) {
				respItem.setDistributorName("");
			} else {
				Distributor distributor = distributorService.findById(user.getDistributorId());
				respItem.setDistributorName(distributor.getDistributorName());
			}
			resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
			resp.setMessage("Login Success");
			resp.setItem(respItem);

		} catch (BadCredentialsException exception) {

			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage("Username and/or password invalid");

			log.error(exception.getMessage());

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return resp;
	}
}
