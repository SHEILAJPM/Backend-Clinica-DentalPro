package com.dentalpro.stats;

import com.dentalpro.cita.CitaRepository;
import com.dentalpro.paciente.PacienteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class StatsService {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;

    public StatsService(CitaRepository citaRepository, PacienteRepository pacienteRepository) {
        this.citaRepository = citaRepository;
        this.pacienteRepository = pacienteRepository;
    }

    public Map<String, Object> getAdminStats() {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);
        LocalDate finMes = hoy.withDayOfMonth(hoy.lengthOfMonth());
        LocalDate hace7Dias = hoy.minusDays(6);

        // --- Citas por estado (mes actual) ---
        Map<String, Long> porEstado = new LinkedHashMap<>();
        porEstado.put("PENDIENTE", 0L);
        porEstado.put("ATENDIDO", 0L);
        porEstado.put("CANCELADO", 0L);
        porEstado.put("REAGENDADO", 0L);
        for (Object[] row : citaRepository.countByEstadoForPeriod(inicioMes, finMes)) {
            porEstado.put(row[0].toString(), (Long) row[1]);
        }

        // --- Citas por odontólogo (mes actual) ---
        List<Map<String, Object>> porOdontologo = new ArrayList<>();
        for (Object[] row : citaRepository.countByOdontologoForPeriod(inicioMes, finMes)) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("nombre", row[0]);
            item.put("total", row[1]);
            porOdontologo.add(item);
        }

        // --- Citas por día (últimos 7 días) ---
        List<Map<String, Object>> ultimos7Dias = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate fecha = hoy.minusDays(i);
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("fecha", fecha.toString());
            item.put("dia", fecha.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("es")));
            item.put("total", 0L);
            ultimos7Dias.add(item);
        }
        for (Object[] row : citaRepository.countByFechaSince(hace7Dias)) {
            String fechaStr = row[0].toString();
            Long count = (Long) row[1];
            ultimos7Dias.stream()
                    .filter(d -> d.get("fecha").equals(fechaStr))
                    .findFirst()
                    .ifPresent(d -> d.put("total", count));
        }

        // --- Totales generales ---
        long totalPacientes = pacienteRepository.count();
        long totalCitasMes = porEstado.values().stream().mapToLong(Long::longValue).sum();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("porEstado", porEstado);
        result.put("porOdontologo", porOdontologo);
        result.put("ultimos7Dias", ultimos7Dias);
        result.put("totalPacientes", totalPacientes);
        result.put("totalCitasMes", totalCitasMes);
        result.put("mes", hoy.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es")));
        return result;
    }
}
