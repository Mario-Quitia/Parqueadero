package com.example.Parqueadero.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehiculos") // opcional, puedes omitirlo si querés que la tabla se llame "vehiculo"
public class Vehiculo {

    
  @Id
   @Column(name = "placa", nullable = false, unique = true, length = 10)
private String placa;

    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo;  // Ej: carro, moto, bicicleta

    @Column(name = "color", length = 30)
    private String color;

    @Column(name = "marca", length = 50)
    private String marca;

    @Column(name = "modelo", length = 10)
    private String modelo;

    public Vehiculo() {
    }

    public Vehiculo(String placa, String tipo, String color, String marca, String modelo) {
        this.placa = placa;
        this.tipo = tipo;
        this.color = color;
        this.marca = marca;
        this.modelo = modelo;
    }

    
    // Getters y Setters

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

  
}
