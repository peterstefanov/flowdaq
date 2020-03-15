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
import com.flowdaq.app.model.Distributor;
import com.flowdaq.app.model.Role;
import com.flowdaq.app.model.User;
import com.flowdaq.app.model.request.DistributorRequest;
import com.flowdaq.app.model.response.Response;
import com.flowdaq.app.model.response.Response.ResponseStatusEnum;
import com.flowdaq.app.service.address.AddressService;
import com.flowdaq.app.service.customer.CustomerService;
import com.flowdaq.app.service.distributor.DistributorService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RestController
public class DistributorManagementController extends UserManagementBaseController {

	@Autowired
	private DistributorService distributorService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private CustomerService customerService;

	@PostMapping(value = "/distributor", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response createDistributor(@Valid @RequestBody DistributorRequest distributorRequest,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Response resp = new Response();

		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!principal.getRole().toString().equalsIgnoreCase(Role.ADMIN.toString())) {
			String messsage = "User is not authorized to perform this action";
			log.error(messsage);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage(messsage);
			return resp;
		}

		Optional<User> existingUser = userService.findByUsername(distributorRequest.getUserName());

		if (existingUser.isPresent()) {
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage("User with this username already exist. Try another one, please.");

			log.info("User with this username already exist. Try another one, please.");

			response.setStatus(HttpServletResponse.SC_CONFLICT);

			return resp;
		} else {
			try {
				Address address = new Address();
				address.setAddressLine1(distributorRequest.getAddressLine1());
				address.setAddressLine2(distributorRequest.getAddressLine2());
				address.setAddressLine3(distributorRequest.getAddressLine3());
				address.setCity(distributorRequest.getCity());
				address.setPostalCode(distributorRequest.getPostalCode());
				address.setState(distributorRequest.getState());
				address.setCountry(distributorRequest.getCountry());
				addressService.saveAddress(address);

				Distributor distributor = new Distributor();
				distributor.setAddressId(address.getId());
				distributor.setDistributorName(distributorRequest.getCompanyName());
				distributor.setDeliveryAddress(address);
				distributorService.save(distributor);

				Long distributorId = distributor.getId();
				address.setDistributorId(distributorId);

				User user = new User();
				user.setUsername(distributorRequest.getUserName());
				user.setDistributorId(distributorId);
				user.setFirstName(distributorRequest.getFirstName());
				user.setLastName(StringUtils.trim(distributorRequest.getLastName()));
				user.setEmailAddress(distributorRequest.getEmail());
				user.setEnabled(true);
				user.setPhoneNumber(distributorRequest.getPhoneNumber());
				user.setPassword(RandomStringUtils.randomAlphabetic(10));
				user.setRole(Role.distributor);

				userService.save(user);
				requestService.createResetPasswordRequest(user);

			} catch (DataIntegrityViolationException dive) {
				resp.setOperationStatus(ResponseStatusEnum.ERROR);
				resp.setMessage("User with this email already exists.");

				log.error("User with this email already exists. ", dive);

				response.setStatus(HttpServletResponse.SC_CONFLICT);
				return resp;
			} catch (Exception e) {
				resp.setOperationStatus(ResponseStatusEnum.ERROR);
				resp.setMessage("Distributor cannot be created.");

				log.error("Distributor cannot be created. ", e);

				response.setStatus(HttpServletResponse.SC_CONFLICT);
				return resp;
			}

			resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
			resp.setMessage("Distributor created and reset password link sent to " + distributorRequest.getEmail());
		}

		return resp;
	}

	@PutMapping(value = "/distributor", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response editDistributor(@Valid @RequestBody DistributorRequest distributorRequest,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Response resp = new Response();

		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!principal.getRole().toString().equalsIgnoreCase(Role.ADMIN.toString())) {
			String messsage = "User is not authorized to perform this action";
			log.error(messsage);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage(messsage);
			return resp;
		}

		Optional<User> existingUser = userService.findByUsername(distributorRequest.getUserName());

		if (!existingUser.isPresent()) {
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage("User with this username does not exist. Try another one, please.");

			log.info("User with this username does not exist. Try another one, please.");

			response.setStatus(HttpServletResponse.SC_CONFLICT);

			return resp;
		} else {
			try {

				Address address = new Address();
				address.setId(distributorRequest.getAddressId());
				address.setAddressLine1(distributorRequest.getAddressLine1());
				address.setAddressLine2(distributorRequest.getAddressLine2());
				address.setAddressLine3(distributorRequest.getAddressLine3());
				address.setCity(distributorRequest.getCity());
				address.setPostalCode(distributorRequest.getPostalCode());
				address.setState(distributorRequest.getState());
				address.setCountry(distributorRequest.getCountry());
				addressService.saveAddress(address);

				Distributor distributor = new Distributor();
				distributor.setId(distributorRequest.getId());
				distributor.setAddressId(address.getId());
				distributor.setDistributorName(distributorRequest.getCompanyName());
				distributor.setDeliveryAddress(address);
				distributorService.save(distributor);

				User user = new User();
				user.setUsername(distributorRequest.getUserName());
				user.setDistributorId(distributorRequest.getId());
				user.setFirstName(distributorRequest.getFirstName());
				user.setLastName(distributorRequest.getLastName());
				user.setEnabled(distributorRequest.isEnabled());
				user.setPhoneNumber(distributorRequest.getPhoneNumber());
				user.setEmailAddress(StringUtils.trim(distributorRequest.getEmail()));
				user.setPassword(existingUser.get().getPassword());
				user.setRole(Role.distributor);
				userService.update(user);

			} catch (DataIntegrityViolationException dive) {
				resp.setOperationStatus(ResponseStatusEnum.ERROR);
				resp.setMessage("User with this email already exists.");

				log.error("User with this email already exists. ", dive);

				response.setStatus(HttpServletResponse.SC_CONFLICT);
				return resp;
			} catch (Exception e) {
				resp.setOperationStatus(ResponseStatusEnum.ERROR);
				resp.setMessage("Distributor cannot be edited.");

				log.error("Distributor cannot be edited. ", e);

				response.setStatus(HttpServletResponse.SC_CONFLICT);
				return resp;
			}

			resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
			resp.setMessage("Distributor was edited successfully");
		}

		return resp;
	}

	@DeleteMapping(value = "/distributor/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response deleteDistributor(HttpServletResponse response, @PathVariable Long id) throws Exception {
		Response resp = new Response();

		User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!principal.getRole().toString().equalsIgnoreCase(Role.ADMIN.toString())) {
			String messsage = "User is not authorized to perform this action";
			log.error(messsage);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage(messsage);
			return resp;
		}

		Optional<Distributor> existingDistributor = distributorService.findById(id);

		if (!existingDistributor.isPresent()) {
			String messsage = "No distributor with this id";
			log.info(messsage);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage(messsage);
			return resp;
		}

		Optional<User> existingUser = userService.findByDistributorId(id);

		if (!existingUser.isPresent()) {
			resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setMessage("No distributor with this id");

			log.info("No distributor with this id");

			response.setStatus(HttpServletResponse.SC_NOT_FOUND);

			return resp;
		} else {
			try {
				addressService.deleteAddress(existingDistributor.get().getAddressId());
				distributorService.deleteDistributorById(id);
				customerService.deleteAllByDistributorId(id);
				/**TODO Delete coolers/devices associated with each customer?*/
				userService.deleteUser(existingUser.get());

			} catch (Exception e) {
				resp.setOperationStatus(ResponseStatusEnum.ERROR);
				resp.setMessage("Distributor cannot be deleted.");

				log.error("Distributor cannot be deleted. ", e);

				response.setStatus(HttpServletResponse.SC_CONFLICT);
				return resp;
			}

			// response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
			resp.setMessage("Distributor was deleted successfully");
		}

		return resp;
	}
}
