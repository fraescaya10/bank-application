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
		return this.clientRepository.findAllByIsDeletedFalse().stream().map(ClientDtoMapper::toDto).collect(Collectors.toList());
	}

	@Override
	public ClientDto getById(Long id) {
		// Get clients by id
		Client client = this.clientRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new ResourceNotFoundException("Client", id));
		return ClientDtoMapper.toDto(client);
	}

	@Transactional
	@Override
	public ClientDto create(ClientDto clientDto) {
		// Create client

		try {
			if (this.clientRepository.existsByDniAndIsDeletedFalse(clientDto.getDni())) {
				throw new ClientException("Dni already exists");
			}

			if (this.clientRepository.existsByEmailAndIsDeletedFalse(clientDto.getEmail())) {
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
			logger.error("Error on create Client: {}", ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			logger.error("Error on create Client: ", ex);
			throw new ClientException("Error on create Client: " + ex.getMessage());
		}
	}

	@Transactional
	@Override
	public ClientDto update(Long id, ClientDto clientDto) {
		// Update client
		try {
			logger.info("Attempting to update client with id={}", id);
			if (null == id) {
				throw new ClientException("Client ID is null");
			}
			Client clientToUpdate = this.clientRepository.findByIdAndIsDeletedFalse(id)
					.orElseThrow(() -> new ResourceNotFoundException("Client", id));

			clientToUpdate.setDni(clientDto.getDni());
			clientToUpdate.setName(clientDto.getName());
			clientToUpdate.setGender(clientDto.getGender());
			clientToUpdate.setAddress(clientDto.getAddress());
			clientToUpdate.setPhone(clientDto.getPhone());
			clientToUpdate.setActive(clientDto.isActive());
			clientToUpdate.setEmail(clientDto.getEmail());

			Client clientUpdated = this.clientRepository.save(clientToUpdate);
			logger.info("Client updated successfully!");
			return ClientDtoMapper.toDto(clientUpdated);
		} catch (ClientException ex) {
			logger.info("Error on update client: {}", ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			logger.info("Error on update client: ", ex);
			throw new ClientException("Error on update client: " + ex.getMessage());
		}
	}

	@Override
	public ClientDto partialUpdate(Long id, PartialClientDto partialClientDto) {
		// Partial update account
		logger.info("Attempting to partial update client with id={}", id);
		if (null == id) {
			throw new ClientException("Client ID is null");
		}
		Client clientToUpdate = this.clientRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new ResourceNotFoundException("Client", id));
		;
		clientToUpdate.setActive(partialClientDto.isActive());

		Client clientUpdated = this.clientRepository.save(clientToUpdate);

		logger.info("Client partial updated successfully!");
		return ClientDtoMapper.toDto(clientUpdated);
	}

	@Override
	public void deleteById(Long id) {
		// Delete client
		logger.info("Attempting to delete client with id={}", id);
		if (null == id) {
			throw new ClientException("Client ID is null");
		}
		Client clientToDelete = this.clientRepository.findByIdAndIsDeletedFalse(id)
				.orElseThrow(() -> new ResourceNotFoundException("Client", id));
		clientToDelete.setDeleted(true);

		this.clientRepository.save(clientToDelete);
		logger.info("Client deleted successfully!");
	}
}
