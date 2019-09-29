package com.flowdaq.app.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ForgotPassword {	
	
	@Email(message = "Email should be valid")
	@NotBlank(message = "Email cannot be blank")
	private String email;
}
