package com.jumder.ulloa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "jugadores_partido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JugadorPartido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "partido_id", nullable = false)
    private Partido partido;

    @ManyToOne
    @JoinColumn(name = "jugador_id", nullable = false)
    private Jugador jugador;

    @Column(nullable = false)
    private Boolean presente = false; // Para control de asistencia

    @Column(nullable = false)
    private Boolean carnetVerificado = false; // Para evitar infiltrados

    private Integer goles = 0;
    private Integer asistencias = 0;
    private Integer tarjetasAmarillas = 0;
    private Integer tarjetasRojas = 0;

    @Column(nullable = false)
    private Boolean titular = true;

    private Integer minutoEntrada;
    private Integer minutoSalida;

    @CreationTimestamp
    private LocalDateTime fechaRegistro;
}