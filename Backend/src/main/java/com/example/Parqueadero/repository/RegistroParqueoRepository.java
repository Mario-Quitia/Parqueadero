package com.example.Parqueadero.repository;

import com.example.Parqueadero.entities.RegistroParqueo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroParqueoRepository extends JpaRepository<RegistroParqueo, Long> {

    // Todos los registros activos (sin salida)
    List<RegistroParqueo> findByHoraSalidaIsNull();

    // Buscar todos los registros por placa
    List<RegistroParqueo> findByVehiculo_Placa(String placa);

    // Buscar el registro activo por placa (para pagar)
    Optional<RegistroParqueo> findByVehiculo_PlacaAndHoraSalidaIsNull(String placa);
}
