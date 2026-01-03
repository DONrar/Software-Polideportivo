package com.jumder.ulloa.controller;

import com.jumder.ulloa.dto.JugadorDTO;
import com.jumder.ulloa.service.JugadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jugadores")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class JugadorController {

    private final JugadorService jugadorService;

    @GetMapping
    public ResponseEntity<List<JugadorDTO>> listarTodos() {
        List<JugadorDTO> jugadores = jugadorService.listarTodos();
        return ResponseEntity.ok(jugadores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JugadorDTO> obtenerPorId(@PathVariable Long id) {
        try {
            JugadorDTO jugador = jugadorService.obtenerPorId(id);
            return ResponseEntity.ok(jugador);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/equipo/{equipoId}")
    public ResponseEntity<List<JugadorDTO>> listarPorEquipo(@PathVariable Long equipoId) {
        List<JugadorDTO> jugadores = jugadorService.listarPorEquipo(equipoId);
        return ResponseEntity.ok(jugadores);
    }

    @GetMapping("/equipo/{equipoId}/activos")
    public ResponseEntity<List<JugadorDTO>> listarActivosPorEquipo(@PathVariable Long equipoId) {
        List<JugadorDTO> jugadores = jugadorService.listarActivosPorEquipo(equipoId);
        return ResponseEntity.ok(jugadores);
    }

    @GetMapping("/torneo/{torneoId}")
    public ResponseEntity<List<JugadorDTO>> listarPorTorneo(@PathVariable Long torneoId) {
        List<JugadorDTO> jugadores = jugadorService.listarPorTorneo(torneoId);
        return ResponseEntity.ok(jugadores);
    }

    @GetMapping("/torneo/{torneoId}/sin-pagar")
    public ResponseEntity<List<JugadorDTO>> listarSinPagarPorTorneo(@PathVariable Long torneoId) {
        List<JugadorDTO> jugadores = jugadorService.listarSinPagarPorTorneo(torneoId);
        return ResponseEntity.ok(jugadores);
    }

    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<JugadorDTO> buscarPorCedula(@PathVariable String cedula) {
        try {
            JugadorDTO jugador = jugadorService.buscarPorCedula(cedula);
            return ResponseEntity.ok(jugador);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<JugadorDTO> crear(@RequestBody JugadorDTO jugadorDTO) {
        try {
            JugadorDTO nuevoJugador = jugadorService.crear(jugadorDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoJugador);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<JugadorDTO> actualizar(
            @PathVariable Long id,
            @RequestBody JugadorDTO jugadorDTO) {
        try {
            jugadorDTO.setId(id);
            JugadorDTO actualizado = jugadorService.actualizar(jugadorDTO);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            jugadorService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/pagar-carnet")
    public ResponseEntity<Void> pagarCarnet(@PathVariable Long id) {
        try {
            jugadorService.marcarCarnetPagado(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}/verificar")
    public ResponseEntity<Void> verificarJugador(@PathVariable Long id) {
        try {
            jugadorService.verificarJugador(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}/cambiar-equipo")
    public ResponseEntity<Void> cambiarEquipo(
            @PathVariable Long id,
            @RequestBody Map<String, Long> request) {
        try {
            Long nuevoEquipoId = request.get("equipoId");
            jugadorService.cambiarEquipo(id, nuevoEquipoId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}