package com.dentalpro.reporte;

import com.dentalpro.reporte.dto.ReporteDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping
    ResponseEntity<Map<String, Object>> listar(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String odontologoNombre) {
        return ResponseEntity.ok(reporteService.listar(page, size, odontologoNombre));
    }

    @GetMapping("/{id}")
    ResponseEntity<ReporteDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.findById(id));
    }

    @GetMapping("/cita/{citaId}")
    ResponseEntity<ReporteDto> findByCita(@PathVariable Long citaId) {
        return ResponseEntity.ok(reporteService.findByCita(citaId));
    }

    @PostMapping("/generar/{citaId}")
    ResponseEntity<ReporteDto> generar(@PathVariable Long citaId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reporteService.generar(citaId));
    }

    @GetMapping("/{id}/pdf")
    ResponseEntity<byte[]> pdf(@PathVariable Long id) {
        byte[] pdf = reporteService.generarPdf(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
