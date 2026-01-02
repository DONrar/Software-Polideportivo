package com.jumder.ulloa.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "jugadores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreCompleto;

    @Column(nullable = false, unique = true)
    private String cedula;

    private LocalDate fechaNacimiento;

    private String telefono;
    private String email;

    private String foto; // URL o path de la foto del jugador

    private Integer numeroCamiseta;

    @Enumerated(EnumType.STRING)
    private Posicion posicion;

    @ManyToOne
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario; // Si el jugador tiene cuenta en el sistema

    @Column(nullable = false)
    private Boolean carnetPagado = false;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(nullable = false)
    private Boolean verificado = false; // Para evitar infiltrados

    // Estadísticas
    private Integer goles = 0;
    private Integer asistencias = 0;
    private Integer tarjetasAmarillas = 0;
    private Integer tarjetasRojas = 0;
    private Integer partidosJugados = 0;

    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;

    public enum Posicion {
        PORTERO,
        DEFENSA,
        MEDIOCAMPISTA,
        DELANTERO
    }
}