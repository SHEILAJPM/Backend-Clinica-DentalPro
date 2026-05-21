package com.dentalpro.config;

import com.dentalpro.usuario.Usuario;
import com.dentalpro.usuario.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        crearUsuario("Administrador General", "admin@dental.com",      "Admin123!", Usuario.Rol.ADMINISTRADOR);
        crearUsuario("Recepcionista",         "recepcion@dental.com",  "Admin123!", Usuario.Rol.RECEPCIONISTA);
        crearUsuario("Dr. Odontólogo",        "doctor@dental.com",     "Admin123!", Usuario.Rol.ODONTOLOGO);
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
}
