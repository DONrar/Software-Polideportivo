package com.jumder.ulloa.repository;
import com.jumder.ulloa.entity.Partido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface PartidoRepository extends JpaRepository<Partido, Long> {
    List<Partido> findByTorneoId(Long torneoId);
    List<Partido> findByEstado(Partido.EstadoPartido estado);
    List<Partido> findByTorneoIdAndEstado(Long torneoId, Partido.EstadoPartido estado);
    List<Partido> findByFase(Partido.FasePartido fase);
    List<Partido> findByGrupoId(Long grupoId);
    List<Partido> findByCanchaId(Long canchaId);

    @Query("SELECT p FROM Partido p WHERE p.equipoLocal.id = :equipoId OR p.equipoVisitante.id = :equipoId")
    List<Partido> findByEquipoId(@Param("equipoId") Long equipoId);

    @Query("SELECT p FROM Partido p WHERE p.torneo.id = :torneoId AND p.fechaHora >= :fechaInicio AND p.fechaHora <= :fechaFin")
    List<Partido> findByTorneoIdAndFechaEntre(@Param("torneoId") Long torneoId,
                                              @Param("fechaInicio") LocalDateTime fechaInicio,
                                              @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT p FROM Partido p WHERE p.torneo.id = :torneoId ORDER BY p.fechaHora ASC")
    List<Partido> findByTorneoIdOrderByFechaHoraAsc(@Param("torneoId") Long torneoId);

    @Query("SELECT p FROM Partido p WHERE DATE(p.fechaHora) = :fecha ORDER BY p.fechaHora ASC")
    List<Partido> findByFecha(@Param("fecha") LocalDate fecha);
}