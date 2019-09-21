package com.flowdaq.app.service.resetpassword;

import com.flowdaq.app.model.ResetPasswordRequest;
import com.flowdaq.app.model.User;


public interface ResetPasswordRequestService {

    public void delete(String requestCode);

    public ResetPasswordRequest getResetPasswordRequest(String requestCode);

	public void createResetPasswordRequest(User user);
}
