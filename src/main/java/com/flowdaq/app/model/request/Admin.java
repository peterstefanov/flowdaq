package com.flowdaq.app.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class Admin {

    @Size(min = 4, max = 45, message = "Username must be between 4 and 45 characters")
	private String userName;
	
	@Email(message = "Email should be valid")
	@NotBlank(message = "Email cannot be blank")
	private String email;
	
    @Size(min = 4, max = 45, message = "First anme must be between 4 and 45 characters")
	private String firstName;
	
    @Size(min = 4, max = 45, message = "Last Name must be between 4 and 45 characters")
	private String lastName;
}
