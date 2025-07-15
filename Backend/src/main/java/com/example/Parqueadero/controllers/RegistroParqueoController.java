package com.example.Parqueadero.controllers;

import com.example.Parqueadero.entities.RegistroParqueo;
import com.example.Parqueadero.service.RegistroParqueoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/registros")
@CrossOrigin(origins = "*")
public class RegistroParqueoController {

    @Autowired
    private RegistroParqueoService registroParqueoService;

    // Crear un nuevo registro (entrada al parqueadero)
    @PostMapping("/crear")
    public ResponseEntity<RegistroParqueo> crearRegistro(@RequestBody RegistroParqueo registro) {
        return ResponseEntity.ok(registroParqueoService.crearRegistro(registro));
    }

    // Cerrar un registro (salida del parqueadero)
    @PutMapping("/cerrar/{id}")
    public ResponseEntity<RegistroParqueo> cerrarRegistro(@PathVariable Long id) {
        try {
            RegistroParqueo cerrado = registroParqueoService.cerrarRegistro(id, null);
            return ResponseEntity.ok(cerrado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener registro por ID
    @GetMapping("/obtener/{id}")
    public ResponseEntity<RegistroParqueo> obtenerPorId(@PathVariable Long id) {
        Optional<RegistroParqueo> registro = registroParqueoService.obtenerPorId(id);
        return registro.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Listar todos los registros
    @GetMapping("/listar")
    public ResponseEntity<List<RegistroParqueo>> listarTodos() {
        return ResponseEntity.ok(registroParqueoService.listarTodos());
    }

    // Listar registros activos (vehículos dentro)
    @GetMapping("/activos")
    public ResponseEntity<List<RegistroParqueo>> listarActivos() {
        return ResponseEntity.ok(registroParqueoService.listarActivos());
    }

    // Listar registros por placa
    @GetMapping("/placa/{placa}")
    public ResponseEntity<List<RegistroParqueo>> listarPorPlaca(@PathVariable String placa) {
        return ResponseEntity.ok(registroParqueoService.listarPorPlaca(placa));
    }

    // Eliminar registro
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarRegistro(@PathVariable Long id) {
        registroParqueoService.eliminarRegistro(id);
        return ResponseEntity.noContent().build();
    }
}
