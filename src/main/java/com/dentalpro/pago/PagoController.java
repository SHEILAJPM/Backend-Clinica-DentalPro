package com.dentalpro.pago;

import com.dentalpro.pago.dto.PagoDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping
    ResponseEntity<List<PagoDto>> listar() {
        return ResponseEntity.ok(pagoService.listar());
    }

    @GetMapping("/{id}")
    ResponseEntity<PagoDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.findById(id));
    }

    @GetMapping("/atencion/{atencionId}")
    ResponseEntity<PagoDto> findByAtencion(@PathVariable Long atencionId) {
        PagoDto dto = pagoService.obtenerPorAtencion(atencionId);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    ResponseEntity<PagoDto> crear(@Valid @RequestBody PagoDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.crear(dto));
    }

    @PatchMapping("/{id}/estado")
    ResponseEntity<PagoDto> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(pagoService.cambiarEstado(id, body.get("estado")));
    }
}
