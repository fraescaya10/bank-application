package com.devsu.hackerearth.backend.client.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.model.dto.ValidationGroups;
import com.devsu.hackerearth.backend.client.service.ClientService;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

	private final ClientService clientService;

	public ClientController(ClientService clientService) {
		this.clientService = clientService;
	}

	@GetMapping
	public ResponseEntity<List<ClientDto>> getAll() {
		// api/clients
		// Get all clients
		List<ClientDto> clientsList = this.clientService.getAll();
		return new ResponseEntity<>(clientsList, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ClientDto> get(@PathVariable Long id) {
		// api/clients/{id}
		// Get clients by id
		ClientDto client = this.clientService.getById(id);
		return new ResponseEntity<>(client, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ClientDto> create(
			@Validated(ValidationGroups.OnCreate.class) @RequestBody ClientDto clientDto) {
		// api/clients
		// Create client
		ClientDto client = this.clientService.create(clientDto);
		return new ResponseEntity<>(client, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ClientDto> update(@PathVariable Long id, @Valid @RequestBody ClientDto clientDto) {
		// api/clients/{id}
		// Update client
		ClientDto client = this.clientService.update(id, clientDto);
		return new ResponseEntity<>(client, HttpStatus.OK);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ClientDto> partialUpdate(@PathVariable Long id,
			@RequestBody PartialClientDto partialClientDto) {
		// api/accounts/{id}
		// Partial update accounts
		ClientDto client = this.clientService.partialUpdate(id, partialClientDto);
		return new ResponseEntity<>(client, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		// api/clients/{id}
		// Delete client

		this.clientService.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
