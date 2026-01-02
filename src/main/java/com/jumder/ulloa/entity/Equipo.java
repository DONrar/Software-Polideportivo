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
@Table(name = "equipos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String logo; // URL o path de la imagen

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String colorPrincipal;
    private String colorSecundario;

    @ManyToOne
    @JoinColumn(name = "torneo_id", nullable = false)
    private Torneo torneo;

    @ManyToOne
    @JoinColumn(name = "capitan_id")
    private Jugador capitan;

    @ManyToOne
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;

    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL)
    private List<Jugador> jugadores = new ArrayList<>();

    @Column(nullable = false)
    private Boolean inscripcionPagada = false;

    private Integer partidosJugados = 0;
    private Integer partidosGanados = 0;
    private Integer partidosEmpatados = 0;
    private Integer partidosPerdidos = 0;
    private Integer golesAFavor = 0;
    private Integer golesEnContra = 0;
    private Integer puntos = 0;

    @Column(nullable = false)
    private Boolean activo = true;

    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;

    // Métodos útiles
    public Integer getDiferenciaGoles() {
        return golesAFavor - golesEnContra;
    }
}