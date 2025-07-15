package com.example.Parqueadero.service;

import com.example.Parqueadero.entities.RegistroParqueo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RegistroParqueoService {

    RegistroParqueo crearRegistro(RegistroParqueo registro);

    RegistroParqueo cerrarRegistro(Long id, LocalDateTime horaSalida);

    Optional<RegistroParqueo> obtenerPorId(Long id);

    List<RegistroParqueo> listarTodos();

    List<RegistroParqueo> listarActivos();

    List<RegistroParqueo> listarPorPlaca(String placa);

    void eliminarRegistro(Long id);
}
