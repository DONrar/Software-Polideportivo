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
public class VentaDTO {
    private Long id;
    private String numeroVenta;
    private String cajeroNombre;  // Se envía el ID como string desde el front
    private String clienteNombre;
    private List<DetalleVentaDTO> detalles;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal total;
    private String metodoPago;
    private String estado;
    private Boolean pagada;
    private Long partidoId;
    private Long polideportivoId;  // ✅ AGREGADO
    private LocalDateTime fechaVenta;
}