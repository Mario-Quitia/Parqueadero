package com.example.Parqueadero.controllers;

import com.example.Parqueadero.entities.Tarifa;
import com.example.Parqueadero.service.TarifaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tarifas")
@CrossOrigin(origins = "*")
public class TarifaController {

    @Autowired
    private TarifaService tarifaService;

    // 🔹 Obtener todas las tarifas
    @GetMapping("/listar")
    public List<Tarifa> getAllTarifas() {
        return tarifaService.getAll();
    }

    // 🔹 Obtener una tarifa por ID
    @GetMapping("/ver/{id}")
    public ResponseEntity<Tarifa> getTarifaById(@PathVariable Long id) {
        return tarifaService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Crear nueva tarifa
    @PostMapping("/crear")
    public ResponseEntity<Tarifa> createTarifa(@RequestBody Tarifa tarifa) {
        return ResponseEntity.ok(tarifaService.save(tarifa));
    }

    // 🔹 Editar tarifa existente
    @PutMapping("/editar/{id}")
    public ResponseEntity<Tarifa> updateTarifa(@PathVariable Long id, @RequestBody Tarifa tarifaDetails) {
        try {
            Tarifa updated = tarifaService.update(id, tarifaDetails);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 🔹 Eliminar tarifa
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> deleteTarifa(@PathVariable Long id) {
        tarifaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
