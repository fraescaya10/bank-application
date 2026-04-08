package com.devsu.hackerearth.backend.account.service.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.devsu.hackerearth.backend.account.exception.ClientCallException;
import com.devsu.hackerearth.backend.account.model.dto.client.ClientDto;

@Service
public class ClientServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(ClientServiceClient.class);
    private final RestTemplate restTemplate;

    @Value("${client.api.url}")
    private String clientApiUrl;

    public ClientServiceClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public RestTemplate getRestTemplate() {
        return this.restTemplate;
    }

    public ClientDto getClient(Long id) {
        try {
            return this.restTemplate.getForObject(clientApiUrl + "/api/clients/{id}", ClientDto.class, id);
        } catch (Exception ex) {
            logger.error("Error getting client information", ex);
            throw new ClientCallException("Error getting client information: " + ex.getMessage());
        }
    }
}
