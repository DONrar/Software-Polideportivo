package com.jumder.ulloa.service;

import com.jumder.ulloa.Polideportivo;
import com.jumder.ulloa.dto.ProductoDTO;
import com.jumder.ulloa.entity.Producto;
import com.jumder.ulloa.repository.ProductoRepository;
import com.jumder.ulloa.repository.PolideportivoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final PolideportivoRepository polideportivoRepository;

    public List<ProductoDTO> listarTodos() {
        return productoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> listarPorPolideportivo(Long polideportivoId) {
        return productoRepository.findByPolideportivoId(polideportivoId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> listarActivos(Long polideportivoId) {
        return productoRepository.findByPolideportivoIdAndActivoTrue(polideportivoId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> listarPorCategoria(String categoria, Long polideportivoId) {
        // Convertir String a Enum
        Producto.CategoriaProducto categoriaEnum = Producto.CategoriaProducto.valueOf(categoria.toUpperCase());

        return productoRepository.findByPolideportivoIdAndCategoria(polideportivoId, categoriaEnum)
                .stream()
                .map(this::convertirADTO)  // ✅ Convierte Producto → ProductoDTO
                .collect(Collectors.toList());
    }

    public ProductoDTO obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @Transactional
    public ProductoDTO crear(ProductoDTO dto) {
        Polideportivo polideportivo = polideportivoRepository.findById(dto.getPolideportivoId())
                .orElseThrow(() -> new RuntimeException("Polideportivo no encontrado"));

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setCosto(dto.getCosto());
        producto.setStock(dto.getStock() != null ? dto.getStock() : 0);
        producto.setStockMinimo(dto.getStockMinimo() != null ? dto.getStockMinimo() : 5);
        producto.setCategoria(Producto.CategoriaProducto.valueOf(dto.getCategoria().toUpperCase()));
        producto.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        producto.setPolideportivo(polideportivo);

        producto = productoRepository.save(producto);
        return convertirADTO(producto);
    }

    @Transactional
    public ProductoDTO actualizar(ProductoDTO dto) {
        Producto producto = productoRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setCosto(dto.getCosto());
        producto.setStock(dto.getStock());
        producto.setStockMinimo(dto.getStockMinimo());
        producto.setCategoria(Producto.CategoriaProducto.valueOf(dto.getCategoria().toUpperCase()));
        producto.setActivo(dto.getActivo());

        producto = productoRepository.save(producto);
        return convertirADTO(producto);
    }

    @Transactional
    public void eliminar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    private ProductoDTO convertirADTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setCosto(producto.getCosto());
        dto.setStock(producto.getStock());
        dto.setStockMinimo(producto.getStockMinimo());
        dto.setCategoria(producto.getCategoria().name());
        dto.setActivo(producto.getActivo());
        dto.setPolideportivoId(producto.getPolideportivo().getId());
        return dto;
    }
}