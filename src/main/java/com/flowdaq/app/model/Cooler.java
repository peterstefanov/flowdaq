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
@Table(name = "coolers")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties
@Data
@EqualsAndHashCode(callSuper = false)
public class Cooler implements Serializable {

	private static final long serialVersionUID = 6819372849695592520L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "cooler_identifier")
	private String coolerIdentifier;

	@Column(name = "device_id")
	private Long deviceId;

	@Column(name = "reorder_threshold")
	private Long reorderThreshold;

	@Column(name = "max_bottle_count")
	private Long maxBottleCount;

	@Column(name = "current_bottle_count")
	private Long currentBottleCount;

	@Column(name = "customer_id")
	private Long customerId;

	@Column(name = "delivery_location_id")
	private Long deliveryLocationId;

	@Column(name = "longitude")
	private Double longitude;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "last_delivery_date")
	private Date lastDeliveryDate;

	@Column(name = "prev_bottle_count")
	private Long prevBottleCount;

	@Column(name = "current_full")
	private Long currentFull;

	@Column(name = "current_empty")
	private Long currentEmpty;

	@Column(name = "device_name")
	private String deviceName;
}
