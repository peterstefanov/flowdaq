package com.flowdaq.app.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "resetpassword")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties
@Data
@EqualsAndHashCode(callSuper = false)
public class ResetPasswordRequest implements Serializable {

	private static final long serialVersionUID = -4509588411703844340L;	
    
	@Id
	@Column(name = "user_name")
	private String username;
    
	@Column(name = "request_code")
	private String requestCode;
	
	@Column(name = "created_at")
	private Date createdAt;		
}
