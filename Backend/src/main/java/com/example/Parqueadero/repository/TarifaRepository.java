package com.example.Parqueadero.repository;

import com.example.Parqueadero.entities.Tarifa;
import com.example.Parqueadero.enums.TipoTiempo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Long> {

    // Buscar por tipo de vehículo y tipo de tiempo (única tarifa)
    Optional<Tarifa> findByTipoVehiculoAndTipoTiempoAndActivoTrue(String tipoVehiculo, TipoTiempo tipoTiempo);

    // Buscar todas las tarifas activas de un tipo de vehículo
    List<Tarifa> findByTipoVehiculoAndActivoTrue(String tipoVehiculo);
}
