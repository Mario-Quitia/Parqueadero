package com.example.Parqueadero.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registros_parqueo")
public class RegistroParqueo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "placa_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    @Column(name = "hora_entrada", nullable = false)
    private LocalDateTime horaEntrada;

    @Column(name = "hora_salida")
    private LocalDateTime horaSalida;

    @Column(name = "total_pagado")
    private Double totalPagado;

    @ManyToOne
    @JoinColumn(name = "espacio_id", nullable = false)
    private EspacioParqueo espacioParqueo;
    
         @ManyToOne
    @JoinColumn(name = "tarifa_id", nullable = false)
    private Tarifa tarifa;
    
     @ManyToOne
@JoinColumn(name = "usuario_id", nullable = false)
private UsuarioSistema usuarioRegistro;
    
         
    
    

    // Constructor vacío requerido por JPA
    public RegistroParqueo() {}

    // Constructor con atributos
    public RegistroParqueo(Vehiculo vehiculo, LocalDateTime horaEntrada, LocalDateTime horaSalida,
                           Double totalPagado, EspacioParqueo espacioParqueo) {
        this.vehiculo = vehiculo;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.totalPagado = totalPagado;
        this.espacioParqueo = espacioParqueo;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalDateTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public LocalDateTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalDateTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public Double getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(Double totalPagado) {
        this.totalPagado = totalPagado;
    }

    public EspacioParqueo getEspacioParqueo() {
        return espacioParqueo;
    }

    public void setEspacioParqueo(EspacioParqueo espacioParqueo) {
        this.espacioParqueo = espacioParqueo;
    }
   
    public UsuarioSistema getUsuarioRegistro() {
    return usuarioRegistro;
}

public void setUsuarioRegistro(UsuarioSistema usuarioRegistro) {
    this.usuarioRegistro = usuarioRegistro;
}
    
  public Tarifa getTarifa() {
    return tarifa;
}

public void setTarifa(Tarifa tarifa) {
    this.tarifa = tarifa;
}  
    
}
