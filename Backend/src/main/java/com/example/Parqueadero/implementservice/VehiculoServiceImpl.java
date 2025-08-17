package com.example.Parqueadero.implementservice;

import com.example.Parqueadero.entities.Vehiculo;
import com.example.Parqueadero.repository.VehiculoRepository;
import com.example.Parqueadero.service.VehiculoService;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoServiceImpl(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    @Override
    public Vehiculo crearVehiculo(Vehiculo vehiculo) {
        // Normalizar placa a mayúsculas
        String placa = vehiculo.getPlaca().toUpperCase();
        vehiculo.setPlaca(placa);

        // Validación previa
        if (vehiculoRepository.existsById(placa)) {
            throw new RuntimeException("El vehículo con la placa " + placa + " ya está registrado.");
        }

        // Guardar y capturar posibles errores de duplicado en la base de datos
        try {
            return vehiculoRepository.save(vehiculo);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("No se pudo guardar el vehículo: la placa " + placa + " ya existe.");
        }
    }

    @Override
    public Vehiculo actualizarVehiculo(String placa, Vehiculo vehiculo) {
        // Normalizar placa
        placa = placa.toUpperCase();

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
        return vehiculoRepository.findById(placa.toUpperCase());
    }

    @Override
    public List<Vehiculo> listarVehiculos() {
        return vehiculoRepository.findAll();
    }

    @Override
    public void eliminarVehiculo(String placa) {
        vehiculoRepository.deleteById(placa.toUpperCase());
    }
}

