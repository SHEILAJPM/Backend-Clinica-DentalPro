package com.dentalpro.atencion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AtencionRepository extends JpaRepository<Atencion, Long> {
    Optional<Atencion> findByCitaId(Long citaId);

    List<Atencion> findByCitaPacienteIdOrderByFechaDesc(Long pacienteId);
}
