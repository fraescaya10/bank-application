package com.devsu.hackerearth.backend.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.devsu.hackerearth.backend.client.controller.ClientController;
import com.devsu.hackerearth.backend.client.exception.ClientException;
import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.GenderType;
import com.devsu.hackerearth.backend.client.repository.ClientRepository;
import com.devsu.hackerearth.backend.client.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class sampleTest {

    @Nested
    @ExtendWith(MockitoExtension.class)
    class UnitTests {

        @Mock
        private ClientService clientService;

        @InjectMocks
        private ClientController clientController;

        @Test
        void createClientTestSuccess() {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            ClientDto newClient = ClientDto.builder()
                    .dni("1234567890")
                    .name("Client Test")
                    .password("Test_123")
                    .gender(GenderType.FEMENINO)
                    .age(25)
                    .address("Calle Test A y Calle Test B")
                    .phone("34535343534")
                    .email("test@mail.com")
                    .build();

            ClientDto savedClient = ClientDto.builder()
                    .id(1L)
                    .dni("1234567890")
                    .name("Client Test")
                    .password(passwordEncoder.encode("Test_123"))
                    .gender(GenderType.FEMENINO)
                    .age(25)
                    .address("Calle Test A y Calle Test B")
                    .phone("34535343534")
                    .email("test@mail.com")
                    .build();

            when(clientService.create(any(ClientDto.class))).thenReturn(savedClient);

            ResponseEntity<ClientDto> response = clientController.create(newClient);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(savedClient, response.getBody());
        }

        @Test
        void createClientTestWithException() {

            ClientDto newClient = ClientDto.builder()
                    .dni("1234567890")
                    .name("Client Test")
                    .password("Test_123")
                    .gender(GenderType.FEMENINO)
                    .age(25)
                    .address("Calle Test A y Calle Test B")
                    .phone("34535343534")
                    .email("test@mail.com")
                    .build();

            when(clientService.create(any(ClientDto.class))).thenThrow(new ClientException("Test client error"));

            ClientException ex = assertThrows(ClientException.class, () -> clientController.create(newClient));
            assertEquals("Test client error", ex.getMessage());
        }
    }

    @Nested
    @SpringBootTest
    @AutoConfigureMockMvc
    class IntegrationTests {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ClientRepository clientRepository;

        @Autowired
        private ObjectMapper objectMapper;

        private Client savedClient;

        @BeforeEach
        void setup() {
            clientRepository.deleteAll();
            createClient();
        }

        @Test
        void createClientTest() throws Exception {
            ClientDto newClient = ClientDto.builder()
                    .dni("987654321")
                    .name("Client Test 2")
                    .password("Test_123")
                    .gender(GenderType.MASCULINO)
                    .age(35)
                    .address("Avenida Test A y Calle Test B")
                    .phone("5345435345")
                    .email("test2@mail.com")
                    .build();

            mockMvc.perform(post("/api/clients")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newClient)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.dni").value(newClient.getDni()))
                    .andExpect(jsonPath("$.name").value(newClient.getName()))
                    .andExpect(jsonPath("$.active").value(false));

        }

        @Test
        void getAllClientsTest() throws Exception {
            mockMvc.perform(get("/api/clients")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].id").value(savedClient.getId()))
                    .andExpect(jsonPath("$[0].name").value("Client Test"))
                    .andExpect(jsonPath("$[0].phone").value("34535343534"));
        }

        private void createClient() {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            Client client = Client.builder()
                    .id(1L)
                    .dni("1234567890")
                    .name("Client Test")
                    .password(passwordEncoder.encode("Test_123"))
                    .gender(GenderType.FEMENINO)
                    .age(25)
                    .address("Calle Test A y Calle Test B")
                    .phone("34535343534")
                    .email("test@mail.com")
                    .build();
            savedClient = clientRepository.save(client);
        }
    }

}
