package com.jumder.ulloa.repository;

import com.jumder.ulloa.entity.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {
    List<MovimientoInventario> findByProductoId(Long productoId);
    List<MovimientoInventario> findByTipo(MovimientoInventario.TipoMovimiento tipo);
    List<MovimientoInventario> findByVentaId(Long ventaId);

    @Query("SELECT m FROM MovimientoInventario m WHERE m.producto.polideportivo.id = :polideportivoId AND m.fechaMovimiento >= :fechaInicio AND m.fechaMovimiento <= :fechaFin")
    List<MovimientoInventario> findByPolideportivoAndFecha(@Param("polideportivoId") Long polideportivoId,
                                                           @Param("fechaInicio") LocalDateTime fechaInicio,
                                                           @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT m FROM MovimientoInventario m WHERE m.producto.id = :productoId ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventario> findByProductoIdOrderByFechaDesc(@Param("productoId") Long productoId);
}