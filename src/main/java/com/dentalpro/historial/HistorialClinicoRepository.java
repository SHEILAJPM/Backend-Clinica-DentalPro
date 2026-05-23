package com.dentalpro.historial;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HistorialClinicoRepository extends JpaRepository<HistorialClinico, Long> {
    Optional<HistorialClinico> findByPacienteId(Long pacienteId);
    void deleteByPacienteId(Long pacienteId);
}
