package com.flowdaq.app.model.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DistributorRequest extends UserRequest {

	private Long id;

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

}
