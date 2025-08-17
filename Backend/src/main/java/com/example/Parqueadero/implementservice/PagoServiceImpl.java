package com.example.Parqueadero.implementservice;

import com.example.Parqueadero.entities.Pago;
import com.example.Parqueadero.entities.RegistroParqueo;
import com.example.Parqueadero.entities.UsuarioSistema;
import com.example.Parqueadero.enums.MetodoPago;
import com.example.Parqueadero.repository.PagoRepository;
import com.example.Parqueadero.repository.RegistroParqueoRepository;
import com.example.Parqueadero.repository.UsuarioSistemaRepository;
import com.example.Parqueadero.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PagoServiceImpl implements PagoService {

    private final RegistroParqueoRepository registroParqueoRepository;
    private final UsuarioSistemaRepository usuarioSistemaRepository;
    private final PagoRepository pagoRepository;

    @Autowired
    public PagoServiceImpl(
            RegistroParqueoRepository registroParqueoRepository,
            UsuarioSistemaRepository usuarioSistemaRepository,
            PagoRepository pagoRepository
    ) {
        this.registroParqueoRepository = registroParqueoRepository;
        this.usuarioSistemaRepository = usuarioSistemaRepository;
        this.pagoRepository = pagoRepository;
    }

@Override
public Pago pagar(String placa, MetodoPago metodo) {
    RegistroParqueo registro = registroParqueoRepository
            .findByVehiculo_PlacaAndHoraSalidaIsNull(placa)
            .orElseThrow(() -> new RuntimeException("No hay registro activo para la placa: " + placa));

    registro.setHoraSalida(LocalDateTime.now());
    registroParqueoRepository.save(registro);

    Pago pago = new Pago();
    pago.setRegistroParqueo(registro);
    pago.setMetodoPago(metodo);
    pago.setMonto(registro.getTotalPagado());
    pago.setFechaPago(LocalDateTime.now());

    return pagoRepository.save(pago);
}
}
