package com.jumder.ulloa.repository;
import com.jumder.ulloa.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    List<DetalleVenta> findByVentaId(Long ventaId);

    @Query("SELECT dv FROM DetalleVenta dv WHERE dv.venta.polideportivo.id = :polideportivoId AND dv.venta.fechaVenta >= :fechaInicio AND dv.venta.fechaVenta <= :fechaFin")
    List<DetalleVenta> findByPolideportivoAndFecha(@Param("polideportivoId") Long polideportivoId,
                                                   @Param("fechaInicio") LocalDateTime fechaInicio,
                                                   @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT dv.producto, SUM(dv.cantidad) as totalVendido FROM DetalleVenta dv WHERE dv.venta.polideportivo.id = :polideportivoId GROUP BY dv.producto ORDER BY totalVendido DESC")
    List<Object[]> findProductosMasVendidos(@Param("polideportivoId") Long polideportivoId);
}