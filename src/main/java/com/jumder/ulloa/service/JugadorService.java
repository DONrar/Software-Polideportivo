package com.jumder.ulloa.service;

import com.jumder.ulloa.dto.JugadorDTO;
import com.jumder.ulloa.entity.Equipo;
import com.jumder.ulloa.entity.Jugador;
import com.jumder.ulloa.repository.EquipoRepository;
import com.jumder.ulloa.repository.JugadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JugadorService {

    private final JugadorRepository jugadorRepository;
    private final EquipoRepository equipoRepository;

    public List<JugadorDTO> listarTodos() {
        return jugadorRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<JugadorDTO> listarPorEquipo(Long equipoId) {
        return jugadorRepository.findByEquipoId(equipoId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<JugadorDTO> listarActivosPorEquipo(Long equipoId) {
        return jugadorRepository.findByEquipoIdAndActivoTrue(equipoId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<JugadorDTO> listarPorTorneo(Long torneoId) {
        return jugadorRepository.findByEquipoTorneoId(torneoId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<JugadorDTO> listarSinPagarPorTorneo(Long torneoId) {
        return jugadorRepository.findJugadoresSinPagarCarnetByTorneo(torneoId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public JugadorDTO obtenerPorId(Long id) {
        return jugadorRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
    }

    public JugadorDTO buscarPorCedula(String cedula) {
        return jugadorRepository.findByCedula(cedula)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
    }

    @Transactional
    public JugadorDTO crear(JugadorDTO dto) {
        // Validar que no exista la cédula
        if (jugadorRepository.findByCedula(dto.getCedula()).isPresent()) {
            throw new RuntimeException("Ya existe un jugador con esa cédula");
        }

        Equipo equipo = equipoRepository.findById(dto.getEquipoId())
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

        Jugador jugador = new Jugador();
        jugador.setNombreCompleto(dto.getNombreCompleto());
        jugador.setCedula(dto.getCedula());
        jugador.setFechaNacimiento(dto.getFechaNacimiento());
        jugador.setTelefono(dto.getTelefono());
        jugador.setEmail(dto.getEmail());
        jugador.setFoto(dto.getFoto());
        jugador.setNumeroCamiseta(dto.getNumeroCamiseta());
        jugador.setPosicion(dto.getPosicion() != null
                ? Jugador.Posicion.valueOf(dto.getPosicion())
                : null);
        jugador.setEquipo(equipo);
        jugador.setCarnetPagado(dto.getCarnetPagado() != null ? dto.getCarnetPagado() : false);
        jugador.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        jugador.setVerificado(dto.getVerificado() != null ? dto.getVerificado() : false);

        jugador = jugadorRepository.save(jugador);
        return convertirADTO(jugador);
    }

    @Transactional
    public JugadorDTO actualizar(JugadorDTO dto) {
        Jugador jugador = jugadorRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));

        // Validar cédula única (excepto el mismo jugador)
        jugadorRepository.findByCedula(dto.getCedula()).ifPresent(j -> {
            if (!j.getId().equals(dto.getId())) {
                throw new RuntimeException("Ya existe otro jugador con esa cédula");
            }
        });

        jugador.setNombreCompleto(dto.getNombreCompleto());
        jugador.setCedula(dto.getCedula());
        jugador.setFechaNacimiento(dto.getFechaNacimiento());
        jugador.setTelefono(dto.getTelefono());
        jugador.setEmail(dto.getEmail());
        jugador.setFoto(dto.getFoto());
        jugador.setNumeroCamiseta(dto.getNumeroCamiseta());
        jugador.setPosicion(dto.getPosicion() != null
                ? Jugador.Posicion.valueOf(dto.getPosicion())
                : null);
        jugador.setCarnetPagado(dto.getCarnetPagado());
        jugador.setActivo(dto.getActivo());
        jugador.setVerificado(dto.getVerificado());

        jugador = jugadorRepository.save(jugador);
        return convertirADTO(jugador);
    }

    @Transactional
    public void eliminar(Long id) {
        Jugador jugador = jugadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
        jugador.setActivo(false);
        jugadorRepository.save(jugador);
    }

    @Transactional
    public void marcarCarnetPagado(Long id) {
        Jugador jugador = jugadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
        jugador.setCarnetPagado(true);
        jugadorRepository.save(jugador);
    }

    @Transactional
    public void verificarJugador(Long id) {
        Jugador jugador = jugadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
        jugador.setVerificado(true);
        jugadorRepository.save(jugador);
    }

    @Transactional
    public void cambiarEquipo(Long jugadorId, Long nuevoEquipoId) {
        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));

        Equipo nuevoEquipo = equipoRepository.findById(nuevoEquipoId)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

        jugador.setEquipo(nuevoEquipo);
        jugadorRepository.save(jugador);
    }

    private JugadorDTO convertirADTO(Jugador jugador) {
        JugadorDTO dto = new JugadorDTO();
        dto.setId(jugador.getId());
        dto.setNombreCompleto(jugador.getNombreCompleto());
        dto.setCedula(jugador.getCedula());
        dto.setFechaNacimiento(jugador.getFechaNacimiento());
        dto.setTelefono(jugador.getTelefono());
        dto.setEmail(jugador.getEmail());
        dto.setFoto(jugador.getFoto());
        dto.setNumeroCamiseta(jugador.getNumeroCamiseta());
        dto.setPosicion(jugador.getPosicion() != null ? jugador.getPosicion().name() : null);
        dto.setEquipoId(jugador.getEquipo().getId());
        dto.setEquipoNombre(jugador.getEquipo().getNombre());
        dto.setCarnetPagado(jugador.getCarnetPagado());
        dto.setActivo(jugador.getActivo());
        dto.setVerificado(jugador.getVerificado());
        dto.setGoles(jugador.getGoles());
        dto.setAsistencias(jugador.getAsistencias());
        dto.setTarjetasAmarillas(jugador.getTarjetasAmarillas());
        dto.setTarjetasRojas(jugador.getTarjetasRojas());
        dto.setPartidosJugados(jugador.getPartidosJugados());
        return dto;
    }
}