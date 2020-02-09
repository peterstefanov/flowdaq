package com.flowdaq.app.service.distributor;

import com.flowdaq.app.model.Distributor;

public interface DistributorService {

	public Distributor findById(Long distributorId);

	public Distributor save(Distributor distributor);
}
