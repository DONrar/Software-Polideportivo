package com.jumder.ulloa.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JugadorDTO {
    private Long id;
    private String nombreCompleto;
    private String cedula;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String email;
    private String foto;
    private Integer numeroCamiseta;
    private String posicion;
    private Long equipoId;
    private String equipoNombre;
    private Boolean carnetPagado;
    private Boolean verificado;
    private Integer goles;
    private Integer asistencias;
    private Integer tarjetasAmarillas;
    private Integer tarjetasRojas;
    private Integer partidosJugados;
}