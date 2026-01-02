package com.jumder.ulloa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "eventos_partido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoPartido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "partido_id", nullable = false)
    private Partido partido;

    @ManyToOne
    @JoinColumn(name = "jugador_id")
    private Jugador jugador;

    @ManyToOne
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEvento tipo;

    @Column(nullable = false)
    private Integer minuto;

    @ManyToOne
    @JoinColumn(name = "asistencia_jugador_id")
    private Jugador jugadorAsistencia; // Para goles con asistencia

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @CreationTimestamp
    private LocalDateTime fechaRegistro;

    public enum TipoEvento {
        GOL,
        TARJETA_AMARILLA,
        TARJETA_ROJA,
        SUSTITUCION,
        PENAL_ANOTADO,
        PENAL_FALLADO,
        AUTOGOL
    }
}