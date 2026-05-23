package com.dentalpro.atencion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AtencionRepository extends JpaRepository<Atencion, Long> {
    Optional<Atencion> findByCitaId(Long citaId);

    List<Atencion> findByCitaPacienteIdOrderByFechaDesc(Long pacienteId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Atencion a WHERE a.cita.paciente.id = :pacienteId")
    void deletePorPacienteId(@Param("pacienteId") Long pacienteId);
}
