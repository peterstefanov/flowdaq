package com.flowdaq.app.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "customers")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties
@Data
@EqualsAndHashCode(callSuper = false)
public class Customer implements Serializable {

	private static final long serialVersionUID = 784797035057532082L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "distributor_id")
	private Long distributorId;

	@Column(name = "org_name")
	private String companyName;

	@Column(name = "user_name")
	private String username;

	@Column(name = "contact")
	private String contactName;

	@Column(name = "alt_contact")
	private String alternativeContactName;

	@Column(name = "address_id")
	private Long addressId;

	@Column(name = "related_to")
	private Long relatedTo;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_name", insertable = false, updatable = false)
	private User user;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "address_id", insertable = false, updatable = false)
	private Address deliveryAddress;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "id", insertable = false, updatable = false)
	private List<Cooler> coolers;

	public void setCoolers(List<Cooler> coolers) {
		this.coolers = coolers;
	}

	public List<Cooler> getCoolers() {
		return this.coolers;
	}
}
