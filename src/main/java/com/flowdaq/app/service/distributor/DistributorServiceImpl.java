package com.flowdaq.app.service.distributor;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.flowdaq.app.model.Distributor;
import com.flowdaq.app.repository.DistributorRepository;

@Service
public class DistributorServiceImpl implements DistributorService{

    private DistributorRepository repository;
    
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
}
