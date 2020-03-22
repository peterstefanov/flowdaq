package com.flowdaq.app.controller.usermanagement;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flowdaq.app.ApplicationException;
import com.flowdaq.app.model.Address;
import com.flowdaq.app.model.Customer;
import com.flowdaq.app.model.Role;
import com.flowdaq.app.model.User;
import com.flowdaq.app.model.request.FacilityRequest;
import com.flowdaq.app.model.response.Response;
import com.flowdaq.app.model.response.Response.ResponseStatusEnum;
import com.flowdaq.app.service.address.AddressService;
import com.flowdaq.app.service.customer.CustomerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RestController
public class FacilityManagementController extends UserManagementBaseController {

	@Autowired
	private AddressService addressService;

	@Autowired
	private CustomerService customerService;

	@PostMapping(value = "/facility", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response createFacility(@Valid @RequestBody FacilityRequest facilityRequest, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Response resp = new Response();

		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!principal.getRole().toString().equalsIgnoreCase(Role.CUSTOMER.toString())) {
			String messsage = "User is not authorized to perform this action";
			log.error(messsage);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage(messsage);
			return resp;
		}

		Optional<User> existingUser = userService.findByUsername(facilityRequest.getUserName());

		if (existingUser.isPresent()) {
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage("User with this username already exist. Try another one, please.");

			log.info("User with this username already exist. Try another one, please.");

			response.setStatus(HttpServletResponse.SC_CONFLICT);

			return resp;
		} else {
			try {
				Address address = new Address();
				address.setAddressLine1(facilityRequest.getAddressLine1());
				address.setAddressLine2(facilityRequest.getAddressLine2());
				address.setAddressLine3(facilityRequest.getAddressLine3());
				address.setCity(facilityRequest.getCity());
				address.setPostalCode(facilityRequest.getPostalCode());
				address.setState(facilityRequest.getState());
				address.setCountry(facilityRequest.getCountry());
				addressService.saveAddress(address);

				User user = new User();
				user.setDistributorId(facilityRequest.getDistributorId());
				user.setUsername(facilityRequest.getUserName());
				user.setFirstName(facilityRequest.getFirstName());
				user.setLastName(StringUtils.trim(facilityRequest.getLastName()));
				user.setEmailAddress(facilityRequest.getEmail());
				user.setEnabled(true);
				user.setPhoneNumber(facilityRequest.getPhoneNumber());
				user.setPassword(RandomStringUtils.randomAlphabetic(10));
				user.setRole(Role.facility);				
				userService.save(user);
				
				Customer customer = new Customer();
				customer.setDistributorId(facilityRequest.getDistributorId());
				customer.setAddressId(address.getId());
				customer.setUsername(user.getUsername());
				customer.setCompanyName(facilityRequest.getCompanyName());
				customer.setContactName(facilityRequest.getContact());
				customer.setAlternativeContactName(facilityRequest.getAltContact());
				customer.setDeliveryAddress(address);
				
				Long relatedTo = facilityRequest.getRelatedTo();
				if(relatedTo == null) {
					throw new ApplicationException("relatedTo should not be blank");
				}
				customer.setRelatedTo(relatedTo);
				customerService.save(customer);
				
				requestService.createResetPasswordRequest(user);

			} catch (DataIntegrityViolationException dive) {
				resp.setOperationStatus(ResponseStatusEnum.ERROR);
				resp.setMessage("User with this email already exists.");

				log.error("User with this email already exists. ", dive);

				response.setStatus(HttpServletResponse.SC_CONFLICT);
				return resp;
			} catch (Exception e) {
				resp.setOperationStatus(ResponseStatusEnum.ERROR);
				resp.setMessage("Facility cannot be created.");

				log.error("Facility cannot be created. ", e);

				response.setStatus(HttpServletResponse.SC_CONFLICT);
				return resp;
			}

			resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
			resp.setMessage("Facility created and reset password link sent to " + facilityRequest.getEmail());
		}

		return resp;
	}

	@PutMapping(value = "/facility", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response editFacility(@Valid @RequestBody FacilityRequest facilityRequest, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Response resp = new Response();

		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!principal.getRole().toString().equalsIgnoreCase(Role.CUSTOMER.toString())) {
			String messsage = "User is not authorized to perform this action";
			log.error(messsage);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage(messsage);
			return resp;
		}

		Optional<User> existingUser = userService.findByUsername(facilityRequest.getUserName());

		if (!existingUser.isPresent()) {
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage("User with this username does not exist. Try another one, please.");

			log.info("User with this username does not exist. Try another one, please.");

			response.setStatus(HttpServletResponse.SC_CONFLICT);

			return resp;
		} else {
			try {

				Address address = new Address();
				address.setId(facilityRequest.getAddressId());
				address.setAddressLine1(facilityRequest.getAddressLine1());
				address.setAddressLine2(facilityRequest.getAddressLine2());
				address.setAddressLine3(facilityRequest.getAddressLine3());
				address.setCity(facilityRequest.getCity());
				address.setPostalCode(facilityRequest.getPostalCode());
				address.setState(facilityRequest.getState());
				address.setCountry(facilityRequest.getCountry());
				addressService.saveAddress(address);

				User user = new User();
				user.setDistributorId(facilityRequest.getDistributorId());
				user.setUsername(facilityRequest.getUserName());
				user.setFirstName(facilityRequest.getFirstName());
				user.setLastName(facilityRequest.getLastName());
				user.setEnabled(facilityRequest.isEnabled());
				user.setPhoneNumber(facilityRequest.getPhoneNumber());
				user.setEmailAddress(StringUtils.trim(facilityRequest.getEmail()));
				user.setPassword(existingUser.get().getPassword());
				user.setRole(Role.facility);
				userService.update(user);
				
				Customer customer = new Customer();
				customer.setId(facilityRequest.getId());
				customer.setDistributorId(facilityRequest.getDistributorId());
				customer.setAddressId(address.getId());
				customer.setUsername(user.getUsername());
				customer.setCompanyName(facilityRequest.getCompanyName());
				customer.setContactName(facilityRequest.getContact());
				customer.setAlternativeContactName(facilityRequest.getAltContact());
				customer.setDeliveryAddress(address);
				
				Long relatedTo = facilityRequest.getRelatedTo();
				if(relatedTo == null) {
					throw new ApplicationException("relatedTo should not be blank");
				}
				customer.setRelatedTo(relatedTo);
				customerService.save(customer);

			} catch (DataIntegrityViolationException dive) {
				resp.setOperationStatus(ResponseStatusEnum.ERROR);
				resp.setMessage("User with this email already exists.");

				log.error("User with this email already exists. ", dive);

				response.setStatus(HttpServletResponse.SC_CONFLICT);
				return resp;
			} catch (Exception e) {
				resp.setOperationStatus(ResponseStatusEnum.ERROR);
				resp.setMessage("Facility cannot be edited.");

				log.error("Facility cannot be edited. ", e);

				response.setStatus(HttpServletResponse.SC_CONFLICT);
				return resp;
			}

			resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
			resp.setMessage("Facility was edited successfully");
		}

		return resp;
	}
	
	@DeleteMapping(value = "/facility/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response deleteFacility(HttpServletResponse response, @PathVariable Long id) throws Exception {
		Response resp = new Response();

		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!principal.getRole().toString().equalsIgnoreCase(Role.CUSTOMER.toString())) {
			String messsage = "User is not authorized to perform this action";
			log.error(messsage);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage(messsage);
			return resp;
		}

		Optional<Customer> existingCustomer = customerService.findById(id);

		if (!existingCustomer.isPresent()) {
			String messsage = "No facility with this id: " + id;
			log.info(messsage);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage(messsage);
			return resp;
		}

		Optional<User> existingUser = userService.findByUsername(existingCustomer.get().getUsername());

		if (!existingUser.isPresent()) {
			String messsage = "No user with this username: " + existingCustomer.get().getUsername();
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage(messsage);

			log.info(messsage);

			response.setStatus(HttpServletResponse.SC_NOT_FOUND);

			return resp;
		} else {
			try {
				addressService.deleteAddress(existingCustomer.get().getAddressId());
				customerService.deleteCustomerById(id);
				/**TODO Delete coolers/devices associated with this facility*/
				userService.deleteUser(existingUser.get());

			} catch (Exception e) {
				resp.setOperationStatus(ResponseStatusEnum.ERROR);
				resp.setMessage("Facility cannot be deleted.");

				log.error("fFcility cannot be deleted. ", e);

				response.setStatus(HttpServletResponse.SC_CONFLICT);
				return resp;
			}

			resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
			resp.setMessage("Facility was deleted successfully");
		}

		return resp;
	}
}
