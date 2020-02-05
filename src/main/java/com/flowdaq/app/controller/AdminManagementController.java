package com.flowdaq.app.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flowdaq.app.model.Role;
import com.flowdaq.app.model.User;
import com.flowdaq.app.model.request.Admin;
import com.flowdaq.app.model.response.Response;
import com.flowdaq.app.model.response.Response.ResponseStatusEnum;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("deprecation")
@Slf4j
@RestController
public class AdminManagementController extends UserManagementController{

	@PostMapping(value = "/admin", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response createAdmin(@Valid @RequestBody Admin admin, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Response resp = new Response();
		
		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!principal.getRole().toString().equalsIgnoreCase(Role.ADMIN.toString())) {
			String messsage = "User is not authorized to perform this action";
			log.error(messsage);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage(messsage);
			return resp;
		} 
		
		Optional<User> foundUser = userService.findByUsername(admin.getUserName());
		
		if (foundUser.isPresent()) {
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage("User with this username already exist. Try another one, please.");

			log.info("User with this username already exist. Try another one, please.");

			response.setStatus(HttpServletResponse.SC_CONFLICT);
			
			return resp;
		} else {
			try {
				User user = new User();
				user.setUsername(admin.getUserName());
				user.setDistributorId(9L);
				user.setFirstName(admin.getFirstName());
				user.setLastName(admin.getLastName());
				user.setEmailAddress(admin.getEmail());
				user.setPassword(RandomStringUtils.randomAlphabetic(10));
				user.setRole(Role.admin);
				
				userService.save(user);
				requestService.createResetPasswordRequest(user);
				
			} catch(DataIntegrityViolationException dive) {
				resp.setOperationStatus(ResponseStatusEnum.ERROR);
				resp.setMessage("User with this email already exists.");

				log.error("User with this email already exists. ", dive);

				response.setStatus(HttpServletResponse.SC_CONFLICT);
				return resp;
			} catch (Exception e) {
				resp.setOperationStatus(ResponseStatusEnum.ERROR);
				resp.setMessage("User cannot be created.");

				log.error("User cannot be created. ", e);

				response.setStatus(HttpServletResponse.SC_CONFLICT);
				return resp;
			}

			resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
			resp.setMessage("Admin created and reset password link sent to " + admin.getEmail());
		}
		
		return resp;
	}
}
