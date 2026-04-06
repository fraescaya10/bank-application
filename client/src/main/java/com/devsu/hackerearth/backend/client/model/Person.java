package com.devsu.hackerearth.backend.client.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Person extends Base {
	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String dni;

	private String gender;

	@Column(nullable = false)
	private int age;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String phone;
}
