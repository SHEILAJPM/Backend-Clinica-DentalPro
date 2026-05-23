package com.dentalpro.historial;

import com.dentalpro.paciente.Paciente;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "historial_clinico")
public class HistorialClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    private Paciente paciente;

    @Column(columnDefinition = "TEXT")
    private String alergias;

    @Column(name = "condiciones_medicas", columnDefinition = "TEXT")
    private String condicionesMedicas;

    @Column(name = "medicamentos_actuales", columnDefinition = "TEXT")
    private String medicamentosActuales;

    @Column(name = "grupo_sanguineo", length = 5)
    private String grupoSanguineo;

    @Column(name = "fecha_actualizacion")
    private LocalDate fechaActualizacion;

    @PrePersist
    @PreUpdate
    void actualizar() {
        fechaActualizacion = LocalDate.now();
    }

    public Long getId() { return id; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente v) { this.paciente = v; }
    public String getAlergias() { return alergias; }
    public void setAlergias(String v) { this.alergias = v; }
    public String getCondicionesMedicas() { return condicionesMedicas; }
    public void setCondicionesMedicas(String v) { this.condicionesMedicas = v; }
    public String getMedicamentosActuales() { return medicamentosActuales; }
    public void setMedicamentosActuales(String v) { this.medicamentosActuales = v; }
    public String getGrupoSanguineo() { return grupoSanguineo; }
    public void setGrupoSanguineo(String v) { this.grupoSanguineo = v; }
    public LocalDate getFechaActualizacion() { return fechaActualizacion; }
}
