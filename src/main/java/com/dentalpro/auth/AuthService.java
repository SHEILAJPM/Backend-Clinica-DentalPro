package com.dentalpro.auth;

import com.dentalpro.auth.dto.LoginRequest;
import com.dentalpro.auth.dto.LoginResponse;
import com.dentalpro.config.JwtUtil;
import com.dentalpro.usuario.Usuario;
import com.dentalpro.usuario.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        if (!usuario.getActivo()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario inactivo");
        }

        String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol().name(), usuario.getId());

        return new LoginResponse(
                token,
                new LoginResponse.UserInfo(
                        usuario.getId(),
                        usuario.getNombreCompleto(),
                        usuario.getEmail(),
                        usuario.getRol().name()
                )
        );
    }
}
