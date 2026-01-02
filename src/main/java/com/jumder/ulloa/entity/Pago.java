package com.jumder.ulloa.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigoPago; // Ej: PAG-2024-001

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPago tipo;

    @ManyToOne
    @JoinColumn(name = "jugador_id")
    private Jugador jugador;

    @ManyToOne
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    @ManyToOne
    @JoinColumn(name = "torneo_id", nullable = false)
    private Torneo torneo;

    @Column(nullable = false)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;

    @ManyToOne
    @JoinColumn(name = "recibido_por_id", nullable = false)
    private Usuario recibidoPor;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @CreationTimestamp
    private LocalDateTime fechaPago;

    public enum TipoPago {
        CARNET_JUGADOR,
        INSCRIPCION_EQUIPO
    }

    public enum MetodoPago {
        EFECTIVO,
        TRANSFERENCIA,
        TARJETA_DEBITO,
        TARJETA_CREDITO,
        NEQUI,
        DAVIPLATA
    }
}