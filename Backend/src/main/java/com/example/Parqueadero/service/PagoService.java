
package com.example.Parqueadero.service;

import com.example.Parqueadero.entities.Pago;
import java.util.List;



public interface PagoService {
    Pago realizarPago(Long idRegistroParqueo, String metodoPago, Long idUsuarioSistema);
    List<Pago> obtenerTodos();
    Pago obtenerPorId(Long id);
    void eliminarPago(Long id);
}