package com.flowdaq.app.service.distributor;

import java.util.List;
import java.util.Optional;

import com.flowdaq.app.model.Distributor;
import com.flowdaq.app.model.response.UserItem;

public interface DistributorService {

	public Optional<Distributor> findById(Long distributorId);

	public Distributor save(Distributor distributor);

	public void deleteDistributorById(Long distributorId);

	public List<UserItem> findAllDistributors();
}
