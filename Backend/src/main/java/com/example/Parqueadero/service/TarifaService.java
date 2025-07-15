package com.example.Parqueadero.service;

import com.example.Parqueadero.entities.Tarifa;
import java.util.List;
import java.util.Optional;

public interface TarifaService {

    List<Tarifa> getAll();

    Optional<Tarifa> getById(Long id);

    Tarifa save(Tarifa tarifa);

    Tarifa update(Long id, Tarifa tarifa);

    void delete(Long id);
}
