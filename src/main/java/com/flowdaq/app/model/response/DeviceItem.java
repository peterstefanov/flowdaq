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
public class DeviceItem {

	private Long id;

	private String coolerIdentifier;

	private Long deviceId;

	private Long reorderThreshold;

	private Long maxBottleCount;

	private Long currentBottleCount;

	private Long customerId;

	private Long deliveryLocationId;

	private Double longitude;

	private Double latitude;

	private Date lastDeliveryDate;

	private Long prevBottleCount;

	private Long currentFull;

	private Long currentEmpty;

	private String deviceName;
}
