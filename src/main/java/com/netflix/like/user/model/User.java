package com.netflix.like.user.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("user")
public class User implements Serializable {

	public enum UserStatus {
		ACTIVE, SUSPENDED, REMOVED, ADMIN, PROVIDER;
	}
	
	public enum Country {
		FR, US, DE, GB;
	}

	private static final long serialVersionUID = 1L;
	@Id
	private Integer id;
	private String lastName;
	private String firstName;
	private String email;
	private String address;
	private Country country;
	private UserStatus status;

	public User(Integer id, String lastName, String firstName, String email, String address, Country country,
			UserStatus status) {
		super();
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.address = address;
		this.country = country;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
