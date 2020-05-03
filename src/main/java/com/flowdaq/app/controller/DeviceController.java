package com.flowdaq.app.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowdaq.app.model.response.DeviceItem;
import com.flowdaq.app.model.response.DeviceResponse;
import com.flowdaq.app.service.device.DeviceService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class DeviceController {

	@Autowired
	private DeviceService deviceService;
	
	public DeviceController(DeviceService deviceService) {
		this.deviceService = deviceService;
	}

	@GetMapping(value = "/devices/{customerId}")
	public DeviceResponse getDevices(@PathVariable Long customerId) {

		
		DeviceResponse response = new DeviceResponse();
		List<DeviceItem> result = null;
		try {
			result = deviceService.findAllByCustomerId(customerId);
		} catch (Exception e) {
			log.error("Retrieving coolers error: ", e);
			response.setItems(Collections.EMPTY_LIST);
			return response;
		}

		response.setItems(result);
		
		return response;
	}
}
