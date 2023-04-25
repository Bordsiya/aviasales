package com.example.aviasales.exception.not_found;


public class TariffNotFoundException extends ResourceNotFoundException{
    public TariffNotFoundException(Long tariffId) {
        super("Tariff", "tariff-id", tariffId.toString());
    }
}
