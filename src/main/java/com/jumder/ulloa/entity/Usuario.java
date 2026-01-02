package com.jumder.ulloa.entity;

import com.jumder.ulloa.Polideportivo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nombreCompleto;

    @Column(unique = true)
    private String cedula;

    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolUsuario rol;

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "polideportivo_id")
    private Polideportivo polideportivo;

    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;

    public enum RolUsuario {
        SUPER_ADMIN,      // Administrador del sistema
        ADMIN_POLIDEPORTIVO, // Administrador del polideportivo
        ORGANIZADOR,      // Organizador de torneos
        CAJERO,          // Maneja punto de venta
        JUGADOR          // Jugador registrado
    }
}