
package com.example.Parqueadero.repository;

import com.example.Parqueadero.entities.UsuarioSistema;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioSistemaRepository extends JpaRepository<UsuarioSistema, Long> {
    Optional<UsuarioSistema> findByUsuario(String usuario);
}