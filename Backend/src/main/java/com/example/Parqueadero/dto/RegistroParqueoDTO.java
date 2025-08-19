package com.example.Parqueadero.dto;

import java.time.LocalDateTime;

public class RegistroParqueoDTO {
    private String placa;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSalida;

    public RegistroParqueoDTO(String placa, LocalDateTime horaEntrada, LocalDateTime horaSalida) {
        this.placa = placa;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
    }

    public String getPlaca() { return placa; }
    public LocalDateTime getHoraEntrada() { return horaEntrada; }
    public LocalDateTime getHoraSalida() { return horaSalida; }
}
