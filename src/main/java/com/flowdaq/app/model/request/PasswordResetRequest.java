package com.flowdaq.app.model.request;

import lombok.Data;

@Data
public class PasswordResetRequest {

	  private String username;

	  private String password;
	  
	  private String requestCode;

}
