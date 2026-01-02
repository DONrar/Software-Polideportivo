package com.jumder.ulloa.entity;

import com.jumder.ulloa.Polideportivo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private BigDecimal precio;

    private BigDecimal costo; // Para calcular utilidad

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaProducto categoria;

    private String codigoBarras;

    private String imagen;

    @Column(nullable = false)
    private Integer stock = 0;

    private Integer stockMinimo = 0; // Para alertas

    @ManyToOne
    @JoinColumn(name = "polideportivo_id", nullable = false)
    private Polideportivo polideportivo;

    @Column(nullable = false)
    private Boolean activo = true;

    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;

    public enum CategoriaProducto {
        CERVEZA,
        BEBIDA_ALCOHOLICA,
        GASEOSA,
        AGUA,
        BEBIDA_ENERGIZANTE,
        SNACK,
        COMIDA,
        CIGARRILLOS,
        OTROS
    }
}