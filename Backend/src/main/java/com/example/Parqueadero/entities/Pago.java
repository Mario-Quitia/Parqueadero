
package com.example.Parqueadero.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "registro_parqueo_id", unique = true)
    private RegistroParqueo registroParqueo;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    @Column(nullable = false)
    private Double monto;

    @Column(name = "metodo_pago", length = 20)
    private String metodoPago;

    @ManyToOne
@JoinColumn(name = "usuario_pago_id", nullable = false)
private UsuarioSistema usuarioPago;
    
    
    
    public Pago() {}

    public Pago(RegistroParqueo registroParqueo, LocalDateTime fechaPago, Double monto, String metodoPago) {
        this.registroParqueo = registroParqueo;
        this.fechaPago = fechaPago;
        this.monto = monto;
        this.metodoPago = metodoPago;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegistroParqueo getRegistroParqueo() {
        return registroParqueo;
    }

    public void setRegistroParqueo(RegistroParqueo registroParqueo) {
        this.registroParqueo = registroParqueo;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
    
   public UsuarioSistema getUsuarioPago() {
    return usuarioPago;
}

public void setUsuarioPago(UsuarioSistema usuarioPago) {
    this.usuarioPago = usuarioPago;
}
 
    
}
