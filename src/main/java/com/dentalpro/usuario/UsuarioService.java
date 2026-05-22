package com.dentalpro.usuario;

import com.dentalpro.cita.CitaRepository;
import com.dentalpro.usuario.dto.UsuarioDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CitaRepository citaRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, CitaRepository citaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.citaRepository = citaRepository;
    }

    public List<UsuarioDto> listar() {
        return usuarioRepository.findAll().stream().map(this::toDto).toList();
    }

    public List<UsuarioDto> listarOdontologos() {
        return usuarioRepository.findByRol(Usuario.Rol.ODONTOLOGO).stream().map(this::toDto).toList();
    }

    public UsuarioDto findById(Long id) {
        return toDto(getOrThrow(id));
    }

    public UsuarioDto crear(UsuarioDto dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ya registrado");
        }
        Usuario u = new Usuario();
        u.setNombreCompleto(dto.nombreCompleto());
        u.setEmail(dto.email());
        u.setPassword(passwordEncoder.encode(dto.password()));
        u.setRol(Usuario.Rol.valueOf(dto.rol()));
        u.setActivo(true);
        return toDto(usuarioRepository.save(u));
    }

    public UsuarioDto actualizar(Long id, UsuarioDto dto) {
        Usuario u = getOrThrow(id);
        if (!u.getEmail().equals(dto.email()) && usuarioRepository.existsByEmail(dto.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ya registrado");
        }
        u.setNombreCompleto(dto.nombreCompleto());
        u.setEmail(dto.email());
        u.setRol(Usuario.Rol.valueOf(dto.rol()));
        if (dto.activo() != null) u.setActivo(dto.activo());
        if (dto.password() != null && !dto.password().isBlank()) {
            u.setPassword(passwordEncoder.encode(dto.password()));
        }
        return toDto(usuarioRepository.save(u));
    }

    public void eliminar(Long id) {
        getOrThrow(id);
        if (citaRepository.existsByOdontologoId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede eliminar el usuario porque tiene citas registradas");
        }
        usuarioRepository.deleteById(id);
    }

    private Usuario getOrThrow(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    private UsuarioDto toDto(Usuario u) {
        return new UsuarioDto(u.getId(), u.getNombreCompleto(), u.getEmail(), null, u.getRol().name(), u.getActivo());
    }
}
