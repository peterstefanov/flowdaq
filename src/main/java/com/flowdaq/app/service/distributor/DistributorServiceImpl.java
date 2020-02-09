package com.flowdaq.app.service.distributor;

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
	public Distributor findById(Long distributorId) {		
		return repository.findById(distributorId).orElse(new Distributor());
	}
	
	@Override
	public Distributor save(Distributor distributor) {		
		return repository.save(distributor);
	}
}
