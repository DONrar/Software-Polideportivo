package com.jumder.ulloa.service;

import com.jumder.ulloa.entity.*;
import com.jumder.ulloa.repository.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final TorneoRepository torneoRepository;
    private final EquipoRepository equipoRepository;
    private final JugadorRepository jugadorRepository;
    private final PartidoRepository partidoRepository;
    private final VentaRepository ventaRepository;
    private final PagoRepository pagoRepository;
    private final ProductoRepository productoRepository;
    private final DetalleVentaRepository detalleVentaRepository;

    // ==================== REPORTES DE TORNEO ====================

    public ResumenTorneo obtenerResumenTorneo(Long torneoId) {
        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));

        ResumenTorneo resumen = new ResumenTorneo();
        resumen.setTorneoId(torneoId);
        resumen.setNombreTorneo(torneo.getNombre());
        resumen.setEstado(torneo.getEstado().name());

        // Estadísticas de equipos
        List<Equipo> equipos = equipoRepository.findByTorneoId(torneoId);
        resumen.setTotalEquipos(equipos.size());
        resumen.setEquiposPagos((int) equipos.stream().filter(Equipo::getInscripcionPagada).count());
        resumen.setEquiposPendientes(equipos.size() - resumen.getEquiposPagos());

        // Estadísticas de jugadores
        List<Jugador> jugadores = jugadorRepository.findByEquipoTorneoId(torneoId);
        resumen.setTotalJugadores(jugadores.size());
        resumen.setJugadoresPagos((int) jugadores.stream().filter(Jugador::getCarnetPagado).count());
        resumen.setJugadoresPendientes(jugadores.size() - resumen.getJugadoresPagos());

        // Estadísticas de partidos
        List<Partido> partidos = partidoRepository.findByTorneoId(torneoId);
        resumen.setTotalPartidos(partidos.size());
        resumen.setPartidosJugados((int) partidos.stream()
                .filter(p -> p.getEstado() == Partido.EstadoPartido.FINALIZADO).count());
        resumen.setPartidosPendientes(partidos.size() - resumen.getPartidosJugados());

        // Ingresos
        BigDecimal ingresosCarnets = pagoRepository.calcularTotalPagosPorTipo(torneoId, Pago.TipoPago.CARNET_JUGADOR);
        BigDecimal ingresosInscripciones = pagoRepository.calcularTotalPagosPorTipo(torneoId, Pago.TipoPago.INSCRIPCION_EQUIPO);
        resumen.setIngresosCarnets(ingresosCarnets != null ? ingresosCarnets : BigDecimal.ZERO);
        resumen.setIngresosInscripciones(ingresosInscripciones != null ? ingresosInscripciones : BigDecimal.ZERO);
        resumen.setTotalIngresos(resumen.getIngresosCarnets().add(resumen.getIngresosInscripciones()));

        return resumen;
    }

    public List<Equipo> obtenerTablaPosiciones(Long torneoId) {
        return equipoRepository.findByTorneoIdOrderByPuntosDesc(torneoId);
    }

    public List<Jugador> obtenerTablaGoleadores(Long torneoId) {
        return jugadorRepository.findTopGoleadoresByTorneo(torneoId)
                .stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Jugador> obtenerJugadoresSinPagarCarnet(Long torneoId) {
        return jugadorRepository.findJugadoresSinPagarCarnetByTorneo(torneoId);
    }

    // ==================== REPORTES DE VENTAS ====================

    public ResumenVentas obtenerResumenVentas(Long polideportivoId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        ResumenVentas resumen = new ResumenVentas();

        List<Venta> ventas = ventaRepository.findVentasPorFecha(polideportivoId, fechaInicio, fechaFin);

        resumen.setTotalVentas(ventas.size());
        resumen.setVentasCompletadas((int) ventas.stream()
                .filter(v -> v.getEstado() == Venta.EstadoVenta.COMPLETADA).count());
        resumen.setVentasPendientes((int) ventas.stream()
                .filter(v -> v.getEstado() == Venta.EstadoVenta.PENDIENTE_PAGO).count());

        BigDecimal totalIngresos = ventas.stream()
                .filter(v -> v.getEstado() == Venta.EstadoVenta.COMPLETADA)
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        resumen.setTotalIngresos(totalIngresos);

        // Ingresos por método de pago
        Map<String, BigDecimal> ingresosPorMetodo = ventas.stream()
                .filter(v -> v.getEstado() == Venta.EstadoVenta.COMPLETADA)
                .collect(Collectors.groupingBy(
                        v -> v.getMetodoPago().name(),
                        Collectors.reducing(BigDecimal.ZERO, Venta::getTotal, BigDecimal::add)
                ));

        resumen.setIngresosPorMetodoPago(ingresosPorMetodo);

        // Productos más vendidos
        List<DetalleVenta> detalles = detalleVentaRepository.findByPolideportivoAndFecha(
                polideportivoId, fechaInicio, fechaFin);

        Map<String, Integer> productosMasVendidos = detalles.stream()
                .collect(Collectors.groupingBy(
                        d -> d.getProducto().getNombre(),
                        Collectors.summingInt(DetalleVenta::getCantidad)
                ));

        resumen.setProductosMasVendidos(productosMasVendidos);

        return resumen;
    }

    public List<Producto> obtenerProductosBajoStock(Long polideportivoId) {
        return productoRepository.findProductosBajoStock(polideportivoId);
    }

    // ==================== CLASES DE RESPUESTA ====================

    @Data
    public static class ResumenTorneo {
        private Long torneoId;
        private String nombreTorneo;
        private String estado;

        // Equipos
        private Integer totalEquipos;
        private Integer equiposPagos;
        private Integer equiposPendientes;

        // Jugadores
        private Integer totalJugadores;
        private Integer jugadoresPagos;
        private Integer jugadoresPendientes;

        // Partidos
        private Integer totalPartidos;
        private Integer partidosJugados;
        private Integer partidosPendientes;

        // Ingresos
        private BigDecimal ingresosCarnets;
        private BigDecimal ingresosInscripciones;
        private BigDecimal totalIngresos;
    }

    @Data
    public static class ResumenVentas {
        private Integer totalVentas;
        private Integer ventasCompletadas;
        private Integer ventasPendientes;
        private BigDecimal totalIngresos;
        private Map<String, BigDecimal> ingresosPorMetodoPago;
        private Map<String, Integer> productosMasVendidos;
    }
}