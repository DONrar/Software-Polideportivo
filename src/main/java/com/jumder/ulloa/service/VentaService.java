package com.jumder.ulloa.service;


import com.jumder.ulloa.Polideportivo;
import com.jumder.ulloa.dto.DetalleVentaDTO;
import com.jumder.ulloa.dto.VentaDTO;
import com.jumder.ulloa.entity.*;
import com.jumder.ulloa.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final PartidoRepository partidoRepository;
    private final PolideportivoRepository polideportivoRepository;

    // ACTUALIZACIÓN en VentaService - Método registrarVenta
    @Transactional
    public VentaDTO registrarVenta(VentaDTO dto) {
        // Validaciones
        Usuario cajero = usuarioRepository.findById(Long.parseLong(dto.getCajeroNombre()))
                .orElseThrow(() -> new RuntimeException("Cajero no encontrado"));

        // ✅ AHORA USA EL polideportivoId DEL DTO
        Polideportivo polideportivo = polideportivoRepository.findById(dto.getPolideportivoId())
                .orElseThrow(() -> new RuntimeException("Polideportivo no encontrado"));

        // Crear venta
        Venta venta = new Venta();
        venta.setNumeroVenta(generarNumeroVenta());
        venta.setPolideportivo(polideportivo);
        venta.setCajero(cajero);
        venta.setNombreCliente(dto.getClienteNombre());
        venta.setMetodoPago(Venta.MetodoPago.valueOf(dto.getMetodoPago()));
        venta.setEstado(Venta.EstadoVenta.valueOf(dto.getEstado()));
        venta.setPagada(dto.getPagada());

        if (dto.getPartidoId() != null) {
            Partido partido = partidoRepository.findById(dto.getPartidoId())
                    .orElseThrow(() -> new RuntimeException("Partido no encontrado"));
            venta.setPartido(partido);
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal descuento = dto.getDescuento() != null ? dto.getDescuento() : BigDecimal.ZERO;

        // Crear detalles de venta
        List<DetalleVenta> detalles = new ArrayList<>();
        for (DetalleVentaDTO detalleDTO : dto.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalleDTO.getProductoId()));

            // Validar stock
            if (producto.getStock() < detalleDTO.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());

            BigDecimal subtotalDetalle = producto.getPrecio().multiply(BigDecimal.valueOf(detalleDTO.getCantidad()));
            detalle.setSubtotal(subtotalDetalle);
            detalle.setDescuento(BigDecimal.ZERO);
            detalle.setTotal(subtotalDetalle);

            detalles.add(detalle);
            subtotal = subtotal.add(subtotalDetalle);

            // Actualizar stock
            producto.setStock(producto.getStock() - detalleDTO.getCantidad());
            productoRepository.save(producto);

            // Registrar movimiento de inventario
            registrarMovimientoInventario(producto, detalleDTO.getCantidad(), cajero, venta);
        }

        venta.setSubtotal(subtotal);
        venta.setDescuento(descuento);
        venta.setTotal(subtotal.subtract(descuento));
        venta.setDetalles(detalles);

        venta = ventaRepository.save(venta);

        return convertirADTO(venta);
    }


    private void registrarMovimientoInventario(Producto producto, Integer cantidad, Usuario usuario, Venta venta) {
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setTipo(MovimientoInventario.TipoMovimiento.SALIDA_VENTA);
        movimiento.setCantidad(cantidad);
        movimiento.setStockAnterior(producto.getStock() + cantidad);
        movimiento.setStockNuevo(producto.getStock());
        movimiento.setUsuario(usuario);
        movimiento.setVenta(venta);
        movimientoInventarioRepository.save(movimiento);
    }

    private String generarNumeroVenta() {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long count = ventaRepository.count() + 1;
        return String.format("V-%s-%05d", fecha, count);
    }

    @Transactional
    public void registrarEntradaInventario(Long productoId, Integer cantidad, BigDecimal costoUnitario, Long usuarioId) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Integer stockAnterior = producto.getStock();
        producto.setStock(stockAnterior + cantidad);

        if (costoUnitario != null) {
            producto.setCosto(costoUnitario);
        }

        productoRepository.save(producto);

        // Registrar movimiento
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setTipo(MovimientoInventario.TipoMovimiento.ENTRADA_COMPRA);
        movimiento.setCantidad(cantidad);
        movimiento.setStockAnterior(stockAnterior);
        movimiento.setStockNuevo(producto.getStock());
        movimiento.setCostoUnitario(costoUnitario);
        movimiento.setCostoTotal(costoUnitario != null ? costoUnitario.multiply(BigDecimal.valueOf(cantidad)) : null);
        movimiento.setUsuario(usuario);
        movimientoInventarioRepository.save(movimiento);
    }

    public List<VentaDTO> listarVentasPorFecha(Long polideportivoId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.findVentasPorFecha(polideportivoId, fechaInicio, fechaFin)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public VentaDTO obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }

    public List<Producto> listarProductosBajoStock(Long polideportivoId) {
        return productoRepository.findProductosBajoStock(polideportivoId);
    }
    // El método convertirADTO también debe incluir polideportivoId
    private VentaDTO convertirADTO(Venta venta) {
        VentaDTO dto = new VentaDTO();
        dto.setId(venta.getId());
        dto.setNumeroVenta(venta.getNumeroVenta());
        dto.setCajeroNombre(venta.getCajero().getNombreCompleto());
        dto.setClienteNombre(venta.getNombreCliente());
        dto.setSubtotal(venta.getSubtotal());
        dto.setDescuento(venta.getDescuento());
        dto.setTotal(venta.getTotal());
        dto.setMetodoPago(venta.getMetodoPago().name());
        dto.setEstado(venta.getEstado().name());
        dto.setPagada(venta.getPagada());
        dto.setFechaVenta(venta.getFechaVenta());
        dto.setPolideportivoId(venta.getPolideportivo().getId());  // ✅ AGREGADO

        if (venta.getPartido() != null) {
            dto.setPartidoId(venta.getPartido().getId());
        }

        List<DetalleVentaDTO> detallesDTO = venta.getDetalles().stream()
                .map(detalle -> {
                    DetalleVentaDTO detalleDTO = new DetalleVentaDTO();
                    detalleDTO.setProductoId(detalle.getProducto().getId());
                    detalleDTO.setProductoNombre(detalle.getProducto().getNombre());
                    detalleDTO.setCantidad(detalle.getCantidad());
                    detalleDTO.setPrecioUnitario(detalle.getPrecioUnitario());
                    detalleDTO.setSubtotal(detalle.getSubtotal());
                    detalleDTO.setDescuento(detalle.getDescuento());
                    detalleDTO.setTotal(detalle.getTotal());
                    return detalleDTO;
                })
                .collect(Collectors.toList());

        dto.setDetalles(detallesDTO);

        return dto;
    }
}