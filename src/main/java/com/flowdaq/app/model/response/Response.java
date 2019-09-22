package com.flowdaq.app.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Response {

	public enum ResponseStatusEnum {
		SUCCESS, ERROR, WARNING, NO_ACCESS, TOKEN_EXIPRED
	};

	@ApiModelProperty(required = true)
	private ResponseStatusEnum operationStatus;
	private String message;
	private UserItem item;
}
