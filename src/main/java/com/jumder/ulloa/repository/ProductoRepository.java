package com.jumder.ulloa.repository;

import com.jumder.ulloa.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByPolideportivoId(Long polideportivoId);
    List<Producto> findByPolideportivoIdAndActivoTrue(Long polideportivoId);
    // ✅ Ahora recibe CategoriaProducto en vez de String
    List<Producto> findByPolideportivoIdAndCategoria(Long polideportivoId, Producto.CategoriaProducto categoria);
    @Query("""
    SELECT p
    FROM Producto p
    WHERE p.polideportivo.id = :polideportivoId
      AND p.stock <= p.stockMinimo
      AND p.activo = true
""")
    List<Producto> findProductosBajoStock(@Param("polideportivoId") Long polideportivoId);

}