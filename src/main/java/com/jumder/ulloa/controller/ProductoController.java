package com.jumder.ulloa.controller;

import com.jumder.ulloa.dto.ProductoDTO;
import com.jumder.ulloa.entity.Producto;
import com.jumder.ulloa.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listarTodos() {
        List<ProductoDTO> productos = productoService.listarTodos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/polideportivo/{polideportivoId}")
    public ResponseEntity<List<ProductoDTO>> listarPorPolideportivo(@PathVariable Long polideportivoId) {
        List<ProductoDTO> productos = productoService.listarPorPolideportivo(polideportivoId);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable Long id) {
        try {
            ProductoDTO producto = productoService.obtenerPorId(id);
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@RequestBody ProductoDTO dto) {
        try {
            ProductoDTO nuevo = productoService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(
            @PathVariable Long id,
            @RequestBody ProductoDTO dto) {
        try {
            dto.setId(id);
            ProductoDTO actualizado = productoService.actualizar(dto);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            productoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/categoria/{categoria}/polideportivo/{polideportivoId}")
    public ResponseEntity<List<ProductoDTO>> listarPorCategoria(
            @PathVariable String categoria,
            @PathVariable Long polideportivoId) {
        List<ProductoDTO> productos = productoService.listarPorCategoria(categoria, polideportivoId);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/activos/polideportivo/{polideportivoId}")
    public ResponseEntity<List<ProductoDTO>> listarActivos(@PathVariable Long polideportivoId) {
        List<ProductoDTO> productos = productoService.listarActivos(polideportivoId);
        return ResponseEntity.ok(productos);
    }
}