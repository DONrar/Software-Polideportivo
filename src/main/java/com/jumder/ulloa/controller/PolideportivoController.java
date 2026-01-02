package com.jumder.ulloa.controller;

import com.jumder.ulloa.dto.PolideportivoDTO;
import com.jumder.ulloa.service.PolideportivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/polideportivos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PolideportivoController {

    private final PolideportivoService polideportivoService;

    @GetMapping
    public ResponseEntity<List<PolideportivoDTO>> listarTodos() {
        List<PolideportivoDTO> polideportivos = polideportivoService.listarTodos();
        return ResponseEntity.ok(polideportivos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PolideportivoDTO> obtenerPorId(@PathVariable Long id) {
        try {
            PolideportivoDTO polideportivo = polideportivoService.obtenerPorId(id);
            return ResponseEntity.ok(polideportivo);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PolideportivoDTO> crear(@RequestBody PolideportivoDTO dto) {
        try {
            PolideportivoDTO nuevo = polideportivoService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PolideportivoDTO> actualizar(
            @PathVariable Long id,
            @RequestBody PolideportivoDTO dto) {
        try {
            dto.setId(id);
            PolideportivoDTO actualizado = polideportivoService.actualizar(dto);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            polideportivoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
