package com.dentalpro.config;

import com.dentalpro.atencion.Atencion;
import com.dentalpro.atencion.AtencionRepository;
import com.dentalpro.cita.Cita;
import com.dentalpro.cita.CitaRepository;
import com.dentalpro.historial.HistorialClinico;
import com.dentalpro.historial.HistorialClinicoRepository;
import com.dentalpro.paciente.Paciente;
import com.dentalpro.paciente.PacienteRepository;
import com.dentalpro.pago.Pago;
import com.dentalpro.pago.PagoRepository;
import com.dentalpro.reporte.Reporte;
import com.dentalpro.reporte.ReporteRepository;
import com.dentalpro.tratamiento.TratamientoCatalogo;
import com.dentalpro.tratamiento.TratamientoCatalogoRepository;
import com.dentalpro.usuario.Usuario;
import com.dentalpro.usuario.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final HistorialClinicoRepository historialRepository;
    private final TratamientoCatalogoRepository tratamientoRepository;
    private final CitaRepository citaRepository;
    private final AtencionRepository atencionRepository;
    private final ReporteRepository reporteRepository;
    private final PagoRepository pagoRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository,
                           PacienteRepository pacienteRepository,
                           HistorialClinicoRepository historialRepository,
                           TratamientoCatalogoRepository tratamientoRepository,
                           CitaRepository citaRepository,
                           AtencionRepository atencionRepository,
                           ReporteRepository reporteRepository,
                           PagoRepository pagoRepository,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.pacienteRepository = pacienteRepository;
        this.historialRepository = historialRepository;
        this.tratamientoRepository = tratamientoRepository;
        this.citaRepository = citaRepository;
        this.atencionRepository = atencionRepository;
        this.reporteRepository = reporteRepository;
        this.pagoRepository = pagoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Usuarios
        crearUsuario("Administrador General", "admin@dental.com",     "123456", Usuario.Rol.ADMINISTRADOR);
        crearUsuario("Recepcionista",         "recepcion@dental.com", "123456", Usuario.Rol.RECEPCIONISTA);
        crearUsuario("Dr. Carlos Mendoza",    "doctor@dental.com",    "123456", Usuario.Rol.ODONTOLOGO);
        crearUsuario("Dra. Laura Quispe",     "doctora@dental.com",   "123456", Usuario.Rol.ODONTOLOGO);

        // Pacientes
        crearPaciente("Ana Torres Ramírez",   "12345678", LocalDate.of(1990, 3, 15),  "987654321", "ana.torres@gmail.com");
        crearPaciente("Luis García Peña",     "23456789", LocalDate.of(1985, 7, 22),  "976543210", "luis.garcia@gmail.com");
        crearPaciente("María Flores Huanca",  "34567890", LocalDate.of(1998, 11, 5),  "965432109", "maria.flores@gmail.com");
        crearPaciente("Jorge Castro López",   "45678901", LocalDate.of(1978, 1, 30),  "954321098", "jorge.castro@gmail.com");
        crearPaciente("Rosa Mamani Ccopa",    "56789012", LocalDate.of(2000, 6, 18),  "943210987", "rosa.mamani@gmail.com");
        crearPaciente("Carlos Ríos Villena",  "67890123", LocalDate.of(1992, 9, 10),  "932109876", "carlos.rios@gmail.com");
        crearPaciente("Sofía Paredes Vega",   "78901234", LocalDate.of(1995, 4, 25),  "921098765", "sofia.paredes@gmail.com");
        crearPaciente("Pedro Suárez Noriega", "89012345", LocalDate.of(1980, 12, 3),  "910987654", "pedro.suarez@gmail.com");

        // Historiales clínicos
        crearHistorial("12345678", "Penicilina",   "Ninguna",         "Ninguno",    "B+");
        crearHistorial("23456789", "Ninguna",       "Hipertensión",    "Enalapril",  "O+");
        crearHistorial("34567890", "Ibuprofeno",    "Diabetes tipo 2", "Metformina", "A+");
        crearHistorial("45678901", "Ninguna",       "Ninguna",         "Ninguno",    "AB+");
        crearHistorial("56789012", "Látex",         "Asma",            "Salbutamol", "O+");
        crearHistorial("67890123", "Ninguna",       "Ansiedad",        "Ninguno",    "A-");
        crearHistorial("78901234", "Sulfas",        "Ninguna",         "Ninguno",    "B-");
        crearHistorial("89012345", "Ninguna",       "Ninguna",         "Ninguno",    "O-");

        // Catálogo de tratamientos
        crearTratamiento("Limpieza dental",        "Eliminación de sarro y placa bacteriana",                  80.0,   45);
        crearTratamiento("Extracción dental",       "Extracción de piezas dentales dañadas o impactadas",      150.0,  30);
        crearTratamiento("Blanqueamiento dental",   "Aclaramiento profesional con luz LED",                    350.0,  90);
        crearTratamiento("Ortodoncia mensualidad", "Control y ajuste mensual de aparato ortodóncico",          200.0,  60);
        crearTratamiento("Endodoncia",             "Tratamiento de conducto radicular",                        400.0, 120);
        crearTratamiento("Obturación dental",      "Relleno de caries con resina compuesta",                   120.0,  45);
        crearTratamiento("Implante dental",        "Colocación de implante de titanio",                       1500.0, 180);
        crearTratamiento("Profilaxis dental",      "Limpieza preventiva y pulido coronario",                    60.0,  30);

        // Citas + Atenciones + Reportes + Pagos
        seedCompleto("12345678", "doctor@dental.com",   LocalDate.of(2026, 5, 1),  "09:00", "Limpieza de rutina",
                "Acumulación de sarro en zona interproximal",    "Limpieza dental profesional",        "Próxima revisión en 6 meses",  80.0,  "EFECTIVO",     "PAGADO");
        seedCompleto("23456789", "doctora@dental.com",  LocalDate.of(2026, 5, 2),  "10:00", "Dolor de muela",
                "Caries profunda en molar inferior derecho",     "Obturación dental con resina",       "Evitar alimentos duros 24h",    120.0, "TARJETA",      "PAGADO");
        seedCompleto("34567890", "doctor@dental.com",   LocalDate.of(2026, 5, 5),  "11:00", "Revisión ortodoncia",
                "Maloclusión clase II, tratamiento en curso",    "Ajuste de brackets y arco",          "Control mensual programado",    200.0, "TRANSFERENCIA","PAGADO");
        seedCompleto("45678901", "doctora@dental.com",  LocalDate.of(2026, 5, 7),  "09:30", "Extracción molar",
                "Molar inferior izquierdo con fractura vertical", "Extracción dental simple",          "Reposo 48h, no enjuagues",      150.0, "EFECTIVO",     "PAGADO");
        seedCompleto("56789012", "doctor@dental.com",   LocalDate.of(2026, 5, 9),  "14:00", "Blanqueamiento",
                "Tinción dental por consumo de café y tabaco",   "Blanqueamiento con luz LED",         "Evitar alimentos con colorantes", 350.0, "TARJETA",     "PAGADO");
        seedCompleto("67890123", "doctora@dental.com",  LocalDate.of(2026, 5, 12), "15:00", "Obturación molar",
                "Caries secundaria en obturación previa",        "Remoción y nueva obturación resina", null,                           120.0, "EFECTIVO",     "PENDIENTE");
        seedCompleto("78901234", "doctor@dental.com",   LocalDate.of(2026, 5, 14), "09:00", "Profilaxis",
                "Higiene oral deficiente, gingivitis leve",      "Profilaxis dental y enseñanza",      "Cepillado 3 veces al día",      60.0,  "EFECTIVO",     "PENDIENTE");
        seedCompleto("89012345", "doctora@dental.com",  LocalDate.of(2026, 5, 16), "10:00", "Tratamiento conducto",
                "Necrosis pulpar en incisivo central superior",  "Endodoncia unirradicular",           "Control radiográfico en 3 meses", 400.0, "TARJETA",    "PENDIENTE");
    }

    // ── helpers ────────────────────────────────────────────────────────────────

    private void crearUsuario(String nombre, String email, String password, Usuario.Rol rol) {
        usuarioRepository.findByEmail(email).ifPresentOrElse(u -> {
            u.setActivo(true);
            u.setRol(rol);
            u.setPassword(passwordEncoder.encode(password));
            usuarioRepository.save(u);
        }, () -> {
            Usuario u = new Usuario();
            u.setNombreCompleto(nombre);
            u.setEmail(email);
            u.setPassword(passwordEncoder.encode(password));
            u.setRol(rol);
            u.setActivo(true);
            usuarioRepository.save(u);
            System.out.println("[DentalPro] Usuario creado: " + email);
        });
    }

    private void crearPaciente(String nombre, String dni, LocalDate fechaNac, String telefono, String email) {
        if (!pacienteRepository.existsByDni(dni)) {
            Paciente p = new Paciente();
            p.setNombreCompleto(nombre);
            p.setDni(dni);
            p.setFechaNacimiento(fechaNac);
            p.setTelefono(telefono);
            p.setEmail(email);
            pacienteRepository.save(p);
            System.out.println("[DentalPro] Paciente creado: " + nombre);
        }
    }

    private void crearHistorial(String dni, String alergias, String condiciones,
                                String medicamentos, String grupoSanguineo) {
        pacienteRepository.findByDni(dni).ifPresent(paciente -> {
            if (historialRepository.findByPacienteId(paciente.getId()).isEmpty()) {
                HistorialClinico h = new HistorialClinico();
                h.setPaciente(paciente);
                h.setAlergias(alergias);
                h.setCondicionesMedicas(condiciones);
                h.setMedicamentosActuales(medicamentos);
                h.setGrupoSanguineo(grupoSanguineo);
                historialRepository.save(h);
            }
        });
    }

    private void crearTratamiento(String nombre, String descripcion, double precio, int duracion) {
        if (!tratamientoRepository.existsByNombre(nombre)) {
            TratamientoCatalogo t = new TratamientoCatalogo();
            t.setNombre(nombre);
            t.setDescripcion(descripcion);
            t.setPrecio(precio);
            t.setDuracionMinutos(duracion);
            tratamientoRepository.save(t);
            System.out.println("[DentalPro] Tratamiento creado: " + nombre);
        }
    }

    private void seedCompleto(String pacienteDni, String doctorEmail,
                              LocalDate fecha, String hora, String motivo,
                              String diagnostico, String tratamiento, String obs,
                              double monto, String metodoPago, String estadoPago) {

        var pacienteOpt = pacienteRepository.findByDni(pacienteDni);
        var doctorOpt   = usuarioRepository.findByEmail(doctorEmail);
        if (pacienteOpt.isEmpty() || doctorOpt.isEmpty()) return;

        Paciente paciente = pacienteOpt.get();
        Usuario  doctor   = doctorOpt.get();

        // 1. Cita
        Cita cita = citaRepository
                .findByPacienteIdAndFechaAndHora(paciente.getId(), fecha, hora)
                .orElseGet(() -> {
                    Cita c = new Cita();
                    c.setPaciente(paciente);
                    c.setOdontologo(doctor);
                    c.setFecha(fecha);
                    c.setHora(hora);
                    c.setMotivo(motivo);
                    c.setEstado(Cita.Estado.ATENDIDO);
                    c.setRecordatorioEnviado(true);
                    return citaRepository.save(c);
                });

        // 2. Atencion
        Atencion atencion = atencionRepository.findByCitaId(cita.getId())
                .orElseGet(() -> {
                    Atencion a = new Atencion();
                    a.setCita(cita);
                    a.setDiagnostico(diagnostico);
                    a.setTratamiento(tratamiento);
                    a.setObservaciones(obs);
                    Atencion saved = atencionRepository.save(a);

                    if (reporteRepository.findByCitaId(cita.getId()).isEmpty()) {
                        Reporte r = new Reporte();
                        r.setCita(cita);
                        r.setPacienteNombre(paciente.getNombreCompleto());
                        r.setOdontologoNombre(doctor.getNombreCompleto());
                        r.setDiagnostico(diagnostico);
                        r.setTratamiento(tratamiento);
                        r.setObservaciones(obs != null ? obs : "");
                        reporteRepository.save(r);
                    }
                    return saved;
                });

        // 3. Pago
        if (pagoRepository.findByAtencionId(atencion.getId()).isEmpty()) {
            Pago p = new Pago();
            p.setAtencion(atencion);
            p.setMonto(monto);
            p.setMetodoPago(Pago.MetodoPago.valueOf(metodoPago));
            p.setEstado(Pago.Estado.valueOf(estadoPago));
            p.setFechaPago(fecha.plusDays(1));
            pagoRepository.save(p);
            System.out.println("[DentalPro] Cita/Atencion/Pago creados para: " + paciente.getNombreCompleto());
        }
    }
}
