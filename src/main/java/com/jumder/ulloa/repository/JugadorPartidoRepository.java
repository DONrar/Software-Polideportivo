package com.jumder.ulloa.repository;
import com.jumder.ulloa.entity.JugadorPartido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface JugadorPartidoRepository extends JpaRepository<JugadorPartido, Long> {
    List<JugadorPartido> findByPartidoId(Long partidoId);
    List<JugadorPartido> findByJugadorId(Long jugadorId);
    List<JugadorPartido> findByPartidoIdAndCarnetVerificadoFalse(Long partidoId);

    @Query("SELECT jp FROM JugadorPartido jp WHERE jp.partido.id = :partidoId AND jp.presente = true")
    List<JugadorPartido> findJugadoresPresentesByPartido(@Param("partidoId") Long partidoId);
}