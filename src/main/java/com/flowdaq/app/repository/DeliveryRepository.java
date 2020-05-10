package com.flowdaq.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flowdaq.app.model.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

	public List<Delivery> findAllByFromDistributorId(Long distributorId);
	
	public List<Delivery> findAllByFromCustomerId(Long customerId);
	
	public List<Delivery> findAllByFromFacilityId(Long facilityId);	
}
