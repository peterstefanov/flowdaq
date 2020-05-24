package com.flowdaq.app.service.delivery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flowdaq.app.model.Delivery;
import com.flowdaq.app.model.response.DeliveryItem;
import com.flowdaq.app.repository.DeliveryRepository;

@Service
public class DeliveryServiceImpl implements DeliveryService {

	@Autowired
	private DeliveryRepository deliveryRepository;

	@Override
	public List<DeliveryItem> findAllByDistributorId(Long distributorId) {
		return processDeliveries(deliveryRepository.findAllByFromDistributorId(distributorId));
	}
	
	@Override
	public List<DeliveryItem> findAllByCustomerId(Long customerId) {
		return processDeliveries(deliveryRepository.findAllByFromCustomerId(customerId));
	}

	@Override
	public List<DeliveryItem> findAllByFacilityId(Long facilityId) {
		return processDeliveries(deliveryRepository.findAllByFromFacilityId(facilityId));
	}

	@Override
	public void deleteDeliveryById(Long id) {
		deliveryRepository.deleteById(id);		
	}
	
	@Override
	public Optional<Delivery> findById(Long id) {
		return deliveryRepository.findById(id);
	}
	
	@Override
	public Delivery save(Delivery delivery) {
		return deliveryRepository.save(delivery);
		
	}
	
	private List<DeliveryItem> processDeliveries(List<Delivery> list) {

		List<DeliveryItem> result = new ArrayList<>();

		for (Delivery item : list) {
			DeliveryItem deviceItem = DeliveryItem.builder().id(item.getId()).status(item.getStatus().toString())
					.fromDistributorId(item.getFromDistributorId()).fromCustomerId(item.getFromCustomerId())
					.fromFacilityId(item.getFromFacilityId()).toCustomerId(item.getToCustomerId())
					.toFacilityId(item.getToFacilityId()).toCoolerId(item.getToCoolerId()).driverId(item.getDriverId())
					.vehicleId(item.getVehicleId()).deliveryDate(item.getDeliveryDate())
					.actualDeliveryDate(item.getActualDeliveryDate()).fullBottles(item.getFullBottles())
					.actualFullsDelivered(item.getActualFullsDelivered()).routeId(item.getRouteId())
					.emptiesRetrieved(item.getEmptiesRetrieved())
					.actualEmptiesRetrieved(item.getActualEmptiesRetrieved()).deliveryNotes(item.getDeliveryNotes())
					.build();
			result.add(deviceItem);
		}
		return result;
	}
}
