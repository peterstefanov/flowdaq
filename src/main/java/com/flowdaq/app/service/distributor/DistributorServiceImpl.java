package com.flowdaq.app.service.distributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flowdaq.app.model.Address;
import com.flowdaq.app.model.Distributor;
import com.flowdaq.app.model.Role;
import com.flowdaq.app.model.User;
import com.flowdaq.app.model.response.AddressItem;
import com.flowdaq.app.model.response.UserItem;
import com.flowdaq.app.repository.DistributorRepository;
import com.flowdaq.app.repository.UserRepository;

@Service
public class DistributorServiceImpl implements DistributorService{

    @Autowired
    private DistributorRepository repository;   
    
    @Autowired
    private UserRepository userRepository;
    
	public DistributorServiceImpl(DistributorRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public Optional<Distributor> findById(Long distributorId) {		
		return repository.findById(distributorId);
	}
	
	@Override
	public Distributor save(Distributor distributor) {		
		return repository.save(distributor);
	}
	
	@Override
	public void deleteDistributorById(Long distributorId) {	
		repository.deleteById(distributorId);
	}
	
	@Override
	public List<UserItem> findAllDistributors() {		
		return processResult(userRepository.findAllByRole(Role.distributor));
	} 
	
	private List<UserItem> processResult(List<User> list) {
		
		List<UserItem> result = new ArrayList<>();
		for (User item : list) {
			UserItem resultItem = new UserItem();
			
			resultItem.setFirstName(item.getFirstName());
			resultItem.setLastName(item.getLastName());
			resultItem.setUserId(item.getUsername());
			resultItem.setEmail(item.getEmailAddress());
			resultItem.setEnabled(item.isEnabled());
			resultItem.setPhoneNumber(item.getPhoneNumber());
			resultItem.setDistributorName(item.getDistributor().getDistributorName());
			resultItem.setDistributorId(item.getDistributorId());

			Address address = item.getDistributor().getDeliveryAddress();
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
				resultItem.setAddress(addressItem);
			}
			result.add(resultItem);
		}		
		return result;
	}
}
