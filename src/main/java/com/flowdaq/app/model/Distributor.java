package com.flowdaq.app.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "distributors")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class Distributor implements Serializable {

	private static final long serialVersionUID = -2036999622796996407L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "distributor_name")
	private String distributorName;

	@Column(name = "address_id")
	private Long addressId;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "address_id", insertable = false, updatable = false)
	private Address deliveryAddress;
}
