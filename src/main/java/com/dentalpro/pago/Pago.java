package com.dentalpro.pago;

import com.dentalpro.atencion.Atencion;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atencion_id", nullable = false, unique = true)
    private Atencion atencion;

    @Column(nullable = false)
    private Double monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private MetodoPago metodoPago;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado;

    @PrePersist
    void prePersist() {
        if (fechaPago == null) fechaPago = LocalDate.now();
    }

    public enum MetodoPago { EFECTIVO, TARJETA, TRANSFERENCIA }
    public enum Estado { PENDIENTE, PAGADO, ANULADO }

    public Long getId() { return id; }
    public Atencion getAtencion() { return atencion; }
    public void setAtencion(Atencion v) { this.atencion = v; }
    public Double getMonto() { return monto; }
    public void setMonto(Double v) { this.monto = v; }
    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago v) { this.metodoPago = v; }
    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate v) { this.fechaPago = v; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado v) { this.estado = v; }
}
