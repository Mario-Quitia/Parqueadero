
package com.example.Parqueadero.implementservice;

import com.example.Parqueadero.entities.UsuarioSistema;
import com.example.Parqueadero.repository.UsuarioSistemaRepository;
import com.example.Parqueadero.service.UsuarioSistemaService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@Service
public class UsuarioSistemaServiceImpl implements UsuarioSistemaService {

    private final UsuarioSistemaRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioSistemaServiceImpl(UsuarioSistemaRepository repository) {
        this.repository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UsuarioSistema crearUsuario(UsuarioSistema usuario) {
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        return repository.save(usuario);
    }

    @Override
    public List<UsuarioSistema> obtenerTodos() {
        return repository.findAll();
    }

    @Override
    public Optional<UsuarioSistema> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public UsuarioSistema actualizarUsuario(Long id, UsuarioSistema usuarioActualizado) {
        return repository.findById(id).map(u -> {
            u.setNombre(usuarioActualizado.getNombre());
            u.setUsuario(usuarioActualizado.getUsuario());
            u.setRol(usuarioActualizado.getRol());
            u.setActivo(usuarioActualizado.isActivo());
            if (!usuarioActualizado.getContrasena().isBlank()) {
                u.setContrasena(passwordEncoder.encode(usuarioActualizado.getContrasena()));
            }
            return repository.save(u);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public void eliminarUsuario(Long id) {
        repository.deleteById(id);
    }
}
