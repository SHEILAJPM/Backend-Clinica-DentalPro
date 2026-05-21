package com.dentalpro.auth;

import com.dentalpro.auth.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class PacienteAuthController {

    private final PacienteAuthService pacienteAuthService;

    public PacienteAuthController(PacienteAuthService pacienteAuthService) {
        this.pacienteAuthService = pacienteAuthService;
    }

    @PostMapping("/login-paciente")
    ResponseEntity<LoginResponse> loginPaciente(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(pacienteAuthService.login(req));
    }

    @PostMapping("/registro-paciente")
    ResponseEntity<RegistroPacienteResponse> registrar(@Valid @RequestBody RegistroPacienteRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteAuthService.registrar(req));
    }

    @PostMapping("/forgot-password")
    ResponseEntity<ForgotPasswordResponse> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(pacienteAuthService.forgotPassword(email));
    }

    @PostMapping("/verify-code")
    ResponseEntity<Map<String, String>> verifyCode(@Valid @RequestBody VerifyCodeRequest req) {
        pacienteAuthService.verifyCode(req.email(), req.code());
        return ResponseEntity.ok(Map.of("mensaje", "Código verificado correctamente."));
    }

    @PostMapping("/reset-password")
    ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest req) {
        pacienteAuthService.resetPassword(req);
        return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada correctamente."));
    }
}
