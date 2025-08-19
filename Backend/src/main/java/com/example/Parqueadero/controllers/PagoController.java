
package com.example.Parqueadero.controllers;

import com.example.Parqueadero.entities.Pago;
import com.example.Parqueadero.enums.MetodoPago;
import com.example.Parqueadero.service.PagoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    @Autowired
    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    // Endpoint para pagar usando solo la placa y el método de pago
@PostMapping("/pagar/{placa}")
public ResponseEntity<Pago> pagar(
       @PathVariable String placa,
       @RequestParam("metodoPago") MetodoPago metodoPago
) {
    Pago pago = pagoService.pagar(placa, metodoPago);
    return ResponseEntity.ok(pago);
}

  @GetMapping("/listar")
    public List<Pago> listarPagos() {
        return pagoService.listarPagos();
    }



}
