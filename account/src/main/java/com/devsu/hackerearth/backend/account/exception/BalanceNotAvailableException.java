package com.devsu.hackerearth.backend.account.exception;

public class BalanceNotAvailableException extends RuntimeException {
    public BalanceNotAvailableException(){
        super("Saldo no disponible");
    }
}
