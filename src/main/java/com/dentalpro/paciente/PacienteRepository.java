package com.dentalpro.paciente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByDni(String dni);
    Optional<Paciente> findByEmail(String email);
    boolean existsByDni(String dni);
    boolean existsByEmail(String email);

    @Query("SELECT p FROM Paciente p WHERE LOWER(p.nombreCompleto) LIKE LOWER(CONCAT('%',:q,'%')) OR p.dni LIKE CONCAT('%',:q,'%')")
    List<Paciente> buscar(@Param("q") String q);

    Page<Paciente> findAll(Pageable pageable);
}
