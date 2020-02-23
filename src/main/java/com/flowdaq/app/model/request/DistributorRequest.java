package com.flowdaq.app.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class DistributorRequest {

    private Long id;
    
    @Size(min = 4, max = 45, message = "Username must be between 4 and 45 characters")
	private String userName;
	
	@Email(message = "Email should be valid")
	@NotBlank(message = "Email cannot be blank")
	private String email;
	
    @Size(max = 45, message = "First name must be atmost 45 characters")
	private String firstName;
	
    @Size(max = 45, message = "Last Name must be atmost 45 characters")
	private String lastName;
    
	private boolean enabled;
    
    @NotBlank(message = "Company name cannot be blank")
    private String companyName;
    
    private Long addressId;
    
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
    
    @Size(max = 45, message = "Phone number must be atmost 45 characters")
    private String phoneNumber;

}
