package com.jumder.ulloa.entity;


import com.jumder.ulloa.Polideportivo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "torneos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTorneo estado = EstadoTorneo.INSCRIPCIONES_ABIERTAS;

    @Enumerated(EnumType.STRING)
    private TipoTorneo tipo;

    @Column(nullable = false)
    private Integer numeroEquipos;

    private Integer jugadoresPorEquipo;

    @Column(nullable = false)
    private BigDecimal valorCarnet;

    @Column(nullable = false)
    private BigDecimal valorInscripcionEquipo;

    @ManyToOne
    @JoinColumn(name = "polideportivo_id", nullable = false)
    private Polideportivo polideportivo;

    @OneToMany(mappedBy = "torneo", cascade = CascadeType.ALL)
    private List<Equipo> equipos = new ArrayList<>();

    @OneToMany(mappedBy = "torneo", cascade = CascadeType.ALL)
    private List<Partido> partidos = new ArrayList<>();

    @OneToMany(mappedBy = "torneo", cascade = CascadeType.ALL)
    private List<Grupo> grupos = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;

    public enum EstadoTorneo {
        INSCRIPCIONES_ABIERTAS,
        INSCRIPCIONES_CERRADAS,
        EN_CURSO,
        FASE_GRUPOS,
        OCTAVOS,
        CUARTOS,
        SEMIFINALES,
        FINAL,
        FINALIZADO,
        CANCELADO
    }

    public enum TipoTorneo {
        ELIMINACION_DIRECTA,
        GRUPOS_Y_ELIMINACION,
        TODOS_CONTRA_TODOS,
        COPA
    }
}