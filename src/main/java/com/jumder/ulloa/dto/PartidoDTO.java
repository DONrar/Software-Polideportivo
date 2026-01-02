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
public class PartidoDTO {
    private Long id;
    private Long torneoId;
    private String torneoNombre;
    private Long equipoLocalId;
    private String equipoLocalNombre;
    private String equipoLocalLogo;
    private Long equipoVisitanteId;
    private String equipoVisitanteNombre;
    private String equipoVisitanteLogo;
    private Long canchaId;
    private String canchaNombre;
    private LocalDateTime fechaHora;
    private String estado;
    private String fase;
    private Long grupoId;
    private String grupoNombre;
    private Integer golesLocal;
    private Integer golesVisitante;
    private Integer penalesLocal;
    private Integer penalesVisitante;
    private Long ganadorId;
    private String ganadorNombre;
}