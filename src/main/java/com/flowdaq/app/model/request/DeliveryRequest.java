package com.flowdaq.app.model.request;

import java.util.Date;

import lombok.Data;

@Data
public class DeliveryRequest {

	private Long id;

	private String status;

	private Long fromDistributorId;

	private Long fromCustomerId;

	private Long fromFacilityId;

	private Long toCustomerId;

	private Long toFacilityId;

	private Long toCoolerId;
	
	private Long driverId;

	private Long vehicleId;

	private Date deliveryDate;

	private Date actualDeliveryDate;

	private Long fullBottles;

	private Long actualFullsDelivered;

	private Long routeId;

	private Long emptiesRetrieved;

	private Long actualEmptiesRetrieved;

	private String deliveryNotes;
}
