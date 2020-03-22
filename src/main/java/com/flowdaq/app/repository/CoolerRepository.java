package com.flowdaq.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flowdaq.app.model.Cooler;

public interface CoolerRepository extends JpaRepository<Cooler, Long> {

	public List<Cooler> findAllByCustomerId(Long customerId);
}
