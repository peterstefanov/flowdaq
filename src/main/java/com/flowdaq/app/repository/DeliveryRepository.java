package com.flowdaq.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flowdaq.app.model.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

	
	List<Delivery> findAllByCustomerId(Long customerId);
	
	List<Delivery> findAllByFacilityId(Long facilityId);
}
