package com.flowdaq.app.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@JsonIgnoreProperties
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class AddressItem {

	private Long id;

	private String addressLine1;

	private String addressLine2;

	private String addressLine3;

	private String city;

	private String state;

	private String country;
	
	private String postalCode;	
}
