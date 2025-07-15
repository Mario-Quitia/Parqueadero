package com.example.Parqueadero.controllers;

import com.example.Parqueadero.entities.Vehiculo;
import com.example.Parqueadero.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vehiculos")
@CrossOrigin(origins = "*")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    // Crear vehículo
    @PostMapping("/crear")
    public ResponseEntity<Vehiculo> crearVehiculo(@RequestBody Vehiculo vehiculo) {
        return ResponseEntity.ok(vehiculoService.crearVehiculo(vehiculo));
    }

    // Obtener un vehículo por placa
    @GetMapping("/obtener/{placa}")
    public ResponseEntity<Vehiculo> obtenerVehiculo(@PathVariable String placa) {
        Optional<Vehiculo> vehiculo = vehiculoService.obtenerVehiculoPorPlaca(placa);
        return vehiculo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Listar todos los vehículos
    @GetMapping("/listar")
    public ResponseEntity<List<Vehiculo>> listarVehiculos() {
        return ResponseEntity.ok(vehiculoService.listarVehiculos());
    }

    // Actualizar un vehículo
    @PutMapping("/editar/{placa}")
    public ResponseEntity<Vehiculo> actualizarVehiculo(@PathVariable String placa, @RequestBody Vehiculo vehiculo) {
        try {
            Vehiculo actualizado = vehiculoService.actualizarVehiculo(placa, vehiculo);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un vehículo
    @DeleteMapping("/eliminar/{placa}")
    public ResponseEntity<Void> eliminarVehiculo(@PathVariable String placa) {
        vehiculoService.eliminarVehiculo(placa);
        return ResponseEntity.noContent().build();
    }
}
