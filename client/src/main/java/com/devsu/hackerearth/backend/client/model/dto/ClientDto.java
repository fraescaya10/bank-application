package com.devsu.hackerearth.backend.client.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDto {

	private Long id;
	@NotNull(message = "Dni is mandatory")
	private String dni;
	@NotBlank(message = "Name is mandatory")
	private String name;
	@NotBlank(message = "Password is mandatory", groups = ValidationGroups.OnCreate.class)
	private String password;
	@NotNull(message = "Gender is mandatory")
	private GenderType gender;
	@NotNull(message = "Age is mandatory")
	private int age;
	@NotBlank(message = "Address is mandatory")
	private String address;
	@NotBlank(message = "Phone is mandatory")
	private String phone;
	private boolean isActive;
	@NotBlank(message = "email is mandatory")
	private String email;
}
