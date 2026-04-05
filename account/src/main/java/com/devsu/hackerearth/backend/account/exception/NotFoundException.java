package com.devsu.hackerearth.backend.account.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String resource, Long id) {
        super(resource + " not found with id: " + id);
    }
}
