package com.jumder.ulloa.controller;

import com.jumder.ulloa.entity.Equipo;
import com.jumder.ulloa.entity.Jugador;
import com.jumder.ulloa.entity.Producto;
import com.jumder.ulloa.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    // ==================== REPORTES DE TORNEO ====================

    @GetMapping("/torneo/{torneoId}/resumen")
    public ResponseEntity<ReporteService.ResumenTorneo> obtenerResumenTorneo(@PathVariable Long torneoId) {
        ReporteService.ResumenTorneo resumen = reporteService.obtenerResumenTorneo(torneoId);
        return ResponseEntity.ok(resumen);
    }

    @GetMapping("/torneo/{torneoId}/tabla-posiciones")
    public ResponseEntity<List<Equipo>> obtenerTablaPosiciones(@PathVariable Long torneoId) {
        List<Equipo> tabla = reporteService.obtenerTablaPosiciones(torneoId);
        return ResponseEntity.ok(tabla);
    }

    @GetMapping("/torneo/{torneoId}/goleadores")
    public ResponseEntity<List<Jugador>> obtenerTablaGoleadores(@PathVariable Long torneoId) {
        List<Jugador> goleadores = reporteService.obtenerTablaGoleadores(torneoId);
        return ResponseEntity.ok(goleadores);
    }

    @GetMapping("/torneo/{torneoId}/jugadores-sin-pagar")
    public ResponseEntity<List<Jugador>> obtenerJugadoresSinPagar(@PathVariable Long torneoId) {
        List<Jugador> jugadores = reporteService.obtenerJugadoresSinPagarCarnet(torneoId);
        return ResponseEntity.ok(jugadores);
    }

    // ==================== REPORTES DE VENTAS ====================

    @GetMapping("/ventas/resumen")
    public ResponseEntity<ReporteService.ResumenVentas> obtenerResumenVentas(
            @RequestParam Long polideportivoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        ReporteService.ResumenVentas resumen = reporteService.obtenerResumenVentas(
                polideportivoId, fechaInicio, fechaFin);
        return ResponseEntity.ok(resumen);
    }

    @GetMapping("/inventario/bajo-stock/{polideportivoId}")
    public ResponseEntity<List<Producto>> obtenerProductosBajoStock(@PathVariable Long polideportivoId) {
        List<Producto> productos = reporteService.obtenerProductosBajoStock(polideportivoId);
        return ResponseEntity.ok(productos);
    }
}