package com.flowdaq.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
public class Distributor {

	@Id
	@GeneratedValue
	@Column(name = "id")
    private Long id;
	
	@Column(name = "distributor_name")
    private String distributorName;
}
