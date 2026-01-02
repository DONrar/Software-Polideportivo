package com.jumder.ulloa.service;

import com.jumder.ulloa.dto.PartidoDTO;
import com.jumder.ulloa.entity.*;
import com.jumder.ulloa.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartidoService {

    private final PartidoRepository partidoRepository;
    private final EquipoRepository equipoRepository;
    private final JugadorRepository jugadorRepository;
    private final JugadorPartidoRepository jugadorPartidoRepository;
    private final EventoPartidoRepository eventoPartidoRepository;

    @Transactional
    public void registrarGol(Long partidoId, Long jugadorId, Integer minuto, Long jugadorAsistenciaId) {
        Partido partido = partidoRepository.findById(partidoId)
                .orElseThrow(() -> new RuntimeException("Partido no encontrado"));

        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));

        // Registrar evento
        EventoPartido evento = new EventoPartido();
        evento.setPartido(partido);
        evento.setJugador(jugador);
        evento.setEquipo(jugador.getEquipo());
        evento.setTipo(EventoPartido.TipoEvento.GOL);
        evento.setMinuto(minuto);

        if (jugadorAsistenciaId != null) {
            Jugador jugadorAsistencia = jugadorRepository.findById(jugadorAsistenciaId)
                    .orElseThrow(() -> new RuntimeException("Jugador asistencia no encontrado"));
            evento.setJugadorAsistencia(jugadorAsistencia);

            // Actualizar estadísticas de asistencia
            jugadorAsistencia.setAsistencias(jugadorAsistencia.getAsistencias() + 1);
            jugadorRepository.save(jugadorAsistencia);
        }

        eventoPartidoRepository.save(evento);

        // Actualizar marcador
        if (jugador.getEquipo().getId().equals(partido.getEquipoLocal().getId())) {
            partido.setGolesLocal(partido.getGolesLocal() + 1);
        } else {
            partido.setGolesVisitante(partido.getGolesVisitante() + 1);
        }
        partidoRepository.save(partido);

        // Actualizar estadísticas del jugador
        jugador.setGoles(jugador.getGoles() + 1);
        jugadorRepository.save(jugador);

        // Actualizar JugadorPartido
        JugadorPartido jugadorPartido = jugadorPartidoRepository.findByPartidoId(partidoId)
                .stream()
                .filter(jp -> jp.getJugador().getId().equals(jugadorId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("JugadorPartido no encontrado"));

        jugadorPartido.setGoles(jugadorPartido.getGoles() + 1);
        jugadorPartidoRepository.save(jugadorPartido);
    }

    @Transactional
    public void registrarTarjeta(Long partidoId, Long jugadorId, Integer minuto, EventoPartido.TipoEvento tipoTarjeta) {
        Partido partido = partidoRepository.findById(partidoId)
                .orElseThrow(() -> new RuntimeException("Partido no encontrado"));

        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));

        // Registrar evento
        EventoPartido evento = new EventoPartido();
        evento.setPartido(partido);
        evento.setJugador(jugador);
        evento.setEquipo(jugador.getEquipo());
        evento.setTipo(tipoTarjeta);
        evento.setMinuto(minuto);
        eventoPartidoRepository.save(evento);

        // Actualizar estadísticas del jugador
        if (tipoTarjeta == EventoPartido.TipoEvento.TARJETA_AMARILLA) {
            jugador.setTarjetasAmarillas(jugador.getTarjetasAmarillas() + 1);
        } else if (tipoTarjeta == EventoPartido.TipoEvento.TARJETA_ROJA) {
            jugador.setTarjetasRojas(jugador.getTarjetasRojas() + 1);
        }
        jugadorRepository.save(jugador);

        // Actualizar JugadorPartido
        JugadorPartido jugadorPartido = jugadorPartidoRepository.findByPartidoId(partidoId)
                .stream()
                .filter(jp -> jp.getJugador().getId().equals(jugadorId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("JugadorPartido no encontrado"));

        if (tipoTarjeta == EventoPartido.TipoEvento.TARJETA_AMARILLA) {
            jugadorPartido.setTarjetasAmarillas(jugadorPartido.getTarjetasAmarillas() + 1);
        } else if (tipoTarjeta == EventoPartido.TipoEvento.TARJETA_ROJA) {
            jugadorPartido.setTarjetasRojas(jugadorPartido.getTarjetasRojas() + 1);
        }
        jugadorPartidoRepository.save(jugadorPartido);
    }

    @Transactional
    public void verificarCarnetJugador(Long partidoId, Long jugadorId) {
        JugadorPartido jugadorPartido = jugadorPartidoRepository.findByPartidoId(partidoId)
                .stream()
                .filter(jp -> jp.getJugador().getId().equals(jugadorId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("JugadorPartido no encontrado"));

        Jugador jugador = jugadorPartido.getJugador();

        if (!jugador.getCarnetPagado()) {
            throw new RuntimeException("El jugador no ha pagado el carnet");
        }

        jugadorPartido.setCarnetVerificado(true);
        jugadorPartido.setPresente(true);
        jugadorPartidoRepository.save(jugadorPartido);
    }

    @Transactional
    public void finalizarPartido(Long partidoId) {
        Partido partido = partidoRepository.findById(partidoId)
                .orElseThrow(() -> new RuntimeException("Partido no encontrado"));

        partido.setEstado(Partido.EstadoPartido.FINALIZADO);

        // Determinar ganador
        Equipo ganador = null;
        if (partido.getGolesLocal() > partido.getGolesVisitante()) {
            ganador = partido.getEquipoLocal();
        } else if (partido.getGolesVisitante() > partido.getGolesLocal()) {
            ganador = partido.getEquipoVisitante();
        }
        partido.setGanador(ganador);
        partidoRepository.save(partido);

        // Actualizar estadísticas de equipos
        actualizarEstadisticasEquipos(partido);
    }

    private void actualizarEstadisticasEquipos(Partido partido) {
        Equipo local = partido.getEquipoLocal();
        Equipo visitante = partido.getEquipoVisitante();

        // Actualizar partidos jugados
        local.setPartidosJugados(local.getPartidosJugados() + 1);
        visitante.setPartidosJugados(visitante.getPartidosJugados() + 1);

        // Actualizar goles
        local.setGolesAFavor(local.getGolesAFavor() + partido.getGolesLocal());
        local.setGolesEnContra(local.getGolesEnContra() + partido.getGolesVisitante());
        visitante.setGolesAFavor(visitante.getGolesAFavor() + partido.getGolesVisitante());
        visitante.setGolesEnContra(visitante.getGolesEnContra() + partido.getGolesLocal());

        // Determinar resultado
        if (partido.getGolesLocal() > partido.getGolesVisitante()) {
            local.setPartidosGanados(local.getPartidosGanados() + 1);
            local.setPuntos(local.getPuntos() + 3);
            visitante.setPartidosPerdidos(visitante.getPartidosPerdidos() + 1);
        } else if (partido.getGolesVisitante() > partido.getGolesLocal()) {
            visitante.setPartidosGanados(visitante.getPartidosGanados() + 1);
            visitante.setPuntos(visitante.getPuntos() + 3);
            local.setPartidosPerdidos(local.getPartidosPerdidos() + 1);
        } else {
            local.setPartidosEmpatados(local.getPartidosEmpatados() + 1);
            local.setPuntos(local.getPuntos() + 1);
            visitante.setPartidosEmpatados(visitante.getPartidosEmpatados() + 1);
            visitante.setPuntos(visitante.getPuntos() + 1);
        }

        equipoRepository.save(local);
        equipoRepository.save(visitante);
    }

    public List<PartidoDTO> listarPartidosPorTorneo(Long torneoId) {
        return partidoRepository.findByTorneoIdOrderByFechaHoraAsc(torneoId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public PartidoDTO obtenerPartidoPorId(Long id) {
        return partidoRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Partido no encontrado"));
    }

    private PartidoDTO convertirADTO(Partido partido) {
        PartidoDTO dto = new PartidoDTO();
        dto.setId(partido.getId());
        dto.setTorneoId(partido.getTorneo().getId());
        dto.setTorneoNombre(partido.getTorneo().getNombre());
        dto.setEquipoLocalId(partido.getEquipoLocal().getId());
        dto.setEquipoLocalNombre(partido.getEquipoLocal().getNombre());
        dto.setEquipoLocalLogo(partido.getEquipoLocal().getLogo());
        dto.setEquipoVisitanteId(partido.getEquipoVisitante().getId());
        dto.setEquipoVisitanteNombre(partido.getEquipoVisitante().getNombre());
        dto.setEquipoVisitanteLogo(partido.getEquipoVisitante().getLogo());
        dto.setFechaHora(partido.getFechaHora());
        dto.setEstado(partido.getEstado().name());
        dto.setFase(partido.getFase() != null ? partido.getFase().name() : null);
        dto.setGolesLocal(partido.getGolesLocal());
        dto.setGolesVisitante(partido.getGolesVisitante());

        if (partido.getCancha() != null) {
            dto.setCanchaId(partido.getCancha().getId());
            dto.setCanchaNombre(partido.getCancha().getNombre());
        }

        if (partido.getGrupo() != null) {
            dto.setGrupoId(partido.getGrupo().getId());
            dto.setGrupoNombre(partido.getGrupo().getNombre());
        }

        if (partido.getGanador() != null) {
            dto.setGanadorId(partido.getGanador().getId());
            dto.setGanadorNombre(partido.getGanador().getNombre());
        }

        return dto;
    }
}