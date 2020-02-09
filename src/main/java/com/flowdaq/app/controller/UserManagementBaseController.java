package com.flowdaq.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.flowdaq.app.model.response.Response;
import com.flowdaq.app.model.response.Response.ResponseStatusEnum;
import com.flowdaq.app.service.resetpassword.ResetPasswordRequestService;
import com.flowdaq.app.service.user.UserService;


@RestController
@Validated
@RequestMapping(value = "/api/usermanagement", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserManagementBaseController {

	@Autowired
	protected UserService userService;
	
	@Autowired
	protected ResetPasswordRequestService requestService;	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	protected Response handleConstraintViolationException(MethodArgumentNotValidException e) {
		
		BeanPropertyBindingResult result = (BeanPropertyBindingResult) e.getBindingResult();
		List<ObjectError> errors = result.getAllErrors();

		Response resp = new Response();
		resp.setOperationStatus(ResponseStatusEnum.ERROR);
		resp.setMessage(errors.get(0).getDefaultMessage());
		
		return resp;
	}
}
