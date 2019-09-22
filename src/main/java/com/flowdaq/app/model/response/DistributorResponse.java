package com.flowdaq.app.model.response;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class DistributorResponse {
  private List<UserItem> items;
}
