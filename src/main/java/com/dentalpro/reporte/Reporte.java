package com.dentalpro.reporte;

import com.dentalpro.cita.Cita;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reportes")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_id", nullable = false)
    private Cita cita;

    @Column(name = "paciente_nombre")
    private String pacienteNombre;

    @Column(name = "odontologo_nombre")
    private String odontologoNombre;

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
    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String v) { this.pacienteNombre = v; }
    public String getOdontologoNombre() { return odontologoNombre; }
    public void setOdontologoNombre(String v) { this.odontologoNombre = v; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String v) { this.diagnostico = v; }
    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String v) { this.tratamiento = v; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String v) { this.observaciones = v; }
    public LocalDate getFecha() { return fecha; }
}
