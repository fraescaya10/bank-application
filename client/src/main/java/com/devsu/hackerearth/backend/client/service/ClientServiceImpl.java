package com.devsu.hackerearth.backend.client.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsu.hackerearth.backend.client.exception.ClientException;
import com.devsu.hackerearth.backend.client.exception.ResourceNotFoundException;
import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.model.mapper.ClientDtoMapper;
import com.devsu.hackerearth.backend.client.repository.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService {

	private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);
	private final ClientRepository clientRepository;

	public ClientServiceImpl(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	@Override
	public List<ClientDto> getAll() {
		// Get all clients
		return this.clientRepository.findAll().stream().map(ClientDtoMapper::toDto).collect(Collectors.toList());
	}

	@Override
	public ClientDto getById(Long id) {
		// Get clients by id
		Client client = this.clientRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Client", id));
		return ClientDtoMapper.toDto(client);
	}

	@Transactional
	@Override
	public ClientDto create(ClientDto clientDto) {
		// Create client

		try {
			if(this.clientRepository.existsByDni(clientDto.getDni())) {
				throw new ClientException("Dni already exists");
			}

			if(this.clientRepository.existsByEmail(clientDto.getEmail())) {
				throw new ClientException("Email already exists");
			}

			logger.info("Attempting to create a client");
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

			Client newClient = ClientDtoMapper.toEntity(clientDto);
			newClient.setPassword(passwordEncoder.encode(clientDto.getPassword()));

			Client clientSaved = this.clientRepository.save(newClient);
			logger.info("Client created successfully!");

			return ClientDtoMapper.toDto(clientSaved);
		} catch (ClientException ex) {
			logger.error("Error on create Client: ", ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			logger.error("Error on create Client: ", ex);
			throw new ClientException("Error on create Client: " + ex.getMessage());
		}
	}

	@Override
	public ClientDto update(Long id, ClientDto clientDto) {
		// Update client
		return null;
	}

	@Override
	public ClientDto partialUpdate(Long id, PartialClientDto partialClientDto) {
		// Partial update account
		return null;
	}

	@Override
	public void deleteById(Long id) {
		// Delete client
	}
}
