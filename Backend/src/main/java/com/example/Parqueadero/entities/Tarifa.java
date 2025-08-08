package com.example.Parqueadero.entities;

import com.example.Parqueadero.enums.TipoTiempo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "tarifas")
public class Tarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_vehiculo", nullable = false, length = 20)
    private String tipoVehiculo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_tiempo", nullable = false, length = 10)
    private TipoTiempo tipoTiempo; // HORA, DIA, MES

    @Column(name = "valor_hora", nullable = false)
    private BigDecimal valorHora;

    @Column(name = "valor", nullable = false)
    private Double valor;

    @Column(name = "valor_fraccion")
    private Double valorFraccion;

    @Column(name = "activo", nullable = false)
    
    
    private boolean activo;

    @JsonIgnore
    @OneToMany(mappedBy = "tarifa")
private List<RegistroParqueo> registros;
    
    // Constructor vacío
    public Tarifa() {}

    // Constructor completo
    public Tarifa(String tipoVehiculo, TipoTiempo tipoTiempo, BigDecimal valorHora, Double valor, Double valorFraccion, boolean activo) {
        this.tipoVehiculo = tipoVehiculo;
        this.tipoTiempo = tipoTiempo;
        this.valorHora = valorHora;
        this.valor = valor;
        this.valorFraccion = valorFraccion;
        this.activo = activo;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public TipoTiempo getTipoTiempo() {
        return tipoTiempo;
    }

    public void setTipoTiempo(TipoTiempo tipoTiempo) {
        this.tipoTiempo = tipoTiempo;
    }

    public BigDecimal getValorHora() {
        return valorHora;
    }

    public void setValorHora(BigDecimal valorHora) {
        this.valorHora = valorHora;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getValorFraccion() {
        return valorFraccion;
    }

    public void setValorFraccion(Double valorFraccion) {
        this.valorFraccion = valorFraccion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
