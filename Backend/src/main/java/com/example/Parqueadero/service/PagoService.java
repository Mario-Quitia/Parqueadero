package com.example.Parqueadero.service;

import com.example.Parqueadero.entities.Pago;
import com.example.Parqueadero.enums.MetodoPago;

public interface PagoService {
    Pago pagar(String placa, MetodoPago metodoPago);
}
