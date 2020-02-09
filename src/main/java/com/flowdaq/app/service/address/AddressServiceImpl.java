package com.flowdaq.app.service.address;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flowdaq.app.model.Address;
import com.flowdaq.app.repository.AddressRepository;

@Service
public class AddressServiceImpl implements AddressService{

	@Autowired
	private AddressRepository addressRepository;
	
	@Override
	public List<Address> getAddresses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Address saveAddress(Address address) {
		return addressRepository.save(address);
	}
}
