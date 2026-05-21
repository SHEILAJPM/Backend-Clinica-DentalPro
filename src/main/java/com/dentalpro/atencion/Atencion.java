package com.dentalpro.atencion;

import com.dentalpro.cita.Cita;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "atenciones")
public class Atencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_id", nullable = false)
    private Cita cita;

    @Column(columnDefinition = "TEXT")
    private String diagnostico;

    @Column(columnDefinition = "TEXT")
    private String tratamiento;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(nullable = false)
    private LocalDate fecha;

    @PrePersist
    void prePersist() {
        if (fecha == null) fecha = LocalDate.now();
    }

    public Long getId() { return id; }
    public Cita getCita() { return cita; }
    public void setCita(Cita v) { this.cita = v; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String v) { this.diagnostico = v; }
    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String v) { this.tratamiento = v; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String v) { this.observaciones = v; }
    public LocalDate getFecha() { return fecha; }
}
