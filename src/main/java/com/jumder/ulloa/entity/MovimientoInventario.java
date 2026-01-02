package com.jumder.ulloa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos_inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipo;

    @Column(nullable = false)
    private Integer cantidad;

    private Integer stockAnterior;
    private Integer stockNuevo;

    private BigDecimal costoUnitario;
    private BigDecimal costoTotal;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta; // Si el movimiento es por una venta

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @CreationTimestamp
    private LocalDateTime fechaMovimiento;

    public enum TipoMovimiento {
        ENTRADA_COMPRA,
        ENTRADA_DEVOLUCION,
        ENTRADA_AJUSTE,
        SALIDA_VENTA,
        SALIDA_MERMA,
        SALIDA_AJUSTE
    }
}