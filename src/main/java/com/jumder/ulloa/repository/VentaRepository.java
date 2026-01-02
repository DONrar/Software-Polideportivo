package com.jumder.ulloa.repository;
import com.jumder.ulloa.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByPolideportivoId(Long polideportivoId);
    List<Venta> findByCajeroId(Long cajeroId);
    List<Venta> findByEstado(Venta.EstadoVenta estado);
    List<Venta> findByPagadaFalse();
    List<Venta> findByPartidoId(Long partidoId);

    @Query("SELECT v FROM Venta v WHERE v.polideportivo.id = :polideportivoId AND v.fechaVenta >= :fechaInicio AND v.fechaVenta <= :fechaFin")
    List<Venta> findVentasPorFecha(@Param("polideportivoId") Long polideportivoId,
                                   @Param("fechaInicio") LocalDateTime fechaInicio,
                                   @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT SUM(v.total) FROM Venta v WHERE v.polideportivo.id = :polideportivoId AND v.estado = 'COMPLETADA' AND v.fechaVenta >= :fechaInicio AND v.fechaVenta <= :fechaFin")
    BigDecimal calcularTotalVentasPorFecha(@Param("polideportivoId") Long polideportivoId,
                                           @Param("fechaInicio") LocalDateTime fechaInicio,
                                           @Param("fechaFin") LocalDateTime fechaFin);
}