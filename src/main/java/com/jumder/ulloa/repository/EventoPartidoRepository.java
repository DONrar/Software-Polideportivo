package com.jumder.ulloa.repository;
import com.jumder.ulloa.entity.EventoPartido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface EventoPartidoRepository extends JpaRepository<EventoPartido, Long> {
    List<EventoPartido> findByPartidoId(Long partidoId);
    List<EventoPartido> findByJugadorId(Long jugadorId);
    List<EventoPartido> findByEquipoId(Long equipoId);
    List<EventoPartido> findByTipo(EventoPartido.TipoEvento tipo);

    @Query("SELECT e FROM EventoPartido e WHERE e.partido.id = :partidoId ORDER BY e.minuto ASC")
    List<EventoPartido> findByPartidoIdOrderByMinutoAsc(@Param("partidoId") Long partidoId);
}