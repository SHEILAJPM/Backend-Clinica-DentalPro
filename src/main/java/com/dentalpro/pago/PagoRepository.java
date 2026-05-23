package com.dentalpro.pago;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    Optional<Pago> findByAtencionId(Long atencionId);
    List<Pago> findAllByOrderByFechaPagoDesc();

    @Transactional
    @Modifying
    @Query("DELETE FROM Pago p WHERE p.atencion.cita.paciente.id = :pacienteId")
    void deletePorPacienteId(@Param("pacienteId") Long pacienteId);
}
