package com.dentalpro.atencion;

import com.dentalpro.atencion.dto.AtencionDto;
import com.dentalpro.cita.CitaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    private AtencionDto toDto(Atencion a) {
        return new AtencionDto(a.getId(), a.getCita().getId(),
                a.getDiagnostico(), a.getTratamiento(), a.getObservaciones(), a.getFecha());
    }
}
