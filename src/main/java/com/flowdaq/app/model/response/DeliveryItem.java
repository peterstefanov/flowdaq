package com.flowdaq.app.model.response;

import java.util.Date;

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
public class DeliveryItem {

	private Long id;

	private String status;

	private Long customerId;

	private Long facilityId;

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
