package com.dentalpro.paciente;

import com.dentalpro.paciente.dto.PacienteDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    ResponseEntity<Map<String, Object>> listar(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(pacienteService.listar(page, size));
    }

    @GetMapping("/buscar")
    ResponseEntity<List<PacienteDto>> buscar(@RequestParam String q) {
        return ResponseEntity.ok(pacienteService.buscar(q));
    }

    @GetMapping("/{id}")
    ResponseEntity<PacienteDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.findById(id));
    }

    @PostMapping
    ResponseEntity<PacienteDto> crear(@Valid @RequestBody PacienteDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.crear(dto));
    }

    @PutMapping("/{id}")
    ResponseEntity<PacienteDto> actualizar(@PathVariable Long id, @Valid @RequestBody PacienteDto dto) {
        return ResponseEntity.ok(pacienteService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pacienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
