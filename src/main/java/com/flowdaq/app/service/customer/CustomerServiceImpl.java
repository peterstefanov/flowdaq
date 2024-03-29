package com.flowdaq.app.service.customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.flowdaq.app.model.Address;
import com.flowdaq.app.model.Cooler;
import com.flowdaq.app.model.Customer;
import com.flowdaq.app.model.User;
import com.flowdaq.app.model.response.AddressItem;
import com.flowdaq.app.model.response.CustomerItem;
import com.flowdaq.app.model.response.UserItem;
import com.flowdaq.app.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;
    
	public CustomerServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
	
	@Override
	public Customer save(Customer customer) {		
		return customerRepository.save(customer);
	}

	@Override
	public List<Customer> findAll() {
		return customerRepository.findAll();
	}

	@Override
	public Optional<Customer> findById(Long customerId) {		
		return customerRepository.findById(customerId);
	}
	
	@Override
	public List<CustomerItem> findAllByDistributorId(Long distributorId) {
		return processResult(customerRepository.findAllByDistributorId(distributorId), false);
	}

	@Override
	public List<CustomerItem> findAllFacilities(Long customerId) {
		return processResult(customerRepository.findAllByRelatedTo(customerId), true);
	}
	
	@Override
	public Page<Customer> findAll(Pageable pagebale) {
		return customerRepository.findAll(pagebale);
	}


	@Override
	public void deleteAllByDistributorId(Long distributorId) {
		List<Customer> customersEntity = customerRepository.findAllByDistributorId(distributorId);
		customerRepository.deleteInBatch(customersEntity);		
	}
	
	@Override
	public String getCustomerCompany(String username) {
		Optional<Customer> cust = customerRepository.findByUsername(username);
		return cust.isPresent() ? cust.get().getCompanyName() : StringUtils.EMPTY;
	}
	
	@Override
	public Long getCustomerId(String username) {
		Optional<Customer> cust = customerRepository.findByUsername(username);
		return cust.isPresent() ? cust.get().getId() : 9999L;
	}
	
	@Override
	public void deleteCustomerById(Long id) {
		customerRepository.deleteById(id);
	}
	
	private List<CustomerItem> processResult(List<Customer> list, boolean isFacility) {
		
		List<CustomerItem> result = new ArrayList<>();
		
		for (Customer item : list) {
			
			List<Cooler> coolers = item.getCoolers();
			CustomerItem resultItem = new CustomerItem();
			resultItem.setCustomerId(item.getId());
			resultItem.setCompanyName(item.getCompanyName());
			resultItem.setFull((int) coolers.stream().mapToLong(i -> i.getCurrentFull()).sum());
			resultItem.setEmpty((int) coolers.stream().mapToLong(i -> i.getCurrentEmpty()).sum());
			resultItem.setMax((int) coolers.stream().mapToLong(i -> i.getMaxBottleCount()).sum());
			resultItem.setCapacity(resultItem.getMax() - resultItem.getFull());
			resultItem.setDeliveryDate(new Date(System.currentTimeMillis()));
			resultItem.setCount(coolers.size());
			resultItem.setContact(item.getContactName());
			resultItem.setAltContact(item.getAlternativeContactName());
			resultItem.setRelatedTo(item.getRelatedTo());
			
			UserItem userItem = new UserItem();
			User user = item.getUser();
			userItem.setFirstName(user.getFirstName());
			userItem.setLastName(user.getLastName());
			userItem.setUserId(user.getUsername());
			userItem.setEmail(user.getEmailAddress());
			userItem.setEnabled(user.isEnabled());
			userItem.setPhoneNumber(user.getPhoneNumber());
			userItem.setDistributorName(user.getDistributor().getDistributorName());
			userItem.setDistributorId(user.getDistributorId());
			
			Address address = item.getDeliveryAddress();
			if (address != null) {
				AddressItem addressItem = AddressItem.builder()
						.id(address.getId())
						.addressLine1(address.getAddressLine1())
						.addressLine2(address.getAddressLine2())
						.addressLine3(address.getAddressLine3())
						.city(address.getCity())
						.state(address.getState())
						.country(address.getCountry())
						.postalCode(address.getPostalCode())
						.build();
				userItem.setAddress(addressItem);
			}
			
			resultItem.setUserItem(userItem );
			if (!isFacility && item.getRelatedTo() == null) {
				result.add(resultItem);
			} else if (isFacility && item.getRelatedTo() != null) {
				result.add(resultItem);
			}
			
		}
		
		return result;
	}
}