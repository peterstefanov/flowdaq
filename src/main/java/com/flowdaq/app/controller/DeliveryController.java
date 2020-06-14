package com.flowdaq.app.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowdaq.app.model.Delivery;
import com.flowdaq.app.model.DeliveryStatus;
import com.flowdaq.app.model.Role;
import com.flowdaq.app.model.User;
import com.flowdaq.app.model.request.DeliveryRequest;
import com.flowdaq.app.model.response.Response;
import com.flowdaq.app.model.response.Response.ResponseStatusEnum;
import com.flowdaq.app.service.delivery.DeliveryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class DeliveryController {

	private DeliveryService deliveryService;

	@Autowired
	public DeliveryController(DeliveryService deliveryService) {
		this.deliveryService = deliveryService;
	}

	@PostMapping(value = "/delivery", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response createDelivery(@Valid @RequestBody DeliveryRequest deliveryRequest, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Response resp = new Response();

		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		/**Only distributor, customer or facility can create deliveries*/
		if (!(principal.getRole().toString().equalsIgnoreCase(Role.DISTRIBUTOR.toString())
				|| principal.getRole().toString().equalsIgnoreCase(Role.CUSTOMER.toString())
				|| principal.getRole().toString().equalsIgnoreCase(Role.FACILITY.toString()))) {
			String messsage = "User is not authorized to perform this action";
			log.error(messsage);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage(messsage);
			return resp;
		}

		Optional<Delivery> existingDelivery = deliveryService.findById(deliveryRequest.getId());

		if (existingDelivery.isPresent()) {
			String message = String.format("Delievery with this id s% does exist. Try another one, please.",
					deliveryRequest.getId());
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage(message);

			log.info(message);

			response.setStatus(HttpServletResponse.SC_CONFLICT);

			return resp;
		} else {
			try {
				Delivery delivery = new Delivery();
				delivery.setStatus(deliveryRequest.getStatus().equalsIgnoreCase(DeliveryStatus.DELIVERED.toString())
						? DeliveryStatus.DELIVERED
						: DeliveryStatus.SCHEDULED);
				delivery.setFromDistributorId(deliveryRequest.getFromDistributorId());
				delivery.setFromCustomerId(deliveryRequest.getFromCustomerId());
				delivery.setFromFacilityId(deliveryRequest.getFromFacilityId());
				delivery.setToCustomerId(deliveryRequest.getToCustomerId());
				delivery.setToFacilityId(deliveryRequest.getToFacilityId());
				delivery.setToCoolerId(deliveryRequest.getToCoolerId());
				delivery.setDriverId(deliveryRequest.getDriverId());
				delivery.setVehicleId(deliveryRequest.getVehicleId());
				delivery.setDeliveryDate(deliveryRequest.getDeliveryDate());
				delivery.setActualDeliveryDate(deliveryRequest.getActualDeliveryDate());
				delivery.setFullBottles(deliveryRequest.getFullBottles());
				delivery.setActualFullsDelivered(deliveryRequest.getActualFullsDelivered());
				delivery.setRouteId(deliveryRequest.getRouteId());
				delivery.setEmptiesRetrieved(deliveryRequest.getEmptiesRetrieved());
				delivery.setActualEmptiesRetrieved(deliveryRequest.getActualEmptiesRetrieved());
				delivery.setDeliveryNotes(deliveryRequest.getDeliveryNotes());
				deliveryService.save(delivery);

			} catch (Exception e) {
				resp.setOperationStatus(ResponseStatusEnum.ERROR);
				resp.setMessage("Delivery cannot be created.");

				log.error("Delivery cannot be created. ", e);

				response.setStatus(HttpServletResponse.SC_CONFLICT);
				return resp;
			}

			resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
			resp.setMessage("Delivery was created successfully");
		}

		return resp;
	}
	
	@PutMapping(value = "/delivery", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response editDelivery(@Valid @RequestBody DeliveryRequest deliveryRequest, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Response resp = new Response();

		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!(principal.getRole().toString().equalsIgnoreCase(Role.DISTRIBUTOR.toString())
				|| principal.getRole().toString().equalsIgnoreCase(Role.CUSTOMER.toString())
				|| principal.getRole().toString().equalsIgnoreCase(Role.FACILITY.toString()))) {
			String messsage = "User is not authorized to perform this action";
			log.error(messsage);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage(messsage);
			return resp;
		}

		Optional<Delivery> existingDelivery = deliveryService.findById(deliveryRequest.getId());

		if (!existingDelivery.isPresent()) {
			String message = String.format("Delievery with this id s% does not exist. Try another one, please.",
					deliveryRequest.getId());
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage(message);

			log.info(message);

			response.setStatus(HttpServletResponse.SC_CONFLICT);

			return resp;
		} else {
			try {
				Delivery delivery = new Delivery();
				delivery.setId(deliveryRequest.getId());
				delivery.setStatus(deliveryRequest.getStatus().equalsIgnoreCase(DeliveryStatus.DELIVERED.toString())
						? DeliveryStatus.DELIVERED
						: DeliveryStatus.SCHEDULED);
				delivery.setFromDistributorId(deliveryRequest.getFromDistributorId());
				delivery.setFromCustomerId(deliveryRequest.getFromCustomerId());
				delivery.setFromFacilityId(deliveryRequest.getFromFacilityId());
				delivery.setToCustomerId(deliveryRequest.getToCustomerId());
				delivery.setToFacilityId(deliveryRequest.getToFacilityId());
				delivery.setToCoolerId(deliveryRequest.getToCoolerId());
				delivery.setDriverId(deliveryRequest.getDriverId());
				delivery.setVehicleId(deliveryRequest.getVehicleId());
				delivery.setDeliveryDate(deliveryRequest.getDeliveryDate());
				delivery.setActualDeliveryDate(deliveryRequest.getActualDeliveryDate());
				delivery.setFullBottles(deliveryRequest.getFullBottles());
				delivery.setActualFullsDelivered(deliveryRequest.getActualFullsDelivered());
				delivery.setRouteId(deliveryRequest.getRouteId());
				delivery.setEmptiesRetrieved(deliveryRequest.getEmptiesRetrieved());
				delivery.setActualEmptiesRetrieved(deliveryRequest.getActualEmptiesRetrieved());
				delivery.setDeliveryNotes(deliveryRequest.getDeliveryNotes());
				deliveryService.save(delivery);

			} catch (Exception e) {
				resp.setOperationStatus(ResponseStatusEnum.ERROR);
				resp.setMessage("Delivery cannot be edited.");

				log.error("Delivery cannot be edited. ", e);

				response.setStatus(HttpServletResponse.SC_CONFLICT);
				return resp;
			}

			resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
			resp.setMessage("Delivery was edited successfully");
		}

		return resp;
	}

	@DeleteMapping(value = "/delivery/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response deleteDelivery(HttpServletResponse response, @PathVariable Long id) throws Exception {
		Response resp = new Response();

		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!(principal.getRole().toString().equalsIgnoreCase(Role.DISTRIBUTOR.toString())
				|| principal.getRole().toString().equalsIgnoreCase(Role.CUSTOMER.toString())
				|| principal.getRole().toString().equalsIgnoreCase(Role.FACILITY.toString()))) {
			String messsage = "User is not authorized to perform this action";
			log.error(messsage);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage(messsage);
			return resp;
		}

		try {
			deliveryService.deleteDeliveryById(id);

		} catch (Exception e) {
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage("Delivery cannot be deleted.");

			log.error("Delivery cannot be deleted. ", e);

			response.setStatus(HttpServletResponse.SC_CONFLICT);
			return resp;
		}

		resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
		resp.setMessage("Delivery was deleted successfully");

		return resp;
	}
}
