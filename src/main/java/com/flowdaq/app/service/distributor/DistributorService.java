package com.flowdaq.app.service.distributor;

import java.util.Optional;

import com.flowdaq.app.model.Distributor;

public interface DistributorService {

	public Optional<Distributor> findById(Long distributorId);

	public Distributor save(Distributor distributor);

	public void deleteDistributorById(Long distributorId);
}
