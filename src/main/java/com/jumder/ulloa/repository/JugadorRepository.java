package com.jumder.ulloa.repository;

import com.jumder.ulloa.entity.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long> {
    Optional<Jugador> findByCedula(String cedula);
    List<Jugador> findByEquipoId(Long equipoId);
    List<Jugador> findByEquipoIdAndActivoTrue(Long equipoId);
    List<Jugador> findByCarnetPagadoFalse();
    List<Jugador> findByEquipoTorneoId(Long torneoId);

    @Query("SELECT j FROM Jugador j WHERE j.equipo.torneo.id = :torneoId AND j.carnetPagado = false")
    List<Jugador> findJugadoresSinPagarCarnetByTorneo(@Param("torneoId") Long torneoId);

    @Query("SELECT j FROM Jugador j WHERE j.equipo.torneo.id = :torneoId ORDER BY j.goles DESC")
    List<Jugador> findTopGoleadoresByTorneo(@Param("torneoId") Long torneoId);
}