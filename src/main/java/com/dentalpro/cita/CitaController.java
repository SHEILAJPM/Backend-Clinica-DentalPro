package com.dentalpro.cita;

import com.dentalpro.cita.dto.CitaDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping
    ResponseEntity<List<CitaDto>> listar(
            @RequestParam(required = false) String fecha,
            @RequestParam(required = false) Long pacienteId,
            @RequestParam(required = false) Long odontologoId) {
        if (pacienteId != null) {
            return ResponseEntity.ok(citaService.listarPorPaciente(pacienteId));
        }
        if (odontologoId != null) {
            return ResponseEntity.ok(citaService.listarPorOdontologoYFecha(odontologoId, fecha));
        }
        return ResponseEntity.ok(citaService.listarPorFecha(fecha));
    }

    @GetMapping("/disponibilidad")
    ResponseEntity<Boolean> disponibilidad(
            @RequestParam Long odontologoId,
            @RequestParam String fecha,
            @RequestParam String hora) {
        return ResponseEntity.ok(citaService.verificarDisponibilidad(odontologoId, fecha, hora));
    }

    @GetMapping("/{id}")
    ResponseEntity<CitaDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.findById(id));
    }

    @PostMapping
    ResponseEntity<CitaDto> crear(@Valid @RequestBody CitaDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citaService.crear(dto));
    }

    @PutMapping("/{id}")
    ResponseEntity<CitaDto> actualizar(@PathVariable Long id, @Valid @RequestBody CitaDto dto) {
        return ResponseEntity.ok(citaService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/cancelar")
    ResponseEntity<CitaDto> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.cancelar(id));
    }

    @PatchMapping("/{id}/estado")
    ResponseEntity<CitaDto> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(citaService.cambiarEstado(id, body.get("estado")));
    }

    @PutMapping("/{id}/finalizar")
    ResponseEntity<Void> finalizar(@PathVariable Long id) {
        citaService.finalizar(id);
        return ResponseEntity.noContent().build();
    }
}
