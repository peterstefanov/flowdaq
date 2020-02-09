package com.flowdaq.app.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class DistributorRequest {

    @Size(min = 4, max = 45, message = "Username must be between 4 and 45 characters")
	private String userName;
	
	@Email(message = "Email should be valid")
	@NotBlank(message = "Email cannot be blank")
	private String email;
	
    @Size(min = 4, max = 45, message = "First anme must be between 4 and 45 characters")
	private String firstName;
	
    @Size(min = 4, max = 45, message = "Last Name must be between 4 and 45 characters")
	private String lastName;
    
    @NotBlank(message = "Company name cannot be blank")
    private String companyName;
    
    @NotBlank(message = "Address cannot be blank")
    private String addressLine1;
    
    private String addressLine2;
    
    private String addressLine3;
    
    @NotBlank(message = "City cannot be blank")
    private String city;
    
    private String state;
    
    @NotBlank(message = "Country cannot be blank")
    private String country;
    
    private String postalCode;

}
