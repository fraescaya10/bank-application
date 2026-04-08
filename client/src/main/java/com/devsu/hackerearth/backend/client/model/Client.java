package com.devsu.hackerearth.backend.client.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Client extends Person {
	@Column(nullable=false, unique = true)
	private String password;
	private boolean isActive;
	private boolean isDeleted;
}
