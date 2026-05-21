package com.dentalpro.cita;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByFecha(LocalDate fecha);

    List<Cita> findByPacienteIdOrderByFechaDescHoraDesc(Long pacienteId);

    List<Cita> findByOdontologoIdAndFechaOrderByHoraAsc(Long odontologoId, LocalDate fecha);

    @Query("SELECT COUNT(c) > 0 FROM Cita c WHERE c.odontologo.id = :odontologoId AND c.fecha = :fecha AND c.hora = :hora AND c.estado IN ('PENDIENTE','REAGENDADO')")
    boolean existeConflicto(@Param("odontologoId") Long odontologoId,
                            @Param("fecha") LocalDate fecha,
                            @Param("hora") String hora);

    @Query("SELECT c FROM Cita c WHERE c.fecha = :fecha AND c.recordatorioEnviado = false AND c.estado IN (com.dentalpro.cita.Cita.Estado.PENDIENTE, com.dentalpro.cita.Cita.Estado.REAGENDADO)")
    List<Cita> findPendientesDeRecordatorio(@Param("fecha") LocalDate fecha);
}
