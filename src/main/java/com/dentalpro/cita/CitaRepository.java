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

    @Query("SELECT c.estado, COUNT(c) FROM Cita c WHERE c.fecha >= :inicio AND c.fecha <= :fin GROUP BY c.estado")
    List<Object[]> countByEstadoForPeriod(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    @Query("SELECT c.odontologo.nombreCompleto, COUNT(c) FROM Cita c WHERE c.fecha >= :inicio AND c.fecha <= :fin GROUP BY c.odontologo.id, c.odontologo.nombreCompleto ORDER BY COUNT(c) DESC")
    List<Object[]> countByOdontologoForPeriod(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    @Query("SELECT c.fecha, COUNT(c) FROM Cita c WHERE c.fecha >= :desde GROUP BY c.fecha ORDER BY c.fecha ASC")
    List<Object[]> countByFechaSince(@Param("desde") LocalDate desde);

    boolean existsByOdontologoId(Long odontologoId);

    java.util.Optional<Cita> findByPacienteIdAndFechaAndHora(Long pacienteId, java.time.LocalDate fecha, String hora);

    @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("DELETE FROM Cita c WHERE c.paciente.id = :pacienteId")
    void deletePorPacienteId(@org.springframework.data.repository.query.Param("pacienteId") Long pacienteId);
}
