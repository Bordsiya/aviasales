package com.example.aviasales.service;

import com.example.aviasales.entity.Tariff;
import com.example.aviasales.exception.not_found.TariffNotFoundException;
import com.example.aviasales.repo.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TariffService {
    private TariffRepository tariffRepository;

    @Autowired
    public TariffService(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }
    public Tariff getTariffById(Long tariffId) {
        return tariffRepository.findById(tariffId).orElseThrow(() -> new TariffNotFoundException(tariffId));
    }
}
