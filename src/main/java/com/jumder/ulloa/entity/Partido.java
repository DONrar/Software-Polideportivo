package com.jumder.ulloa.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "partidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Partido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "torneo_id", nullable = false)
    private Torneo torneo;

    @ManyToOne
    @JoinColumn(name = "equipo_local_id", nullable = false)
    private Equipo equipoLocal;

    @ManyToOne
    @JoinColumn(name = "equipo_visitante_id", nullable = false)
    private Equipo equipoVisitante;

    @ManyToOne
    @JoinColumn(name = "cancha_id")
    private Cancha cancha;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPartido estado = EstadoPartido.PROGRAMADO;

    @Enumerated(EnumType.STRING)
    private FasePartido fase;

    @ManyToOne
    @JoinColumn(name = "grupo_id")
    private Grupo grupo; // Si es fase de grupos

    private Integer golesLocal = 0;
    private Integer golesVisitante = 0;

    // Para penales
    private Integer penalesLocal;
    private Integer penalesVisitante;

    @ManyToOne
    @JoinColumn(name = "ganador_id")
    private Equipo ganador;

    @OneToMany(mappedBy = "partido", cascade = CascadeType.ALL)
    private List<EventoPartido> eventos = new ArrayList<>();

    @OneToMany(mappedBy = "partido", cascade = CascadeType.ALL)
    private List<JugadorPartido> jugadoresPartido = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;

    public enum EstadoPartido {
        PROGRAMADO,
        EN_CURSO,
        FINALIZADO,
        SUSPENDIDO,
        POSPUESTO,
        CANCELADO
    }

    public enum FasePartido {
        GRUPOS,
        OCTAVOS,
        CUARTOS,
        SEMIFINAL,
        TERCER_LUGAR,
        FINAL
    }
}