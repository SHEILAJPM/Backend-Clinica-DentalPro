package com.dentalpro.config;

import com.dentalpro.paciente.Paciente;
import com.dentalpro.paciente.PacienteRepository;
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
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository,
                           PacienteRepository pacienteRepository,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.pacienteRepository = pacienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        crearUsuario("Administrador General", "admin@dental.com",      "Admin123!", Usuario.Rol.ADMINISTRADOR);
        crearUsuario("Recepcionista",         "recepcion@dental.com",  "Admin123!", Usuario.Rol.RECEPCIONISTA);
        crearUsuario("Dr. Carlos Mendoza",    "doctor@dental.com",     "Admin123!", Usuario.Rol.ODONTOLOGO);
        crearUsuario("Dra. Laura Quispe",     "doctora@dental.com",    "Admin123!", Usuario.Rol.ODONTOLOGO);

        crearPaciente("Ana Torres Ramírez",    "12345678", LocalDate.of(1990, 3, 15), "987654321", "ana.torres@gmail.com");
        crearPaciente("Luis García Peña",      "23456789", LocalDate.of(1985, 7, 22), "976543210", "luis.garcia@gmail.com");
        crearPaciente("María Flores Huanca",   "34567890", LocalDate.of(1998, 11, 5), "965432109", "maria.flores@gmail.com");
        crearPaciente("Jorge Castro López",    "45678901", LocalDate.of(1978, 1, 30), "954321098", "jorge.castro@gmail.com");
        crearPaciente("Rosa Mamani Ccopa",     "56789012", LocalDate.of(2000, 6, 18), "943210987", "rosa.mamani@gmail.com");
        crearPaciente("Carlos Ríos Villena",   "67890123", LocalDate.of(1992, 9, 10), "932109876", "carlos.rios@gmail.com");
        crearPaciente("Sofía Paredes Vega",    "78901234", LocalDate.of(1995, 4, 25), "921098765", "sofia.paredes@gmail.com");
        crearPaciente("Pedro Suárez Noriega",  "89012345", LocalDate.of(1980, 12, 3), "910987654", "pedro.suarez@gmail.com");
    }

    private void crearUsuario(String nombre, String email, String password, Usuario.Rol rol) {
        usuarioRepository.findByEmail(email).ifPresentOrElse(u -> {
            u.setActivo(true);
            u.setRol(rol);
            u.setPassword(passwordEncoder.encode(password));
            usuarioRepository.save(u);
            System.out.println("[DentalPro] Usuario actualizado: " + email);
        }, () -> {
            Usuario u = new Usuario();
            u.setNombreCompleto(nombre);
            u.setEmail(email);
            u.setPassword(passwordEncoder.encode(password));
            u.setRol(rol);
            u.setActivo(true);
            usuarioRepository.save(u);
            System.out.println("[DentalPro] Usuario creado: " + email + " | Contraseña: " + password);
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
}
