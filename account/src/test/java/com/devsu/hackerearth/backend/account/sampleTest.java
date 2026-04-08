package com.devsu.hackerearth.backend.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;

import com.devsu.hackerearth.backend.account.controller.AccountController;
import com.devsu.hackerearth.backend.account.exception.AccountException;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.AccountType;
import com.devsu.hackerearth.backend.account.model.dto.client.ClientDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;
import com.devsu.hackerearth.backend.account.service.AccountService;
import com.devsu.hackerearth.backend.account.service.client.ClientServiceClient;
import com.fasterxml.jackson.databind.ObjectMapper;

public class sampleTest {

	@Nested
	@ExtendWith(MockitoExtension.class)
	class UnitTests {

		@Mock
		private AccountService accountService;

		@InjectMocks
		private AccountController accountController;

		@Test
		void createAccountTest() {

			AccountDto newAccountDto = AccountDto.builder()
					.type(AccountType.AHORROS)
					.initialAmount(BigDecimal.valueOf(100.0))
					.clientId(123L)
					.build();

			AccountDto accountDtoSaved = AccountDto.builder()
					.id(1L)
					.number(UUID.randomUUID().toString())
					.type(AccountType.AHORROS)
					.initialAmount(BigDecimal.valueOf(100.0))
					.clientId(123L)
					.isActive(false)
					.balance(BigDecimal.valueOf(100.0))
					.build();

			when(accountService.create(any(AccountDto.class))).thenReturn(accountDtoSaved);

			// Act
			ResponseEntity<AccountDto> response = accountController.create(newAccountDto);

			// Assert
			assertEquals(HttpStatus.CREATED, response.getStatusCode());
			assertEquals(accountDtoSaved, response.getBody());
		}

		@Test
		void createAccountTestWithException() {
			AccountDto newAccountDto = AccountDto.builder()
					.type(AccountType.AHORROS)
					.initialAmount(BigDecimal.valueOf(100.0))
					.clientId(123L)
					.build();

			when(accountService.create(any(AccountDto.class))).thenThrow(new AccountException("Test account error"));

			AccountException ex = assertThrows(AccountException.class, () -> accountController.create(newAccountDto));
			assertEquals("Test account error", ex.getMessage());
		}
	}

	@Nested
	@SpringBootTest
	@AutoConfigureMockMvc
	class IntegrationTests {
		@Autowired
		private MockMvc mockMvc;

		@Autowired
		private AccountRepository accountRepository;

		@Autowired
		private ObjectMapper objectMapper;

		@Autowired
		private ClientServiceClient clientServiceClient;

		private Account savedAccount;
		private MockRestServiceServer mockRestServiceServer;

		@Value("${client.api.url}")
		private String clientApiUrl;

		@BeforeEach
		void setup() {
			accountRepository.deleteAll();
			this.createAccount();
			mockRestServiceServer = MockRestServiceServer.createServer(clientServiceClient.getRestTemplate());
		}

		@Test
		void createAccountTest() throws Exception {

			ClientDto client = new ClientDto();
			client.setId(456L);
			client.setName("Client test");
			client.setActive(true);

			mockRestServiceServer.expect(ExpectedCount.once(), requestTo(clientApiUrl + "/api/clients/456"))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess(objectMapper.writeValueAsString(client), MediaType.APPLICATION_JSON));

			AccountDto newAccountDto = AccountDto.builder()
					.type(AccountType.AHORROS)
					.initialAmount(BigDecimal.valueOf(100.0))
					.clientId(456L)
					.build();

			mockMvc.perform(post("/api/accounts")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(newAccountDto)))
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$.type").value(newAccountDto.getType().name()))
					.andExpect(jsonPath("$.initialAmount").value(newAccountDto.getInitialAmount()))
					.andExpect(jsonPath("$.clientId").value(newAccountDto.getClientId()));

			mockRestServiceServer.verify();
		}

		@Test
		void getAllAccountsTest() throws Exception {
			mockMvc.perform(get("/api/accounts")
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$").isArray())
					.andExpect(jsonPath("$.length()").value(1))
					.andExpect(jsonPath("$[0].id").value(savedAccount.getId()))
					.andExpect(jsonPath("$[0].clientId").value(savedAccount.getClientId()));
		};

		private void createAccount() {
			Account newAccount = Account.builder()
					.id(1L)
					.number(UUID.randomUUID().toString())
					.type(AccountType.AHORROS)
					.initialAmount(BigDecimal.valueOf(150.0))
					.clientId(123L)
					.clientName("Client test")
					.isActive(false)
					.balance(BigDecimal.valueOf(150.0))
					.build();
			savedAccount = this.accountRepository.save(newAccount);
		}
	}

}
