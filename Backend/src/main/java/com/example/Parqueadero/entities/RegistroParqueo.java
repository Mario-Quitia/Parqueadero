package com.example.Parqueadero.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registros_parqueo")
public class RegistroParqueo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Vehículo asociado al registro
    @ManyToOne(optional = false)
    @JoinColumn(name = "placa_vehiculo", referencedColumnName = "placa", nullable = false)
    private Vehiculo vehiculo;

    // Hora de entrada del vehículo
    @Column(name = "hora_entrada", nullable = false)
    private LocalDateTime horaEntrada;

    // Hora de salida (si es null, el vehículo sigue parqueado)
    @Column(name = "hora_salida")
    private LocalDateTime horaSalida;

    // Monto total pagado por este registro
    @Column(name = "total_pagado")
    private Double totalPagado;

    // Espacio físico donde se parqueó el vehículo
    @ManyToOne(optional = false)
    @JoinColumn(name = "espacio_id", nullable = false)
    private EspacioParqueo espacioParqueo;

    // Tarifa usada para calcular el costo
    @ManyToOne(optional = false)
    @JoinColumn(name = "tarifa_id", nullable = false)
    private Tarifa tarifa;

    // Usuario del sistema que registró la entrada


    // Constructor vacío requerido por JPA
    public RegistroParqueo() {}

    // Constructor con atributos clave
    public RegistroParqueo(Vehiculo vehiculo, LocalDateTime horaEntrada, EspacioParqueo espacioParqueo, Tarifa tarifa) {
        this.vehiculo = vehiculo;
        this.horaEntrada = horaEntrada;
        this.espacioParqueo = espacioParqueo;
        this.tarifa = tarifa;
        this.totalPagado = 0.0; // Por defecto 0 hasta que se realice el pago
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }

    public LocalDateTime getHoraEntrada() { return horaEntrada; }
    public void setHoraEntrada(LocalDateTime horaEntrada) { this.horaEntrada = horaEntrada; }

    public LocalDateTime getHoraSalida() { return horaSalida; }
    public void setHoraSalida(LocalDateTime horaSalida) { this.horaSalida = horaSalida; }

    public Double getTotalPagado() { return totalPagado; }
    public void setTotalPagado(Double totalPagado) { this.totalPagado = totalPagado; }

    public EspacioParqueo getEspacioParqueo() { return espacioParqueo; }
    public void setEspacioParqueo(EspacioParqueo espacioParqueo) { this.espacioParqueo = espacioParqueo; }

    public Tarifa getTarifa() { return tarifa; }
    public void setTarifa(Tarifa tarifa) { this.tarifa = tarifa; }

    
}
