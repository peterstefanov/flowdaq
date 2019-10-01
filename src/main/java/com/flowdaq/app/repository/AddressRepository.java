package com.flowdaq.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flowdaq.app.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
