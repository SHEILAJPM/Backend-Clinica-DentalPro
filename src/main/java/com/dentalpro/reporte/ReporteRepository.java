package com.dentalpro.reporte;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    Optional<Reporte> findByCitaId(Long citaId);
    Page<Reporte> findAll(Pageable pageable);
}
