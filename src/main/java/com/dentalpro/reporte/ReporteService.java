package com.dentalpro.reporte;

import com.dentalpro.cita.CitaRepository;
import com.dentalpro.reporte.dto.ReporteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final CitaRepository citaRepository;

    public ReporteService(ReporteRepository reporteRepository, CitaRepository citaRepository) {
        this.reporteRepository = reporteRepository;
        this.citaRepository = citaRepository;
    }

    public Map<String, Object> listar(int page, int size) {
        Page<Reporte> result = reporteRepository.findAll(
                PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "fecha"))
        );
        return Map.of(
                "content", result.getContent().stream().map(this::toDto).toList(),
                "totalElements", result.getTotalElements(),
                "totalPages", result.getTotalPages(),
                "currentPage", page,
                "size", size
        );
    }

    public ReporteDto findByCita(Long citaId) {
        return reporteRepository.findByCitaId(citaId)
                .map(this::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reporte no encontrado"));
    }

    public ReporteDto generar(Long citaId) {
        var cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada"));

        Reporte r = new Reporte();
        r.setCita(cita);
        r.setPacienteNombre(cita.getPaciente().getNombreCompleto());
        r.setOdontologoNombre(cita.getOdontologo().getNombreCompleto());
        r.setDiagnostico("Diagnóstico generado");
        r.setTratamiento("Tratamiento realizado");
        r.setObservaciones("");
        return toDto(reporteRepository.save(r));
    }

    public byte[] generarPdf(Long id) {
        reporteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reporte no encontrado"));
        return "%PDF-1.4 mock pdf content".getBytes();
    }

    private ReporteDto toDto(Reporte r) {
        return new ReporteDto(r.getId(), r.getCita().getId(),
                r.getPacienteNombre(), r.getOdontologoNombre(),
                r.getDiagnostico(), r.getTratamiento(), r.getObservaciones(), r.getFecha());
    }
}
