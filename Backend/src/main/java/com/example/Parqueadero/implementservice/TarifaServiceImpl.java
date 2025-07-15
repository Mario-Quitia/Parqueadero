package com.example.Parqueadero.implementservice;

import com.example.Parqueadero.entities.Tarifa;
import com.example.Parqueadero.repository.TarifaRepository;
import com.example.Parqueadero.service.TarifaService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TarifaServiceImpl implements TarifaService {

    @Autowired
    private TarifaRepository tarifaRepository;

    @Override
    public List<Tarifa> getAll() {
        return tarifaRepository.findAll();
    }

    @Override
    public Optional<Tarifa> getById(Long id) {
        return tarifaRepository.findById(id);
    }

    @Override
    public Tarifa save(Tarifa tarifa) {
        return tarifaRepository.save(tarifa);
    }

    @Override
    public Tarifa update(Long id, Tarifa tarifaDetails) {
        return tarifaRepository.findById(id).map(tarifa -> {
            tarifa.setTipoVehiculo(tarifaDetails.getTipoVehiculo());
            tarifa.setValorHora(tarifaDetails.getValorHora()); // ← Cambio correcto aquí
            tarifa.setActivo(tarifaDetails.isActivo());
            tarifa.setTipoTiempo(tarifaDetails.getTipoTiempo());
            return tarifaRepository.save(tarifa);
        }).orElseThrow(() -> new RuntimeException("Tarifa no encontrada con ID: " + id));
    }

    @Override
    public void delete(Long id) {
        tarifaRepository.deleteById(id);
    }
}
