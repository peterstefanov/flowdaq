package com.flowdaq.app.service.device;

import java.util.List;

import com.flowdaq.app.model.response.DeviceItem;

public interface DeviceService {

	List<DeviceItem> findAllByCustomerId(Long customerId);

}
