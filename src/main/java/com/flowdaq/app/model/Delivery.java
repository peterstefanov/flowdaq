package com.flowdaq.app.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "delivery")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties
@Data
@EqualsAndHashCode(callSuper = false)
public class Delivery implements Serializable {

	private static final long serialVersionUID = -4575372671041005904L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "status")
	private String status;

	@Column(name = "from_ditributor_id")
	private Long fromDitributorId;
	
	@Column(name = "from_customer_id")
	private Long fromCustomerId;

	@Column(name = "from_facility_id")
	private Long fromFacilityId;

	@Column(name = "to_customer_id")
	private Long toCustomerId;
	
	@Column(name = "to_facility_id")
	private Long toFacilityId;

	@Column(name = "to_cooler_id")
	private Long toCoolerId;
	
	@Column(name = "driver_id")
	private Long driverId;

	@Column(name = "vehicle_id")
	private Long vehicleId;

	@Column(name = "delivery_date")
	private Date deliveryDate;

	@Column(name = "actual_delivery_date")
	private Date actualDeliveryDate;

	@Column(name = "full_bottles")
	private Long fullBottles;

	@Column(name = "actual_fulls_delivered")
	private Long actualFullsDelivered;

	@Column(name = "route_id")
	private Long routeId;

	@Column(name = "empties_retrieved")
	private Long emptiesRetrieved;

	@Column(name = "actual_empties_retrieved")
	private Long actualEmptiesRetrieved;

	@Column(name = "delivery_notes")
	private String deliveryNotes;
}