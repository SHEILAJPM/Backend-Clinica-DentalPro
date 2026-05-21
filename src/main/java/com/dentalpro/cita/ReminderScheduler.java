package com.dentalpro.cita;

import com.dentalpro.config.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ReminderScheduler {

    private final CitaRepository citaRepository;
    private final EmailService emailService;

    private static final DateTimeFormatter FECHA_DISPLAY =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ReminderScheduler(CitaRepository citaRepository, EmailService emailService) {
        this.citaRepository = citaRepository;
        this.emailService = emailService;
    }

    /**
     * Corre cada 60 segundos. Busca citas de hoy cuyo horario esté entre
     * 28 y 35 minutos en el futuro y aún no hayan recibido recordatorio.
     * La ventana de 7 minutos garantiza que ninguna cita quede sin aviso
     * aunque el scheduler sufra un pequeño retraso.
     */
    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void enviarRecordatorios() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDate hoy = ahora.toLocalDate();

        List<Cita> candidatas = citaRepository.findPendientesDeRecordatorio(hoy);

        for (Cita cita : candidatas) {
            LocalTime horaCita;
            try {
                horaCita = LocalTime.parse(cita.getHora());
            } catch (Exception e) {
                continue;
            }

            LocalDateTime citaDateTime = LocalDateTime.of(hoy, horaCita);
            long minutosRestantes = java.time.Duration.between(ahora, citaDateTime).toMinutes();

            if (minutosRestantes >= 28 && minutosRestantes <= 35) {
                String doctorEmail  = cita.getOdontologo().getEmail();
                String doctorNombre = cita.getOdontologo().getNombreCompleto();
                String paciente     = cita.getPaciente().getNombreCompleto();
                String fecha        = cita.getFecha().format(FECHA_DISPLAY);
                String hora         = cita.getHora();
                String motivo       = cita.getMotivo();

                emailService.sendCitaReminder(doctorEmail, doctorNombre,
                                              paciente, fecha, hora, motivo);

                cita.setRecordatorioEnviado(true);
                citaRepository.save(cita);

                System.out.printf("[DentalPro Reminder] Recordatorio enviado a %s para cita con %s a las %s%n",
                        doctorEmail, paciente, hora);
            }
        }
    }
}
