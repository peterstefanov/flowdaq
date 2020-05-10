package com.flowdaq.app.service.delivery;

import java.util.List;

import com.flowdaq.app.model.response.DeliveryItem;

public interface DeliveryService {
	
	public List<DeliveryItem> findAllByDistributorId(Long distributorId);
	
	public List<DeliveryItem> findAllByCustomerId(Long customerId);
	
	public List<DeliveryItem> findAllByFacilityId(Long facilityId);

}
