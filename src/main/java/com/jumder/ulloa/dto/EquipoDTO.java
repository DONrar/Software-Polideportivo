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
public class EquipoDTO {
    private Long id;
    private String nombre;
    private String logo;
    private String colorPrincipal;
    private String colorSecundario;
    private Long torneoId;
    private String torneoNombre;
    private Long grupoId;
    private String grupoNombre;
    private String capitanNombre;
    private Boolean inscripcionPagada;
    private Integer partidosJugados;
    private Integer partidosGanados;
    private Integer partidosEmpatados;
    private Integer partidosPerdidos;
    private Integer golesAFavor;
    private Integer golesEnContra;
    private Integer puntos;
    private Integer diferenciaGoles;
    private Integer cantidadJugadores;
}