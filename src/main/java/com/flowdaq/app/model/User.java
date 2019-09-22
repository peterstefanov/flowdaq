package com.flowdaq.app.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class User implements UserDetails {

	private static final long serialVersionUID = 8620846088208393250L;

	@Id
	@Column(name = "user_name")
	private String username;

	@Column(name = "email_address")
	private String emailAddress;

	@Column(name = "password")
	private String password;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "distributor_id")
	private Long distributorId;

	@Transient
	private String token;
	
	@Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private Role role;
    
	public String getFullName() {
		return this.firstName + "" + this.lastName;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<>();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
