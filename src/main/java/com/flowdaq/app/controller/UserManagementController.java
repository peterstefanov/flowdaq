package com.flowdaq.app.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.flowdaq.app.model.Role;
import com.flowdaq.app.model.User;
import com.flowdaq.app.model.request.Admin;
import com.flowdaq.app.model.response.Response;
import com.flowdaq.app.model.response.Response.ResponseStatusEnum;
import com.flowdaq.app.service.resetpassword.ResetPasswordRequestService;
import com.flowdaq.app.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
@RequestMapping(value = "/api/usermanagement", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserManagementController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ResetPasswordRequestService requestService;
	
	@PostMapping(value = "/admin", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response createAdmin(@Valid @RequestBody Admin admin, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Response resp = new Response();
		
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
				user.setPassword("dummy");
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
			resp.setMessage("Reset Password link sent to " + admin.getEmail());
		}
		
		return resp;
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	Response handleConstraintViolationException(MethodArgumentNotValidException e) {
		
		BeanPropertyBindingResult result = (BeanPropertyBindingResult) e.getBindingResult();
		List<ObjectError> errors = result.getAllErrors();

		Response resp = new Response();
		resp.setOperationStatus(ResponseStatusEnum.ERROR);
		resp.setMessage(errors.get(0).getDefaultMessage());
		
		return resp;
	}
}
