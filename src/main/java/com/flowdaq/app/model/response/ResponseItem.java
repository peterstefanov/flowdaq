package com.flowdaq.app.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResponseItem extends Response{

    private String  token;
    private String  userId;
    private String  firstName;
    private String  lastName;
    private String  email;
    private String role;
    private String distributorName;
}
