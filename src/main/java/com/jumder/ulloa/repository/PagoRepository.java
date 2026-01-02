package com.jumder.ulloa.repository;
import com.jumder.ulloa.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByTorneoId(Long torneoId);
    List<Pago> findByJugadorId(Long jugadorId);
    List<Pago> findByEquipoId(Long equipoId);
    List<Pago> findByTipo(Pago.TipoPago tipo);

    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.torneo.id = :torneoId AND p.tipo = :tipo")
    BigDecimal calcularTotalPagosPorTipo(@Param("torneoId") Long torneoId,
                                         @Param("tipo") Pago.TipoPago tipo);

    @Query("SELECT p FROM Pago p WHERE p.torneo.id = :torneoId AND p.fechaPago >= :fechaInicio AND p.fechaPago <= :fechaFin")
    List<Pago> findByTorneoAndFecha(@Param("torneoId") Long torneoId,
                                    @Param("fechaInicio") LocalDateTime fechaInicio,
                                    @Param("fechaFin") LocalDateTime fechaFin);
}