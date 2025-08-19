package com.example.Parqueadero.service;

import com.example.Parqueadero.entities.Pago;
import com.example.Parqueadero.enums.MetodoPago;
import java.util.List;

public interface PagoService {
    Pago pagar(String placa, MetodoPago metodoPago);
    
    List<Pago> listarPagos();
    
}
