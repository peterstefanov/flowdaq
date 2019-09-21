package com.flowdaq.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flowdaq.app.model.ResetPasswordRequest;

public interface ResetPasswordRequestRepository extends JpaRepository<ResetPasswordRequest, Long> {

	public void deleteByRequestCode(String requestCode);
	
	public ResetPasswordRequest findOneByRequestCode(String requestCode);
}
