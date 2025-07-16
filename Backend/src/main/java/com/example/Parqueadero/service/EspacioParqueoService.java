
package com.example.Parqueadero.service;

import com.example.Parqueadero.entities.EspacioParqueo;
import java.util.List;
import java.util.Optional;


public interface EspacioParqueoService {
    EspacioParqueo crear(EspacioParqueo espacio);
    Optional<EspacioParqueo> obtenerPorId(Long id);
    List<EspacioParqueo> obtenerTodos();
    EspacioParqueo actualizar(Long id, EspacioParqueo espacioActualizado);
    void eliminar(Long id);
    void generarEspacios(int cantidad, String tipo);
    void eliminarMultiples(List<Long> ids);

}