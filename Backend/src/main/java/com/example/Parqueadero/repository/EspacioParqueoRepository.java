package com.example.Parqueadero.repository;

import com.example.Parqueadero.entities.EspacioParqueo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspacioParqueoRepository extends JpaRepository<EspacioParqueo, Long> {

    // Buscar espacios por tipo
    List<EspacioParqueo> findByTipo(String tipo);

    // Buscar espacios por estado
    List<EspacioParqueo> findByEstado(String estado);

    // Buscar por tipo y estado (por ejemplo: todos los espacios "CARRO" que estén "LIBRE")
    List<EspacioParqueo> findByTipoAndEstado(String tipo, String estado);

    // Buscar el primer espacio disponible por tipo y estado
    Optional<EspacioParqueo> findFirstByTipoAndEstado(String tipo, String estado);

    // Verificar si un número de espacio ya existe
    boolean existsByNumero(String numero);
    
     List<EspacioParqueo> findByTipoOrderByNumeroAsc(String tipo);
    
    
    
}
