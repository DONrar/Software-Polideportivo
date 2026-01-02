package com.jumder.ulloa.service;

import com.jumder.ulloa.Polideportivo;
import com.jumder.ulloa.dto.TorneoDTO;
import com.jumder.ulloa.entity.Equipo;
import com.jumder.ulloa.entity.Grupo;
import com.jumder.ulloa.entity.Partido;
import com.jumder.ulloa.entity.Torneo;
import com.jumder.ulloa.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TorneoService {

    private final TorneoRepository torneoRepository;
    private final PolideportivoRepository polideportivoRepository;
    private final EquipoRepository equipoRepository;
    private final JugadorRepository jugadorRepository;
    private final GrupoRepository grupoRepository;
    private final PartidoRepository partidoRepository;

    @Transactional
    public TorneoDTO crearTorneo(TorneoDTO dto) {
        Polideportivo polideportivo = polideportivoRepository.findById(dto.getPolideportivoId())
                .orElseThrow(() -> new RuntimeException("Polideportivo no encontrado"));

        Torneo torneo = new Torneo();
        torneo.setNombre(dto.getNombre());
        torneo.setDescripcion(dto.getDescripcion());
        torneo.setFechaInicio(dto.getFechaInicio());
        torneo.setFechaFin(dto.getFechaFin());
        torneo.setEstado(Torneo.EstadoTorneo.valueOf(dto.getEstado()));
        torneo.setTipo(Torneo.TipoTorneo.valueOf(dto.getTipo()));
        torneo.setNumeroEquipos(dto.getNumeroEquipos());
        torneo.setJugadoresPorEquipo(dto.getJugadoresPorEquipo());
        torneo.setValorCarnet(dto.getValorCarnet());
        torneo.setValorInscripcionEquipo(dto.getValorInscripcionEquipo());
        torneo.setPolideportivo(polideportivo);

        torneo = torneoRepository.save(torneo);

        return convertirADTO(torneo);
    }

    @Transactional
    public void generarFixture(Long torneoId) {
        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));

        List<Equipo> equipos = equipoRepository.findByTorneoId(torneoId);

        if (equipos.size() < 2) {
            throw new RuntimeException("Se necesitan al menos 2 equipos para generar fixture");
        }

        switch (torneo.getTipo()) {
            case GRUPOS_Y_ELIMINACION:
                generarFixtureConGrupos(torneo, equipos);
                break;
            case ELIMINACION_DIRECTA:
                generarFixtureEliminacionDirecta(torneo, equipos);
                break;
            case TODOS_CONTRA_TODOS:
                generarFixtureTodosContraTodos(torneo, equipos);
                break;
            default:
                throw new RuntimeException("Tipo de torneo no soportado");
        }

        torneo.setEstado(Torneo.EstadoTorneo.INSCRIPCIONES_CERRADAS);
        torneoRepository.save(torneo);
    }

    private void generarFixtureConGrupos(Torneo torneo, List<Equipo> equipos) {
        // Determinar número de grupos (por ejemplo, 4 equipos por grupo)
        int equiposPorGrupo = 4;
        int numeroGrupos = (int) Math.ceil((double) equipos.size() / equiposPorGrupo);

        // Crear grupos
        for (int i = 0; i < numeroGrupos; i++) {
            Grupo grupo = new Grupo();
            grupo.setNombre("Grupo " + (char) ('A' + i));
            grupo.setTorneo(torneo);
            grupo = grupoRepository.save(grupo);

            // Asignar equipos al grupo
            int inicio = i * equiposPorGrupo;
            int fin = Math.min(inicio + equiposPorGrupo, equipos.size());

            for (int j = inicio; j < fin; j++) {
                Equipo equipo = equipos.get(j);
                equipo.setGrupo(grupo);
                equipoRepository.save(equipo);
            }
        }

        // Generar partidos por grupo
        List<Grupo> grupos = grupoRepository.findByTorneoId(torneo.getId());
        LocalDate fechaActual = torneo.getFechaInicio();

        for (Grupo grupo : grupos) {
            List<Equipo> equiposGrupo = equipoRepository.findByGrupoId(grupo.getId());

            // Todos contra todos en el grupo
            for (int i = 0; i < equiposGrupo.size(); i++) {
                for (int j = i + 1; j < equiposGrupo.size(); j++) {
                    Partido partido = new Partido();
                    partido.setTorneo(torneo);
                    partido.setEquipoLocal(equiposGrupo.get(i));
                    partido.setEquipoVisitante(equiposGrupo.get(j));
                    partido.setFechaHora(fechaActual.atTime(18, 0));
                    partido.setEstado(Partido.EstadoPartido.PROGRAMADO);
                    partido.setFase(Partido.FasePartido.GRUPOS);
                    partido.setGrupo(grupo);
                    partidoRepository.save(partido);

                    fechaActual = fechaActual.plusDays(1);
                }
            }
        }
    }

    private void generarFixtureEliminacionDirecta(Torneo torneo, List<Equipo> equipos) {
        // Implementar lógica de eliminación directa
        // Similar a un torneo de copa mundial
        LocalDate fechaActual = torneo.getFechaInicio();

        for (int i = 0; i < equipos.size(); i += 2) {
            if (i + 1 < equipos.size()) {
                Partido partido = new Partido();
                partido.setTorneo(torneo);
                partido.setEquipoLocal(equipos.get(i));
                partido.setEquipoVisitante(equipos.get(i + 1));
                partido.setFechaHora(fechaActual.atTime(18, 0));
                partido.setEstado(Partido.EstadoPartido.PROGRAMADO);

                // Determinar fase según cantidad de equipos
                if (equipos.size() <= 2) {
                    partido.setFase(Partido.FasePartido.FINAL);
                } else if (equipos.size() <= 4) {
                    partido.setFase(Partido.FasePartido.SEMIFINAL);
                } else if (equipos.size() <= 8) {
                    partido.setFase(Partido.FasePartido.CUARTOS);
                } else {
                    partido.setFase(Partido.FasePartido.OCTAVOS);
                }

                partidoRepository.save(partido);
                fechaActual = fechaActual.plusDays(1);
            }
        }
    }

    private void generarFixtureTodosContraTodos(Torneo torneo, List<Equipo> equipos) {
        LocalDate fechaActual = torneo.getFechaInicio();

        for (int i = 0; i < equipos.size(); i++) {
            for (int j = i + 1; j < equipos.size(); j++) {
                Partido partido = new Partido();
                partido.setTorneo(torneo);
                partido.setEquipoLocal(equipos.get(i));
                partido.setEquipoVisitante(equipos.get(j));
                partido.setFechaHora(fechaActual.atTime(18, 0));
                partido.setEstado(Partido.EstadoPartido.PROGRAMADO);
                partido.setFase(Partido.FasePartido.GRUPOS);
                partidoRepository.save(partido);

                fechaActual = fechaActual.plusDays(1);
            }
        }
    }

    public List<TorneoDTO> listarTorneosPorPolideportivo(Long polideportivoId) {
        return torneoRepository.findByPolideportivoId(polideportivoId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public TorneoDTO obtenerTorneoPorId(Long id) {
        return torneoRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));
    }

    private TorneoDTO convertirADTO(Torneo torneo) {
        TorneoDTO dto = new TorneoDTO();
        dto.setId(torneo.getId());
        dto.setNombre(torneo.getNombre());
        dto.setDescripcion(torneo.getDescripcion());
        dto.setFechaInicio(torneo.getFechaInicio());
        dto.setFechaFin(torneo.getFechaFin());
        dto.setEstado(torneo.getEstado().name());
        dto.setTipo(torneo.getTipo().name());
        dto.setNumeroEquipos(torneo.getNumeroEquipos());
        dto.setJugadoresPorEquipo(torneo.getJugadoresPorEquipo());
        dto.setValorCarnet(torneo.getValorCarnet());
        dto.setValorInscripcionEquipo(torneo.getValorInscripcionEquipo());
        dto.setPolideportivoId(torneo.getPolideportivo().getId());
        dto.setPolideportivoNombre(torneo.getPolideportivo().getNombre());
        dto.setEquiposInscritos(equipoRepository.findByTorneoId(torneo.getId()).size());
        dto.setJugadoresRegistrados(jugadorRepository.findByEquipoTorneoId(torneo.getId()).size());
        return dto;
    }
}