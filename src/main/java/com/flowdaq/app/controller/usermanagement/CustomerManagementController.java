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

import com.flowdaq.app.model.Address;
import com.flowdaq.app.model.Customer;
import com.flowdaq.app.model.Role;
import com.flowdaq.app.model.User;
import com.flowdaq.app.model.request.CustomerRequest;
import com.flowdaq.app.model.response.Response;
import com.flowdaq.app.model.response.Response.ResponseStatusEnum;
import com.flowdaq.app.service.address.AddressService;
import com.flowdaq.app.service.customer.CustomerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RestController
public class CustomerManagementController extends UserManagementBaseController {

	@Autowired
	private AddressService addressService;

	@Autowired
	private CustomerService customerService;

	@PostMapping(value = "/customer", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response createCustomer(@Valid @RequestBody CustomerRequest customerRequest, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Response resp = new Response();

		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!principal.getRole().toString().equalsIgnoreCase(Role.DISTRIBUTOR.toString())) {
			String messsage = "User is not authorized to perform this action";
			log.error(messsage);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage(messsage);
			return resp;
		}

		Optional<User> existingUser = userService.findByUsername(customerRequest.getUserName());

		if (existingUser.isPresent()) {
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage("User with this username already exist. Try another one, please.");

			log.info("User with this username already exist. Try another one, please.");

			response.setStatus(HttpServletResponse.SC_CONFLICT);

			return resp;
		} else {
			try {
				Address address = new Address();
				address.setAddressLine1(customerRequest.getAddressLine1());
				address.setAddressLine2(customerRequest.getAddressLine2());
				address.setAddressLine3(customerRequest.getAddressLine3());
				address.setCity(customerRequest.getCity());
				address.setPostalCode(customerRequest.getPostalCode());
				address.setState(customerRequest.getState());
				address.setCountry(customerRequest.getCountry());
				addressService.saveAddress(address);

				User user = new User();
				user.setDistributorId(customerRequest.getDistributorId());
				user.setUsername(customerRequest.getUserName());
				user.setFirstName(customerRequest.getFirstName());
				user.setLastName(StringUtils.trim(customerRequest.getLastName()));
				user.setEmailAddress(customerRequest.getEmail());
				user.setEnabled(true);
				user.setPhoneNumber(customerRequest.getPhoneNumber());
				user.setPassword(RandomStringUtils.randomAlphabetic(10));
				user.setRole(Role.customer);				
				userService.save(user);
				
				Customer customer = new Customer();
				customer.setDistributorId(customerRequest.getDistributorId());
				customer.setAddressId(address.getId());
				customer.setUsername(user.getUsername());
				customer.setCompanyName(customerRequest.getCompanyName());
				customer.setContactName(customerRequest.getContact());
				customer.setAlternativeContactName(customerRequest.getAltContact());
				customer.setDeliveryAddress(address);
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
				resp.setMessage("Customer cannot be created.");

				log.error("Customer cannot be created. ", e);

				response.setStatus(HttpServletResponse.SC_CONFLICT);
				return resp;
			}

			resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
			resp.setMessage("Customer created and reset password link sent to " + customerRequest.getEmail());
		}

		return resp;
	}

	@PutMapping(value = "/customer", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response editCustomer(@Valid @RequestBody CustomerRequest customerRequest, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Response resp = new Response();

		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!principal.getRole().toString().equalsIgnoreCase(Role.DISTRIBUTOR.toString())) {
			String messsage = "User is not authorized to perform this action";
			log.error(messsage);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage(messsage);
			return resp;
		}

		Optional<User> existingUser = userService.findByUsername(customerRequest.getUserName());

		if (!existingUser.isPresent()) {
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage("User with this username does not exist. Try another one, please.");

			log.info("User with this username does not exist. Try another one, please.");

			response.setStatus(HttpServletResponse.SC_CONFLICT);

			return resp;
		} else {
			try {

				Address address = new Address();
				address.setId(customerRequest.getAddressId());
				address.setAddressLine1(customerRequest.getAddressLine1());
				address.setAddressLine2(customerRequest.getAddressLine2());
				address.setAddressLine3(customerRequest.getAddressLine3());
				address.setCity(customerRequest.getCity());
				address.setPostalCode(customerRequest.getPostalCode());
				address.setState(customerRequest.getState());
				address.setCountry(customerRequest.getCountry());
				addressService.saveAddress(address);

				User user = new User();
				user.setDistributorId(customerRequest.getDistributorId());
				user.setUsername(customerRequest.getUserName());
				user.setFirstName(customerRequest.getFirstName());
				user.setLastName(customerRequest.getLastName());
				user.setEnabled(customerRequest.isEnabled());
				user.setPhoneNumber(customerRequest.getPhoneNumber());
				user.setEmailAddress(StringUtils.trim(customerRequest.getEmail()));
				user.setPassword(existingUser.get().getPassword());
				user.setRole(Role.customer);
				userService.update(user);
				
				Customer customer = new Customer();
				customer.setId(customerRequest.getId());
				customer.setDistributorId(customerRequest.getDistributorId());
				customer.setAddressId(address.getId());
				customer.setUsername(user.getUsername());
				customer.setCompanyName(customerRequest.getCompanyName());
				customer.setContactName(customerRequest.getContact());
				customer.setAlternativeContactName(customerRequest.getAltContact());
				customer.setDeliveryAddress(address);
				customerService.save(customer);

			} catch (DataIntegrityViolationException dive) {
				resp.setOperationStatus(ResponseStatusEnum.ERROR);
				resp.setMessage("User with this email already exists.");

				log.error("User with this email already exists. ", dive);

				response.setStatus(HttpServletResponse.SC_CONFLICT);
				return resp;
			} catch (Exception e) {
				resp.setOperationStatus(ResponseStatusEnum.ERROR);
				resp.setMessage("Customer cannot be edited.");

				log.error("Customer cannot be edited. ", e);

				response.setStatus(HttpServletResponse.SC_CONFLICT);
				return resp;
			}

			resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
			resp.setMessage("Customer was edited successfully");
		}

		return resp;
	}
	
	@DeleteMapping(value = "/customer/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response deleteCustomer(HttpServletResponse response, @PathVariable Long id) throws Exception {
		Response resp = new Response();

		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!principal.getRole().toString().equalsIgnoreCase(Role.DISTRIBUTOR.toString())) {
			String messsage = "User is not authorized to perform this action";
			log.error(messsage);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage(messsage);
			return resp;
		}

		Optional<Customer> existingCustomer = customerService.findById(id);

		if (!existingCustomer.isPresent()) {
			String messsage = "No customer with this id: " + id;
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
				/**TODO Delete all facilities for this customer*/
				/**TODO Delete coolers/devices associated with each facility?*/
				userService.deleteUser(existingUser.get());

			} catch (Exception e) {
				resp.setOperationStatus(ResponseStatusEnum.ERROR);
				resp.setMessage("Customer cannot be deleted.");

				log.error("Customer cannot be deleted. ", e);

				response.setStatus(HttpServletResponse.SC_CONFLICT);
				return resp;
			}

			resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
			resp.setMessage("Customer was deleted successfully");
		}

		return resp;
	}
}
