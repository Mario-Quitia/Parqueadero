package com.example.Parqueadero.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "usuarios_sistema")
public class UsuarioSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 50)
    private String usuario;

    @Column(nullable = false, length = 100)
    private String contrasena;

    @Column(nullable = false, length = 30)
    private String rol; // Ej: "ADMIN", "CAJERO", "VIGILANTE"

    @Column(nullable = false)
    private boolean activo;

    // Relaciones
   @JsonIgnore

    @OneToMany(mappedBy = "usuarioRegistro")
    private List<RegistroParqueo> registrosParqueo;

    @OneToMany(mappedBy = "usuarioPago")
    private List<Pago> pagosRealizados;

    // Constructor vacío
    public UsuarioSistema() {}

    // Constructor con atributos
    public UsuarioSistema(String nombre, String usuario, String contrasena, String rol, boolean activo) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.rol = rol;
        this.activo = activo;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public List<RegistroParqueo> getRegistrosParqueo() {
        return registrosParqueo;
    }

    public void setRegistrosParqueo(List<RegistroParqueo> registrosParqueo) {
        this.registrosParqueo = registrosParqueo;
    }

    public List<Pago> getPagosRealizados() {
        return pagosRealizados;
    }

    public void setPagosRealizados(List<Pago> pagosRealizados) {
        this.pagosRealizados = pagosRealizados;
    }
}
