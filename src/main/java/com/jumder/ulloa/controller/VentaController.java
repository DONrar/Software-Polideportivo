package com.jumder.ulloa.controller;

import com.jumder.ulloa.dto.VentaDTO;
import com.jumder.ulloa.entity.Producto;
import com.jumder.ulloa.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @PostMapping
    public ResponseEntity<VentaDTO> registrarVenta(@RequestBody VentaDTO ventaDTO) {
        try {
            VentaDTO nuevaVenta = ventaService.registrarVenta(ventaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaVenta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> obtenerVenta(@PathVariable Long id) {
        try {
            VentaDTO venta = ventaService.obtenerVentaPorId(id);
            return ResponseEntity.ok(venta);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/polideportivo/{polideportivoId}")
    public ResponseEntity<List<VentaDTO>> listarVentasPorFecha(
            @PathVariable Long polideportivoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<VentaDTO> ventas = ventaService.listarVentasPorFecha(polideportivoId, fechaInicio, fechaFin);
        return ResponseEntity.ok(ventas);
    }

    @PostMapping("/inventario/entrada")
    public ResponseEntity<Void> registrarEntradaInventario(@RequestBody Map<String, Object> request) {
        try {
            Long productoId = Long.valueOf(request.get("productoId").toString());
            Integer cantidad = Integer.valueOf(request.get("cantidad").toString());
            BigDecimal costoUnitario = new BigDecimal(request.get("costoUnitario").toString());
            Long usuarioId = Long.valueOf(request.get("usuarioId").toString());

            ventaService.registrarEntradaInventario(productoId, cantidad, costoUnitario, usuarioId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/productos/bajo-stock/{polideportivoId}")
    public ResponseEntity<List<Producto>> listarProductosBajoStock(@PathVariable Long polideportivoId) {
        List<Producto> productos = ventaService.listarProductosBajoStock(polideportivoId);
        return ResponseEntity.ok(productos);
    }
}