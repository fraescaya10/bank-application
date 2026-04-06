package com.devsu.hackerearth.backend.account.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@GetMapping
	public ResponseEntity<List<AccountDto>> getAll() {
		// api/accounts
		// Get all accounts
		List<AccountDto> accountDtos = this.accountService.getAll();
		return new ResponseEntity<List<AccountDto>>(accountDtos, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AccountDto> get(@PathVariable Long id) {
		// api/accounts/{id}
		// Get accounts by id
		AccountDto accountDto = this.accountService.getById(id);
		return new ResponseEntity<AccountDto>(accountDto, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<AccountDto> create(@RequestBody AccountDto accountDto) {
		// api/accounts
		AccountDto accountDtoSaved = this.accountService.create(accountDto);
		return new ResponseEntity<AccountDto>(accountDtoSaved, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<AccountDto> update(@PathVariable Long id, @RequestBody AccountDto accountDto) {
		// api/accounts/{id}
		// Update accounts
		AccountDto accountUpdated = this.accountService.update(id, accountDto);
		return new ResponseEntity<AccountDto>(accountUpdated, HttpStatus.OK);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<AccountDto> partialUpdate(@PathVariable Long id,
			@RequestBody PartialAccountDto partialAccountDto) {
		// api/accounts/{id}
		// Partial update accounts
		AccountDto accountUpdated = this.accountService.partialUpdate(id, partialAccountDto);
		return new ResponseEntity<AccountDto>(accountUpdated, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		// api/accounts/{id}
		this.accountService.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
