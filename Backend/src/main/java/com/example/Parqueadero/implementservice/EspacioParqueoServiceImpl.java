package com.example.Parqueadero.implementservice;

import com.example.Parqueadero.entities.EspacioParqueo;
import com.example.Parqueadero.repository.EspacioParqueoRepository;
import com.example.Parqueadero.service.EspacioParqueoService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EspacioParqueoServiceImpl implements EspacioParqueoService {

    private final EspacioParqueoRepository repository;

    @Autowired
    public EspacioParqueoServiceImpl(EspacioParqueoRepository repository) {
        this.repository = repository;
    }

    @Override
    public EspacioParqueo crear(EspacioParqueo espacio) {
        return repository.save(espacio);
    }

    @Override
    public Optional<EspacioParqueo> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<EspacioParqueo> obtenerTodos() {
        return repository.findAll();
    }

    @Override
    public EspacioParqueo actualizar(Long id, EspacioParqueo espacioActualizado) {
        return repository.findById(id).map(e -> {
            e.setNumero(espacioActualizado.getNumero());
            e.setTipo(espacioActualizado.getTipo());
            e.setEstado(espacioActualizado.getEstado());
            return repository.save(e);
        }).orElseThrow(() -> new RuntimeException("Espacio no encontrado"));
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

  @Override
public void generarEspacios(int cantidad, String tipo) {
    List<EspacioParqueo> existentes = repository.findByTipoOrderByNumeroAsc(tipo.toUpperCase());

    int ultimoNumero = 0;

    for (EspacioParqueo e : existentes) {
        String soloNumero = e.getNumero().replaceAll("\\D+", ""); // elimina letras
        if (!soloNumero.isEmpty()) {
            int n = Integer.parseInt(soloNumero);
            if (n > ultimoNumero) ultimoNumero = n;
        }
    }

    for (int i = 1; i <= cantidad; i++) {
        int nuevoNumero = ultimoNumero + i;
        String numeroEspacio = tipo.toUpperCase().charAt(0) + String.valueOf(nuevoNumero); // Ej: "C5", "M12"

        if (!repository.existsByNumero(numeroEspacio)) {
            EspacioParqueo nuevo = new EspacioParqueo(numeroEspacio, tipo.toUpperCase(), "LIBRE");
            repository.save(nuevo);
        }
    }
}

}


