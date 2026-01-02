package com.jumder.ulloa.controller;

import com.jumder.ulloa.dto.PartidoDTO;
import com.jumder.ulloa.entity.EventoPartido;
import com.jumder.ulloa.service.PartidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/partidos")
@RequiredArgsConstructor
public class PartidoController {

    private final PartidoService partidoService;

    @GetMapping("/{id}")
    public ResponseEntity<PartidoDTO> obtenerPartido(@PathVariable Long id) {
        try {
            PartidoDTO partido = partidoService.obtenerPartidoPorId(id);
            return ResponseEntity.ok(partido);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/torneo/{torneoId}")
    public ResponseEntity<List<PartidoDTO>> listarPartidosPorTorneo(@PathVariable Long torneoId) {
        List<PartidoDTO> partidos = partidoService.listarPartidosPorTorneo(torneoId);
        return ResponseEntity.ok(partidos);
    }

    @PostMapping("/{partidoId}/gol")
    public ResponseEntity<Void> registrarGol(
            @PathVariable Long partidoId,
            @RequestBody Map<String, Object> request) {
        try {
            Long jugadorId = Long.valueOf(request.get("jugadorId").toString());
            Integer minuto = Integer.valueOf(request.get("minuto").toString());
            Long jugadorAsistenciaId = request.get("jugadorAsistenciaId") != null
                    ? Long.valueOf(request.get("jugadorAsistenciaId").toString())
                    : null;

            partidoService.registrarGol(partidoId, jugadorId, minuto, jugadorAsistenciaId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/{partidoId}/tarjeta")
    public ResponseEntity<Void> registrarTarjeta(
            @PathVariable Long partidoId,
            @RequestBody Map<String, Object> request) {
        try {
            Long jugadorId = Long.valueOf(request.get("jugadorId").toString());
            Integer minuto = Integer.valueOf(request.get("minuto").toString());
            String tipoTarjeta = request.get("tipoTarjeta").toString();

            EventoPartido.TipoEvento tipo = tipoTarjeta.equals("AMARILLA")
                    ? EventoPartido.TipoEvento.TARJETA_AMARILLA
                    : EventoPartido.TipoEvento.TARJETA_ROJA;

            partidoService.registrarTarjeta(partidoId, jugadorId, minuto, tipo);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/{partidoId}/verificar-carnet/{jugadorId}")
    public ResponseEntity<Void> verificarCarnet(
            @PathVariable Long partidoId,
            @PathVariable Long jugadorId) {
        try {
            partidoService.verificarCarnetJugador(partidoId, jugadorId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/{partidoId}/finalizar")
    public ResponseEntity<Void> finalizarPartido(@PathVariable Long partidoId) {
        try {
            partidoService.finalizarPartido(partidoId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}