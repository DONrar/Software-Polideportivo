package com.jumder.ulloa;

import com.jumder.ulloa.entity.Cancha;
import com.jumder.ulloa.entity.Torneo;
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
@Table(name = "polideportivos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Polideportivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String direccion;

    private String ciudad;
    private String departamento;
    private String telefono;
    private String email;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private Integer capacidad;

    @Column(nullable = false)
    private Boolean activo = true;

    @OneToMany(mappedBy = "polideportivo", cascade = CascadeType.ALL)
    private List<Cancha> canchas = new ArrayList<>();

    @OneToMany(mappedBy = "polideportivo", cascade = CascadeType.ALL)
    private List<Torneo> torneos = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;
}