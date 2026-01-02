package com.jumder.ulloa.entity;

import com.jumder.ulloa.Polideportivo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroVenta; // Ej: V-2024-001

    @ManyToOne
    @JoinColumn(name = "polideportivo_id", nullable = false)
    private Polideportivo polideportivo;

    @ManyToOne
    @JoinColumn(name = "cajero_id", nullable = false)
    private Usuario cajero;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Usuario cliente; // Si el cliente está registrado

    private String nombreCliente; // Si no está registrado

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private List<DetalleVenta> detalles = new ArrayList<>();

    @Column(nullable = false)
    private BigDecimal subtotal;

    private BigDecimal descuento = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoVenta estado = EstadoVenta.COMPLETADA;

    @Column(nullable = false)
    private Boolean pagada = true;

    @ManyToOne
    @JoinColumn(name = "partido_id")
    private Partido partido; // Para asociar ventas a partidos específicos

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @CreationTimestamp
    private LocalDateTime fechaVenta;

    public enum MetodoPago {
        EFECTIVO,
        TRANSFERENCIA,
        TARJETA_DEBITO,
        TARJETA_CREDITO,
        NEQUI,
        DAVIPLATA,
        CREDITO // Fiado
    }

    public enum EstadoVenta {
        COMPLETADA,
        PENDIENTE_PAGO,
        CANCELADA,
        DEVOLUCION
    }
}