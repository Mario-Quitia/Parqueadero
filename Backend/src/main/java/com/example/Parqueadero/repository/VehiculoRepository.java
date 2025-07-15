
package com.example.Parqueadero.repository;

import com.example.Parqueadero.entities.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculoRepository extends JpaRepository<Vehiculo, String> {
  
}