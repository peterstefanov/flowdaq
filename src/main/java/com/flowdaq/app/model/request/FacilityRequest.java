package com.flowdaq.app.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FacilityRequest extends CustomerRequest {

	private Long relatedTo;
}
