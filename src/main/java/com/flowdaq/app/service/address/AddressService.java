package com.flowdaq.app.service.address;

import java.util.List;

import com.flowdaq.app.model.Address;

public interface AddressService {

	public List<Address> getAddresses();
	
	public Address saveAddress(Address address);

	public void deleteAddress(Long addressId);
}
