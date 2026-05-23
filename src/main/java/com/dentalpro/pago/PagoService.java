package com.dentalpro.pago;

import com.dentalpro.atencion.AtencionRepository;
import com.dentalpro.pago.dto.PagoDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final AtencionRepository atencionRepository;

    public PagoService(PagoRepository pagoRepository, AtencionRepository atencionRepository) {
        this.pagoRepository = pagoRepository;
        this.atencionRepository = atencionRepository;
    }

    public List<PagoDto> listar() {
        return pagoRepository.findAllByOrderByFechaPagoDesc().stream().map(this::toDto).toList();
    }

    public PagoDto findById(Long id) {
        return toDto(getOrThrow(id));
    }

    public PagoDto obtenerPorAtencion(Long atencionId) {
        return pagoRepository.findByAtencionId(atencionId).map(this::toDto).orElse(null);
    }

    public PagoDto crear(PagoDto dto) {
        var atencion = atencionRepository.findById(dto.atencionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Atención no encontrada"));

        if (pagoRepository.findByAtencionId(dto.atencionId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta atención ya tiene un pago registrado");
        }

        Pago p = new Pago();
        p.setAtencion(atencion);
        p.setMonto(dto.monto());
        p.setMetodoPago(Pago.MetodoPago.valueOf(dto.metodoPago()));
        p.setEstado(Pago.Estado.PENDIENTE);
        return toDto(pagoRepository.save(p));
    }

    public PagoDto cobrar(Long id, Double monto, String metodoPago) {
        Pago p = getOrThrow(id);
        if (p.getEstado() != Pago.Estado.PENDIENTE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se pueden cobrar pagos en estado PENDIENTE");
        }
        p.setMonto(monto);
        p.setMetodoPago(Pago.MetodoPago.valueOf(metodoPago));
        p.setEstado(Pago.Estado.PAGADO);
        return toDto(pagoRepository.save(p));
    }

    public PagoDto cambiarEstado(Long id, String estado) {
        Pago p = getOrThrow(id);
        p.setEstado(Pago.Estado.valueOf(estado));
        return toDto(pagoRepository.save(p));
    }

    private Pago getOrThrow(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pago no encontrado"));
    }

    private PagoDto toDto(Pago p) {
        var cita = p.getAtencion().getCita();
        return new PagoDto(
                p.getId(),
                p.getAtencion().getId(),
                cita.getPaciente().getId(),
                cita.getPaciente().getNombreCompleto(),
                cita.getOdontologo().getNombreCompleto(),
                cita.getFecha(),
                p.getMonto(),
                p.getMetodoPago().name(),
                p.getFechaPago(),
                p.getEstado().name()
        );
    }
}
