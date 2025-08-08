package com.example.Parqueadero.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "espacios_parqueo")
public class EspacioParqueo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El número del espacio no puede estar vacío.")
    @Size(max = 10, message = "El número del espacio no puede tener más de 10 caracteres.")
    @Column(name = "numero", nullable = false, unique = true, length = 10)
    private String numero;

    @NotBlank(message = "El tipo de espacio no puede estar vacío.")
    @Size(max = 20, message = "El tipo de espacio no puede tener más de 20 caracteres.")
    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo;

    @NotBlank(message = "El estado del espacio no puede estar vacío.")
    @Size(max = 15, message = "El estado del espacio no puede tener más de 15 caracteres.")
    @Column(name = "estado", nullable = false, length = 15)
    private String estado;

    @JsonIgnore
    @OneToMany(mappedBy = "espacioParqueo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroParqueo> registros = new ArrayList<>();

    public EspacioParqueo() {}

    public EspacioParqueo(String numero, String tipo, String estado) {
        this.numero = numero;
        this.tipo = tipo;
        this.estado = estado;
    }
    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<RegistroParqueo> getRegistros() {
        return registros;
    }

    public void setRegistros(List<RegistroParqueo> registros) {
        this.registros = registros;
    }
}
