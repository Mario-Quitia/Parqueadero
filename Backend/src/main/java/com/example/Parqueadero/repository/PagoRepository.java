
package com.example.Parqueadero.repository;

import com.example.Parqueadero.entities.Pago;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PagoRepository extends JpaRepository<Pago, Long> {
}