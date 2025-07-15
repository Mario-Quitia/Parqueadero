
package com.example.Parqueadero.service;

import com.example.Parqueadero.entities.Vehiculo;
import java.util.List;
import java.util.Optional;

public interface VehiculoService {
    
    Vehiculo crearVehiculo(Vehiculo vehiculo);
    Vehiculo actualizarVehiculo(String placa, Vehiculo vehiculo);
    Optional<Vehiculo> obtenerVehiculoPorPlaca(String placa);
    List<Vehiculo> listarVehiculos();
    void eliminarVehiculo(String placa);
     
    
}
