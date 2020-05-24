package com.flowdaq.app.service.delivery;

import java.util.List;
import java.util.Optional;

import com.flowdaq.app.model.Delivery;
import com.flowdaq.app.model.response.DeliveryItem;

public interface DeliveryService {
	
	public List<DeliveryItem> findAllByDistributorId(Long distributorId);
	
	public List<DeliveryItem> findAllByCustomerId(Long customerId);
	
	public List<DeliveryItem> findAllByFacilityId(Long facilityId);

	public void deleteDeliveryById(Long id);

	public Optional<Delivery> findById(Long id);

	public Delivery save(Delivery delivery);

}
