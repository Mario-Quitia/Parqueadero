
package com.example.Parqueadero.implementservice;

import com.example.Parqueadero.entities.Vehiculo;
import com.example.Parqueadero.repository.VehiculoRepository;
import com.example.Parqueadero.service.VehiculoService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service

public class VehiculoServiceImpl implements VehiculoService {

    public VehiculoServiceImpl(com.example.Parqueadero.repository.VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }
    
        private final VehiculoRepository vehiculoRepository;

      @Override
    public Vehiculo crearVehiculo(Vehiculo vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }

   @Override
    public Vehiculo actualizarVehiculo(String placa, Vehiculo vehiculo) {
        Optional<Vehiculo> existente = vehiculoRepository.findById(placa);
        if (existente.isPresent()) {
            Vehiculo v = existente.get();
            v.setTipo(vehiculo.getTipo());
            v.setColor(vehiculo.getColor());
            v.setMarca(vehiculo.getMarca());
            v.setModelo(vehiculo.getModelo());
            return vehiculoRepository.save(v);
        } else {
            throw new RuntimeException("Vehículo no encontrado con placa: " + placa);
        }
    }
    @Override
    public Optional<Vehiculo> obtenerVehiculoPorPlaca(String placa) {
                return vehiculoRepository.findById(placa);

    }

    @Override
    public List<Vehiculo> listarVehiculos() {
                return vehiculoRepository.findAll();

        
    }

    @Override
    public void eliminarVehiculo(String placa) {
                vehiculoRepository.deleteById(placa);

    }
        
        
        
        
}
