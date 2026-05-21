package com.dentalpro.cita;

import com.dentalpro.cita.dto.CitaDto;
import com.dentalpro.paciente.PacienteRepository;
import com.dentalpro.usuario.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;

    public CitaService(CitaRepository citaRepository, PacienteRepository pacienteRepository, UsuarioRepository usuarioRepository) {
        this.citaRepository = citaRepository;
        this.pacienteRepository = pacienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<CitaDto> listarPorFecha(String fecha) {
        LocalDate localDate = fecha != null ? LocalDate.parse(fecha) : LocalDate.now();
        return citaRepository.findByFecha(localDate).stream().map(this::toDto).toList();
    }

    public List<CitaDto> listarPorPaciente(Long pacienteId) {
        return citaRepository.findByPacienteIdOrderByFechaDescHoraDesc(pacienteId).stream().map(this::toDto).toList();
    }

    public List<CitaDto> listarPorOdontologoYFecha(Long odontologoId, String fecha) {
        LocalDate localDate = fecha != null ? LocalDate.parse(fecha) : LocalDate.now();
        return citaRepository.findByOdontologoIdAndFechaOrderByHoraAsc(odontologoId, localDate)
                .stream().map(this::toDto).toList();
    }

    public boolean verificarDisponibilidad(Long odontologoId, String fecha, String hora) {
        return !citaRepository.existeConflicto(odontologoId, LocalDate.parse(fecha), hora);
    }

    public CitaDto findById(Long id) {
        return toDto(getOrThrow(id));
    }

    public CitaDto crear(CitaDto dto) {
        var paciente = pacienteRepository.findById(dto.pacienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado"));
        var odontologo = usuarioRepository.findById(dto.odontologoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Odontólogo no encontrado"));

        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setOdontologo(odontologo);
        cita.setFecha(dto.fecha());
        cita.setHora(dto.hora());
        cita.setMotivo(dto.motivo());
        cita.setEstado(Cita.Estado.PENDIENTE);
        return toDto(citaRepository.save(cita));
    }

    public CitaDto actualizar(Long id, CitaDto dto) {
        Cita cita = getOrThrow(id);
        var paciente = pacienteRepository.findById(dto.pacienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado"));
        var odontologo = usuarioRepository.findById(dto.odontologoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Odontólogo no encontrado"));

        cita.setPaciente(paciente);
        cita.setOdontologo(odontologo);
        cita.setFecha(dto.fecha());
        cita.setHora(dto.hora());
        cita.setMotivo(dto.motivo());
        cita.setEstado(Cita.Estado.REAGENDADO);
        return toDto(citaRepository.save(cita));
    }

    public CitaDto cambiarEstado(Long id, String estado) {
        Cita cita = getOrThrow(id);
        cita.setEstado(Cita.Estado.valueOf(estado));
        return toDto(citaRepository.save(cita));
    }

    public CitaDto cancelar(Long id) {
        return cambiarEstado(id, "CANCELADO");
    }

    public void finalizar(Long id) {
        cambiarEstado(id, "ATENDIDO");
    }

    private Cita getOrThrow(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada"));
    }

    private CitaDto toDto(Cita c) {
        return new CitaDto(
                c.getId(),
                c.getPaciente().getId(),
                c.getPaciente().getNombreCompleto(),
                c.getOdontologo().getId(),
                c.getOdontologo().getNombreCompleto(),
                c.getFecha(),
                c.getHora(),
                c.getMotivo(),
                c.getEstado().name()
        );
    }
}
