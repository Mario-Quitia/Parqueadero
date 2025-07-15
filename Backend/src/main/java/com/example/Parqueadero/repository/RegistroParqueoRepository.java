
package com.example.Parqueadero.repository;

import com.example.Parqueadero.entities.RegistroParqueo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RegistroParqueoRepository extends JpaRepository<RegistroParqueo, Long> {

    // Opcionales útiles

    // Registros activos (vehículos sin hora de salida)
    List<RegistroParqueo> findByHoraSalidaIsNull();

    // Buscar registros por placa del vehículo
    List<RegistroParqueo> findByVehiculo_Placa(String placa);
    
    
    

}