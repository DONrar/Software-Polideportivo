package com.jumder.ulloa.repository;
import com.jumder.ulloa.entity.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface TorneoRepository extends JpaRepository<Torneo, Long> {
    List<Torneo> findByPolideportivoId(Long polideportivoId);
    List<Torneo> findByEstado(Torneo.EstadoTorneo estado);
    List<Torneo> findByPolideportivoIdAndEstado(Long polideportivoId, Torneo.EstadoTorneo estado);

    @Query("SELECT t FROM Torneo t WHERE t.fechaInicio >= :fechaInicio AND t.fechaInicio <= :fechaFin")
    List<Torneo> findByFechaInicioEntre(@Param("fechaInicio") LocalDate fechaInicio,
                                        @Param("fechaFin") LocalDate fechaFin);

    List<Torneo> findByPolideportivoIdOrderByFechaInicioDesc(Long polideportivoId);
}
