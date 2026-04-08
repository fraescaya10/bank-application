package com.devsu.hackerearth.backend.client.model.mapper;

import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;

public class ClientDtoMapper {
    public static ClientDto toDto(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .dni(client.getDni())
                .name(client.getName())
                .password(client.getPassword())
                .gender(client.getGender())
                .age(client.getAge())
                .address(client.getAddress())
                .phone(client.getPhone())
                .isActive(client.isActive())
                .email(client.getEmail())
                .build();
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
