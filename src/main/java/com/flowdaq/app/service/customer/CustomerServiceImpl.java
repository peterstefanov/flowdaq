package com.flowdaq.app.service.customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	public List<CustomerItem> findAllByDistributorId(Long distributorId) {
		return processResult(customerRepository.findAllByDistributorId(distributorId));
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
	
	private List<CustomerItem> processResult(List<Customer> list) {
		
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
			result.add(resultItem);
		}
		
		return result;
	}
}
