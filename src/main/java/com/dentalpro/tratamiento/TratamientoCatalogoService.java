package com.dentalpro.tratamiento;

import com.dentalpro.tratamiento.dto.TratamientoCatalogoDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TratamientoCatalogoService {

    private final TratamientoCatalogoRepository repository;

    public TratamientoCatalogoService(TratamientoCatalogoRepository repository) {
        this.repository = repository;
    }

    public List<TratamientoCatalogoDto> listar() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public TratamientoCatalogoDto findById(Long id) {
        return toDto(getOrThrow(id));
    }

    public TratamientoCatalogoDto crear(TratamientoCatalogoDto dto) {
        if (repository.existsByNombre(dto.nombre())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un tratamiento con ese nombre");
        }
        TratamientoCatalogo t = new TratamientoCatalogo();
        t.setNombre(dto.nombre());
        t.setDescripcion(dto.descripcion());
        t.setPrecio(dto.precio());
        t.setDuracionMinutos(dto.duracionMinutos());
        return toDto(repository.save(t));
    }

    public TratamientoCatalogoDto actualizar(Long id, TratamientoCatalogoDto dto) {
        TratamientoCatalogo t = getOrThrow(id);
        if (!t.getNombre().equals(dto.nombre()) && repository.existsByNombre(dto.nombre())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un tratamiento con ese nombre");
        }
        t.setNombre(dto.nombre());
        t.setDescripcion(dto.descripcion());
        t.setPrecio(dto.precio());
        t.setDuracionMinutos(dto.duracionMinutos());
        return toDto(repository.save(t));
    }

    public void eliminar(Long id) {
        getOrThrow(id);
        repository.deleteById(id);
    }

    private TratamientoCatalogo getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tratamiento no encontrado"));
    }

    private TratamientoCatalogoDto toDto(TratamientoCatalogo t) {
        return new TratamientoCatalogoDto(t.getId(), t.getNombre(), t.getDescripcion(),
                t.getPrecio(), t.getDuracionMinutos());
    }
}
