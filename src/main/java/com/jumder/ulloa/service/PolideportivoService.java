package com.jumder.ulloa.service;

import com.jumder.ulloa.Polideportivo;
import com.jumder.ulloa.dto.PolideportivoDTO;
import com.jumder.ulloa.repository.PolideportivoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PolideportivoService {

    private final PolideportivoRepository polideportivoRepository;

    public List<PolideportivoDTO> listarTodos() {
        return polideportivoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public PolideportivoDTO obtenerPorId(Long id) {
        return polideportivoRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Polideportivo no encontrado"));
    }

    @Transactional
    public PolideportivoDTO crear(PolideportivoDTO dto) {
        Polideportivo polideportivo = new Polideportivo();
        polideportivo.setNombre(dto.getNombre());
        polideportivo.setDireccion(dto.getDireccion());
        polideportivo.setTelefono(dto.getTelefono());
        polideportivo.setEmail(dto.getEmail());
        polideportivo.setCiudad(dto.getCiudad());
        polideportivo.setActivo(dto.getActivo() != null ? dto.getActivo() : true);

        polideportivo = polideportivoRepository.save(polideportivo);
        return convertirADTO(polideportivo);
    }

    @Transactional
    public PolideportivoDTO actualizar(PolideportivoDTO dto) {
        Polideportivo polideportivo = polideportivoRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Polideportivo no encontrado"));

        polideportivo.setNombre(dto.getNombre());
        polideportivo.setDireccion(dto.getDireccion());
        polideportivo.setTelefono(dto.getTelefono());
        polideportivo.setEmail(dto.getEmail());
        polideportivo.setCiudad(dto.getCiudad());
        polideportivo.setActivo(dto.getActivo());

        polideportivo = polideportivoRepository.save(polideportivo);
        return convertirADTO(polideportivo);
    }

    @Transactional
    public void eliminar(Long id) {
        Polideportivo polideportivo = polideportivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Polideportivo no encontrado"));
        polideportivo.setActivo(false);
        polideportivoRepository.save(polideportivo);
    }

    private PolideportivoDTO convertirADTO(Polideportivo polideportivo) {
        PolideportivoDTO dto = new PolideportivoDTO();
        dto.setId(polideportivo.getId());
        dto.setNombre(polideportivo.getNombre());
        dto.setDireccion(polideportivo.getDireccion());
        dto.setTelefono(polideportivo.getTelefono());
        dto.setEmail(polideportivo.getEmail());
        dto.setCiudad(polideportivo.getCiudad());
        dto.setActivo(polideportivo.getActivo());
        return dto;
    }
}