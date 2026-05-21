package com.dentalpro.atencion;

import com.dentalpro.atencion.dto.AtencionDto;
import com.dentalpro.cita.CitaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AtencionService {

    private final AtencionRepository atencionRepository;
    private final CitaRepository citaRepository;

    public AtencionService(AtencionRepository atencionRepository, CitaRepository citaRepository) {
        this.atencionRepository = atencionRepository;
        this.citaRepository = citaRepository;
    }

    public AtencionDto crear(AtencionDto dto) {
        var cita = citaRepository.findById(dto.citaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada"));

        Atencion a = new Atencion();
        a.setCita(cita);
        a.setDiagnostico(dto.diagnostico());
        a.setTratamiento(dto.tratamiento());
        a.setObservaciones(dto.observaciones());
        return toDto(atencionRepository.save(a));
    }

    public List<AtencionDto> listarPorPaciente(Long pacienteId) {
        return atencionRepository.findByCitaPacienteIdOrderByFechaDesc(pacienteId)
                .stream().map(this::toDto).toList();
    }

    public AtencionDto obtenerPorCita(Long citaId) {
        return atencionRepository.findByCitaId(citaId).map(this::toDto).orElse(null);
    }

    private AtencionDto toDto(Atencion a) {
        return new AtencionDto(
                a.getId(),
                a.getCita().getId(),
                a.getCita().getPaciente().getId(),
                a.getCita().getPaciente().getNombreCompleto(),
                a.getCita().getOdontologo().getNombreCompleto(),
                a.getDiagnostico(),
                a.getTratamiento(),
                a.getObservaciones(),
                a.getFecha()
        );
    }
}
