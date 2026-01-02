package com.jumder.ulloa.repository;
import com.jumder.ulloa.entity.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    List<Equipo> findByTorneoId(Long torneoId);
    List<Equipo> findByGrupoId(Long grupoId);
    Optional<Equipo> findByNombreAndTorneoId(String nombre, Long torneoId);
    List<Equipo> findByTorneoIdAndInscripcionPagadaTrue(Long torneoId);
    List<Equipo> findByTorneoIdAndInscripcionPagadaFalse(Long torneoId);

    @Query("SELECT e FROM Equipo e WHERE e.torneo.id = :torneoId ORDER BY e.puntos DESC, e.golesAFavor DESC")
    List<Equipo> findByTorneoIdOrderByPuntosDesc(@Param("torneoId") Long torneoId);
}