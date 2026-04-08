package com.devsu.hackerearth.backend.account.model.dto.client;

import lombok.Data;

@Data
public class ClientDto {

	private Long id;
	private String dni;
	private String name;
	private String password;
	private String gender;
	private int age;
	private String address;
	private String phone;
	private boolean isActive;
	private String email;
}
