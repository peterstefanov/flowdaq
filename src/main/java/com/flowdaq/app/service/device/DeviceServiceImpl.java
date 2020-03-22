package com.flowdaq.app.service.device;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flowdaq.app.model.Cooler;
import com.flowdaq.app.model.response.DeviceItem;
import com.flowdaq.app.repository.CoolerRepository;

@Service
public class DeviceServiceImpl implements DeviceService{

    @Autowired
	private CoolerRepository coolerRepository;
	
	@Override
	public List<DeviceItem> findAllByCustomerId(Long customerId) {
		return processCoolers(coolerRepository.findAllByCustomerId(customerId));
	}

	private List<DeviceItem> processCoolers(List<Cooler> list) {
		
		List<DeviceItem> result = new ArrayList<>();
		
		for (Cooler item : list) {
			DeviceItem deviceItem = DeviceItem.builder()
					.id(item.getId())
					.coolerIdentifier(item.getCoolerIdentifier())
					.deviceId(item.getDeviceId())
					.reorderThreshold(item.getReorderThreshold())
					.maxBottleCount(item.getMaxBottleCount())
					.currentBottleCount(item.getCurrentBottleCount())
					.customerId(item.getCustomerId())
					.deliveryLocationId(item.getDeliveryLocationId())
					.longitude(item.getLongitude())
					.latitude(item.getLatitude())
					.lastDeliveryDate(item.getLastDeliveryDate())
					.prevBottleCount(item.getPrevBottleCount())
					.currentFull(item.getCurrentFull())
					.currentEmpty(item.getCurrentEmpty())
					.deviceName(item.getDeviceName())
					.build();
			result.add(deviceItem);
		}
		return result;
	}
}
