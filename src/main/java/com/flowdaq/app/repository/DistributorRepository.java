package com.flowdaq.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flowdaq.app.model.Distributor;

public interface DistributorRepository extends JpaRepository<Distributor, Long> {

}
