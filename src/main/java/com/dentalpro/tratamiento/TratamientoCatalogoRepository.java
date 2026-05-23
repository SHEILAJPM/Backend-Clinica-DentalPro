package com.dentalpro.tratamiento;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TratamientoCatalogoRepository extends JpaRepository<TratamientoCatalogo, Long> {
    Optional<TratamientoCatalogo> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}
