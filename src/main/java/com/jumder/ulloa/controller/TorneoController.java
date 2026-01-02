package com.jumder.ulloa.controller;

import com.jumder.ulloa.dto.TorneoDTO;
import com.jumder.ulloa.service.TorneoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/torneos")
@RequiredArgsConstructor
public class TorneoController {

    private final TorneoService torneoService;

    @PostMapping
    public ResponseEntity<TorneoDTO> crearTorneo(@RequestBody TorneoDTO torneoDTO) {
        try {
            TorneoDTO nuevoTorneo = torneoService.crearTorneo(torneoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTorneo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TorneoDTO> obtenerTorneo(@PathVariable Long id) {
        try {
            TorneoDTO torneo = torneoService.obtenerTorneoPorId(id);
            return ResponseEntity.ok(torneo);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/polideportivo/{polideportivoId}")
    public ResponseEntity<List<TorneoDTO>> listarTorneosPorPolideportivo(@PathVariable Long polideportivoId) {
        List<TorneoDTO> torneos = torneoService.listarTorneosPorPolideportivo(polideportivoId);
        return ResponseEntity.ok(torneos);
    }

    @PostMapping("/{id}/generar-fixture")
    public ResponseEntity<Void> generarFixture(@PathVariable Long id) {
        try {
            torneoService.generarFixture(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TorneoDTO> actualizarTorneo(@PathVariable Long id, @RequestBody TorneoDTO torneoDTO) {
        try {
            torneoDTO.setId(id);
            // Implementar lógica de actualización
            return ResponseEntity.ok(torneoDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTorneo(@PathVariable Long id) {
        try {
            // Implementar lógica de eliminación (soft delete)
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}