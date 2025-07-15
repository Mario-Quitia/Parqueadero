package com.example.Parqueadero.controllers;

import com.example.Parqueadero.entities.EspacioParqueo;
import com.example.Parqueadero.service.EspacioParqueoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/espacios-parqueo")
@CrossOrigin(origins = "*") // Si estás haciendo pruebas desde Postman o un frontend
public class EspacioParqueoController {

    private final EspacioParqueoService espacioService;

    public EspacioParqueoController(EspacioParqueoService espacioService) {
        this.espacioService = espacioService;
    }

    // Crear un nuevo espacio
    @PostMapping("/crear")
    public ResponseEntity<EspacioParqueo> crearEspacio(@RequestBody @Valid EspacioParqueo espacio) {
        return ResponseEntity.ok(espacioService.crear(espacio));
    }

    // Listar todos los espacios
    @GetMapping("/listar")
    public ResponseEntity<List<EspacioParqueo>> listarEspacios() {
        return ResponseEntity.ok(espacioService.obtenerTodos());
    }

    // Obtener espacio por ID
    @GetMapping("/detalle/{id}")
    public ResponseEntity<EspacioParqueo> obtenerEspacioPorId(@PathVariable Long id) {
        return espacioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar espacio por ID
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EspacioParqueo> actualizarEspacio(@PathVariable Long id, @RequestBody @Valid EspacioParqueo espacio) {
        try {
            return ResponseEntity.ok(espacioService.actualizar(id, espacio));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar espacio por ID
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarEspacio(@PathVariable Long id) {
        espacioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Generar múltiples espacios automáticamente
    @PostMapping("/generar")
    public ResponseEntity<String> generarEspacios(
            @RequestParam int cantidad,
            @RequestParam String tipo) {
        if (cantidad <= 0) {
            return ResponseEntity.badRequest().body("La cantidad debe ser mayor que cero.");
        }

        espacioService.generarEspacios(cantidad, tipo);
        return ResponseEntity.ok("Se generaron " + cantidad + " espacios del tipo '" + tipo.toUpperCase() + "' correctamente.");
    }
}

