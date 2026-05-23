package com.dentalpro.tratamiento;

import com.dentalpro.tratamiento.dto.TratamientoCatalogoDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tratamientos")
public class TratamientoCatalogoController {

    private final TratamientoCatalogoService service;

    public TratamientoCatalogoController(TratamientoCatalogoService service) {
        this.service = service;
    }

    @GetMapping
    ResponseEntity<List<TratamientoCatalogoDto>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    ResponseEntity<TratamientoCatalogoDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    ResponseEntity<TratamientoCatalogoDto> crear(@Valid @RequestBody TratamientoCatalogoDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    ResponseEntity<TratamientoCatalogoDto> actualizar(@PathVariable Long id,
                                                      @Valid @RequestBody TratamientoCatalogoDto dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
