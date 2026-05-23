package com.dentalpro.historial;

import com.dentalpro.historial.dto.HistorialClinicoDto;
import com.dentalpro.paciente.PacienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class HistorialClinicoService {

    private final HistorialClinicoRepository historialRepository;
    private final PacienteRepository pacienteRepository;

    public HistorialClinicoService(HistorialClinicoRepository historialRepository,
                                   PacienteRepository pacienteRepository) {
        this.historialRepository = historialRepository;
        this.pacienteRepository = pacienteRepository;
    }

    public HistorialClinicoDto obtenerPorPaciente(Long pacienteId) {
        return historialRepository.findByPacienteId(pacienteId)
                .map(this::toDto)
                .orElse(new HistorialClinicoDto(null, pacienteId, null, null, null, null, null));
    }

    public HistorialClinicoDto guardar(Long pacienteId, HistorialClinicoDto dto) {
        var paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado"));

        HistorialClinico historial = historialRepository.findByPacienteId(pacienteId)
                .orElse(new HistorialClinico());

        historial.setPaciente(paciente);
        historial.setAlergias(dto.alergias());
        historial.setCondicionesMedicas(dto.condicionesMedicas());
        historial.setMedicamentosActuales(dto.medicamentosActuales());
        historial.setGrupoSanguineo(dto.grupoSanguineo());

        return toDto(historialRepository.save(historial));
    }

    private HistorialClinicoDto toDto(HistorialClinico h) {
        return new HistorialClinicoDto(
                h.getId(),
                h.getPaciente().getId(),
                h.getAlergias(),
                h.getCondicionesMedicas(),
                h.getMedicamentosActuales(),
                h.getGrupoSanguineo(),
                h.getFechaActualizacion()
        );
    }
}
