package com.flowdaq.app.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserItem extends Response{

    private String  token;
    private String  userId;
    private String  firstName;
    private String  lastName;
    private String  email;
    private String role;
    private String distributorName;
    private Long distributorId;
    private AddressItem address;
}
