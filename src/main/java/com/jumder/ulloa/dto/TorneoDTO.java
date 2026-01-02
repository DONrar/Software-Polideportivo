package com.jumder.ulloa.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// ==================== TORNEO DTO ====================
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TorneoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    private String tipo;
    private Integer numeroEquipos;
    private Integer jugadoresPorEquipo;
    private BigDecimal valorCarnet;
    private BigDecimal valorInscripcionEquipo;
    private Long polideportivoId;
    private String polideportivoNombre;
    private Integer equiposInscritos;
    private Integer jugadoresRegistrados;
}