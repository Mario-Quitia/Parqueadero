package com.example.Parqueadero.implementservice;

import com.example.Parqueadero.entities.Pago;
import com.example.Parqueadero.entities.RegistroParqueo;
import com.example.Parqueadero.enums.MetodoPago;
import com.example.Parqueadero.repository.PagoRepository;
import com.example.Parqueadero.repository.RegistroParqueoRepository;
import com.example.Parqueadero.service.PagoService;
import com.example.Parqueadero.service.RegistroParqueoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagoServiceImpl implements PagoService {

    private final RegistroParqueoRepository registroParqueoRepository;
    private final PagoRepository pagoRepository;
    private final RegistroParqueoService registroParqueoService;

    @Autowired
    public PagoServiceImpl(
            RegistroParqueoRepository registroParqueoRepository,
            PagoRepository pagoRepository,
            RegistroParqueoService registroParqueoService
    ) {
        this.registroParqueoRepository = registroParqueoRepository;
        this.pagoRepository = pagoRepository;
        this.registroParqueoService = registroParqueoService;
    }

    @Override
    @Transactional
    public Pago pagar(String placa, MetodoPago metodo) {
        // Buscar el registro activo del vehículo
        RegistroParqueo registro = registroParqueoRepository
                .findByVehiculo_PlacaAndHoraSalidaIsNull(placa)
                .orElseThrow(() -> new RuntimeException("No hay registro activo para la placa: " + placa));

        // Cerrar el registro y calcular el total a pagar
        RegistroParqueo registroCerrado = registroParqueoService.cerrarRegistro(
                registro.getId(),
                LocalDateTime.now()
        );

        // Crear el pago con el total calculado
        Pago pago = new Pago();
        pago.setRegistroParqueo(registroCerrado);
        pago.setMetodoPago(metodo);
        pago.setMonto(registroCerrado.getTotalPagado());
        pago.setFechaPago(LocalDateTime.now());

        // Guardar y retornar el pago
        return pagoRepository.save(pago);
    }
    
    
      @Override
    public List<Pago> listarPagos() {
        return pagoRepository.findAll();
    }
}
