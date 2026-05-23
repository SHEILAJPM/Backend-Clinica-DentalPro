package com.dentalpro.atencion;

import com.dentalpro.atencion.dto.AtencionDto;
import com.dentalpro.cita.CitaRepository;
import com.dentalpro.pago.Pago;
import com.dentalpro.pago.PagoRepository;
import com.dentalpro.reporte.Reporte;
import com.dentalpro.reporte.ReporteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AtencionService {

    private final AtencionRepository atencionRepository;
    private final CitaRepository citaRepository;
    private final ReporteRepository reporteRepository;
    private final PagoRepository pagoRepository;

    public AtencionService(AtencionRepository atencionRepository,
                           CitaRepository citaRepository,
                           ReporteRepository reporteRepository,
                           PagoRepository pagoRepository) {
        this.atencionRepository = atencionRepository;
        this.citaRepository = citaRepository;
        this.reporteRepository = reporteRepository;
        this.pagoRepository = pagoRepository;
    }

    public AtencionDto crear(AtencionDto dto) {
        var cita = citaRepository.findById(dto.citaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cita no encontrada"));

        Atencion a = new Atencion();
        a.setCita(cita);
        a.setDiagnostico(dto.diagnostico());
        a.setTratamiento(dto.tratamiento());
        a.setObservaciones(dto.observaciones());
        Atencion saved = atencionRepository.save(a);

        if (reporteRepository.findByCitaId(cita.getId()).isEmpty()) {
            Reporte r = new Reporte();
            r.setCita(cita);
            r.setPacienteNombre(cita.getPaciente().getNombreCompleto());
            r.setOdontologoNombre(cita.getOdontologo().getNombreCompleto());
            r.setDiagnostico(dto.diagnostico());
            r.setTratamiento(dto.tratamiento());
            r.setObservaciones(dto.observaciones() != null ? dto.observaciones() : "");
            reporteRepository.save(r);
        }

        if (pagoRepository.findByAtencionId(saved.getId()).isEmpty()) {
            double monto = cita.getTratamiento() != null ? cita.getTratamiento().getPrecio() : 0.0;
            Pago p = new Pago();
            p.setAtencion(saved);
            p.setMonto(monto);
            p.setMetodoPago(Pago.MetodoPago.EFECTIVO);
            p.setEstado(Pago.Estado.PENDIENTE);
            pagoRepository.save(p);
        }

        return toDto(saved);
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
