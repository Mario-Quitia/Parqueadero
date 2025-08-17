
package com.example.Parqueadero.controllers;

import com.example.Parqueadero.entities.Pago;
import com.example.Parqueadero.enums.MetodoPago;
import com.example.Parqueadero.service.PagoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    // Endpoint para realizar un pago
    @PostMapping("/pagar")
    public ResponseEntity<Pago> pagar(
            @RequestParam String placa,
            @RequestParam MetodoPago metodoPago) {

        Pago pago = pagoService.pagar(placa, metodoPago);
        return ResponseEntity.ok(pago);
    }}