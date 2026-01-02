package com.jumder.ulloa.repository;
import com.jumder.ulloa.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByPolideportivoId(Long polideportivoId);
    List<Producto> findByPolideportivoIdAndActivoTrue(Long polideportivoId);
    List<Producto> findByCategoria(Producto.CategoriaProducto categoria);
    Optional<Producto> findByCodigoBarras(String codigoBarras);

    @Query("SELECT p FROM Producto p WHERE p.polideportivo.id = :polideportivoId AND p.stock <= p.stockMinimo")
    List<Producto> findProductosBajoStock(@Param("polideportivoId") Long polideportivoId);
}