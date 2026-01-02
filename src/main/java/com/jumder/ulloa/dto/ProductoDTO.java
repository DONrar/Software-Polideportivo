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
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private BigDecimal costo;
    private String categoria;
    private String codigoBarras;
    private String imagen;
    private Integer stock;
    private Integer stockMinimo;
    private Boolean activo;
}