package com.dentalpro.auth;

import com.dentalpro.auth.dto.*;
import com.dentalpro.config.EmailService;
import com.dentalpro.config.JwtUtil;
import com.dentalpro.paciente.Paciente;
import com.dentalpro.paciente.PacienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PacienteAuthService {

    private final PacienteRepository pacienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    public PacienteAuthService(PacienteRepository pacienteRepository,
                               PasswordEncoder passwordEncoder,
                               EmailService emailService,
                               JwtUtil jwtUtil) {
        this.pacienteRepository = pacienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest req) {
        Paciente p = pacienteRepository.findByEmail(req.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        if (p.getPasswordHash() == null || !passwordEncoder.matches(req.password(), p.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(p.getEmail(), "PACIENTE", p.getId());

        return new LoginResponse(
                token,
                new LoginResponse.UserInfo(
                        p.getId(),
                        p.getNombreCompleto(),
                        p.getEmail(),
                        "PACIENTE"
                )
        );
    }

    public RegistroPacienteResponse registrar(RegistroPacienteRequest req) {
        if (pacienteRepository.existsByDni(req.dni())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "DNI ya registrado");
        }
        if (pacienteRepository.existsByEmail(req.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ya registrado");
        }

        Paciente p = new Paciente();
        p.setNombreCompleto(req.nombreCompleto());
        p.setDni(req.dni());
        p.setFechaNacimiento(req.fechaNacimiento());
        p.setTelefono(req.telefono());
        p.setEmail(req.email());
        p.setPasswordHash(passwordEncoder.encode(req.password()));
        pacienteRepository.save(p);

        emailService.sendWelcomeEmail(req.email(), req.nombreCompleto());

        return new RegistroPacienteResponse("Registro exitoso. Te enviamos un correo de bienvenida.");
    }

    public ForgotPasswordResponse forgotPassword(String email) {
        Paciente p = pacienteRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe una cuenta con ese correo"));

        String code = String.format("%06d", new Random().nextInt(999999));
        p.setResetCode(code);
        p.setResetCodeExpiry(LocalDateTime.now().plusMinutes(10));
        pacienteRepository.save(p);

        emailService.sendResetCode(email, p.getNombreCompleto(), code);

        // En dev-mode incluimos el código en la respuesta para pruebas sin SMTP
        String devCode = emailService.isDevMode() ? code : null;
        return new ForgotPasswordResponse("Código enviado a tu correo electrónico.", devCode);
    }

    public void verifyCode(String email, String code) {
        Paciente p = pacienteRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email no encontrado"));

        if (p.getResetCode() == null || !p.getResetCode().equals(code)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código inválido");
        }
        if (p.getResetCodeExpiry() == null || LocalDateTime.now().isAfter(p.getResetCodeExpiry())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El código ha expirado");
        }
    }

    public void resetPassword(ResetPasswordRequest req) {
        Paciente p = pacienteRepository.findByEmail(req.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email no encontrado"));

        if (p.getResetCode() == null || !p.getResetCode().equals(req.code())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código inválido");
        }
        if (LocalDateTime.now().isAfter(p.getResetCodeExpiry())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El código ha expirado");
        }

        p.setPasswordHash(passwordEncoder.encode(req.newPassword()));
        p.setResetCode(null);
        p.setResetCodeExpiry(null);
        pacienteRepository.save(p);
    }
}
