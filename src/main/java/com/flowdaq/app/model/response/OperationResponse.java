package com.flowdaq.app.model.response;

import io.swagger.annotations.*;
import lombok.*;

@Data 
public class OperationResponse {
  public enum ResponseStatusEnum {SUCCESS, ERROR, WARNING, NO_ACCESS};
  
  @ApiModelProperty(required = true)
  private ResponseStatusEnum  operationStatus;
  private String  operationMessage;
}
