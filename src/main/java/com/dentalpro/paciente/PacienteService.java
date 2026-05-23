package com.dentalpro.paciente;

import com.dentalpro.atencion.AtencionRepository;
import com.dentalpro.cita.CitaRepository;
import com.dentalpro.historial.HistorialClinicoRepository;
import com.dentalpro.pago.PagoRepository;
import com.dentalpro.paciente.dto.PacienteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final CitaRepository citaRepository;
    private final AtencionRepository atencionRepository;
    private final HistorialClinicoRepository historialRepository;
    private final PagoRepository pagoRepository;

    public PacienteService(PacienteRepository pacienteRepository,
                           CitaRepository citaRepository,
                           AtencionRepository atencionRepository,
                           HistorialClinicoRepository historialRepository,
                           PagoRepository pagoRepository) {
        this.pacienteRepository = pacienteRepository;
        this.citaRepository = citaRepository;
        this.atencionRepository = atencionRepository;
        this.historialRepository = historialRepository;
        this.pagoRepository = pagoRepository;
    }

    public Map<String, Object> listar(int page, int size) {
        Page<Paciente> pageResult = pacienteRepository.findAll(
                PageRequest.of(page - 1, size, Sort.by("nombreCompleto"))
        );
        return Map.of(
                "content", pageResult.getContent().stream().map(this::toDto).toList(),
                "totalElements", pageResult.getTotalElements(),
                "totalPages", pageResult.getTotalPages(),
                "currentPage", page,
                "size", size
        );
    }

    public List<PacienteDto> buscar(String q) {
        return pacienteRepository.buscar(q).stream().map(this::toDto).toList();
    }

    public PacienteDto findById(Long id) {
        return toDto(getOrThrow(id));
    }

    public PacienteDto crear(PacienteDto dto) {
        if (pacienteRepository.existsByDni(dto.dni())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "DNI ya registrado");
        }
        Paciente p = new Paciente();
        p.setNombreCompleto(dto.nombreCompleto());
        p.setDni(dto.dni());
        p.setFechaNacimiento(dto.fechaNacimiento());
        p.setTelefono(dto.telefono());
        p.setEmail(dto.email());
        return toDto(pacienteRepository.save(p));
    }

    public PacienteDto actualizar(Long id, PacienteDto dto) {
        Paciente p = getOrThrow(id);
        if (!p.getDni().equals(dto.dni()) && pacienteRepository.existsByDni(dto.dni())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "DNI ya registrado");
        }
        p.setNombreCompleto(dto.nombreCompleto());
        p.setDni(dto.dni());
        p.setFechaNacimiento(dto.fechaNacimiento());
        p.setTelefono(dto.telefono());
        p.setEmail(dto.email());
        return toDto(pacienteRepository.save(p));
    }

    @Transactional
    public void eliminar(Long id) {
        getOrThrow(id);
        pagoRepository.deletePorPacienteId(id);
        atencionRepository.deletePorPacienteId(id);
        citaRepository.deletePorPacienteId(id);
        historialRepository.deleteByPacienteId(id);
        pacienteRepository.deleteById(id);
    }

    private Paciente getOrThrow(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado"));
    }

    private PacienteDto toDto(Paciente p) {
        return new PacienteDto(p.getId(), p.getNombreCompleto(), p.getDni(),
                p.getFechaNacimiento(), p.getTelefono(), p.getEmail());
    }
}
