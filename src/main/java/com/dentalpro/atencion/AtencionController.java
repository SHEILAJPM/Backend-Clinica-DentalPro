package com.dentalpro.atencion;

import com.dentalpro.atencion.dto.AtencionDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atenciones")
public class AtencionController {

    private final AtencionService atencionService;

    public AtencionController(AtencionService atencionService) {
        this.atencionService = atencionService;
    }

    @PostMapping
    ResponseEntity<AtencionDto> crear(@Valid @RequestBody AtencionDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(atencionService.crear(dto));
    }

    @GetMapping
    ResponseEntity<List<AtencionDto>> listarPorPaciente(@RequestParam Long pacienteId) {
        return ResponseEntity.ok(atencionService.listarPorPaciente(pacienteId));
    }

    @GetMapping("/cita/{citaId}")
    ResponseEntity<AtencionDto> obtenerPorCita(@PathVariable Long citaId) {
        AtencionDto dto = atencionService.obtenerPorCita(citaId);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }
}
