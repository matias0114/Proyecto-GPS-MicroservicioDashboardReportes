package com.ProyectoGPS.Backend.Metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.ProyectoGPS.Backend.repository.CompraRepository;
import com.ProyectoGPS.Backend.model.Compra;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class CompraMetrics {

    private final CompraRepository compraRepository;
    private final AtomicLong totalCompras;
    private final AtomicLong comprasPendientes;
    private final AtomicLong comprasRecibidas;
    private final AtomicReference<BigDecimal> montoTotalCompras;
    
    public CompraMetrics(CompraRepository compraRepository, MeterRegistry meterRegistry) {
        this.compraRepository = compraRepository;
        this.totalCompras = new AtomicLong(0);
        this.comprasPendientes = new AtomicLong(0);
        this.comprasRecibidas = new AtomicLong(0);
        this.montoTotalCompras = new AtomicReference<>(BigDecimal.ZERO);
        
        // Registro de métricas
        Gauge.builder("miapp_compras_totales", totalCompras, AtomicLong::get)
                .description("Cantidad total de compras en la base de datos")
                .register(meterRegistry);
                
        Gauge.builder("miapp_compras_pendientes", comprasPendientes, AtomicLong::get)
                .description("Cantidad de compras pendientes")
                .register(meterRegistry);
                
        Gauge.builder("miapp_compras_recibidas", comprasRecibidas, AtomicLong::get)
                .description("Cantidad de compras recibidas")
                .register(meterRegistry);
                
        Gauge.builder("miapp_monto_total_compras", montoTotalCompras, 
                ref -> ref.get().doubleValue())
                .description("Monto total de compras de los últimos 12 meses")
                .register(meterRegistry);
    }

    @Scheduled(fixedRate = 45000) // Cada 45 segundos
    public void actualizarComprasMetricas() {
        // Total de compras
        long total = compraRepository.count();
        totalCompras.set(total);
        
        // Compras por estado
        long pendientes = compraRepository.findByEstado(Compra.EstadoCompra.PENDIENTE).size();
        comprasPendientes.set(pendientes);
        
        long recibidas = compraRepository.findByEstado(Compra.EstadoCompra.RECIBIDA).size();
        comprasRecibidas.set(recibidas);
        
        // Monto total de compras (últimos 12 meses)
        LocalDateTime hace12Meses = LocalDateTime.now().minusMonths(12);
        BigDecimal montoTotal = compraRepository.sumMontoTotalByFechaCompraBetween(hace12Meses, LocalDateTime.now());
        montoTotalCompras.set(montoTotal != null ? montoTotal : BigDecimal.ZERO);
    }
}
