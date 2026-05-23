package com.dentalpro.historial;

import com.dentalpro.historial.dto.HistorialClinicoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/historial")
public class HistorialClinicoController {

    private final HistorialClinicoService historialService;

    public HistorialClinicoController(HistorialClinicoService historialService) {
        this.historialService = historialService;
    }

    @GetMapping("/paciente/{pacienteId}")
    ResponseEntity<HistorialClinicoDto> obtener(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(historialService.obtenerPorPaciente(pacienteId));
    }

    @PutMapping("/paciente/{pacienteId}")
    ResponseEntity<HistorialClinicoDto> guardar(@PathVariable Long pacienteId,
                                                @RequestBody HistorialClinicoDto dto) {
        return ResponseEntity.ok(historialService.guardar(pacienteId, dto));
    }
}
