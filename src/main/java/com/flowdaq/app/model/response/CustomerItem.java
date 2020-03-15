package com.flowdaq.app.model.response;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CustomerItem {
	
	private Long customerId;
	private String companyName;
	private int full;
	private int empty;
	private int max;
	private int capacity;
	private Date deliveryDate;
	private int count;
	private String contact;	
	private String altContact;
	private Long relatedTo;
	private UserItem userItem;
}
