package com.flowdaq.app.service.delivery;

import java.util.List;

import com.flowdaq.app.model.response.DeliveryItem;

public interface DeliveryService {
	
	List<DeliveryItem> findAllByCustomerId(Long customerId);
	
	List<DeliveryItem> findAllByFacilityId(Long facilityId);
}
