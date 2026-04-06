package com.devsu.hackerearth.backend.client.model.mapper;

import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;

public class ClientDtoMapper {
    public static ClientDto toDto(Client client) {
        return new ClientDto(client.getId(), client.getDni(), client.getName(), client.getPassword(),
                client.getGender(), client.getAge(), client.getAddress(), client.getPhone(), client.isActive(),
                client.getEmail());
    }

    public static Client toEntity(ClientDto clientDto) {
        return Client.builder()
                .name(clientDto.getName())
                .dni(clientDto.getDni())
                .gender(clientDto.getGender())
                .age(clientDto.getAge())
                .address(clientDto.getAddress())
                .email(clientDto.getEmail())
                .phone(clientDto.getPhone())
                .password(clientDto.getPassword())
                .isActive(clientDto.isActive())
                .build();

    }
}
