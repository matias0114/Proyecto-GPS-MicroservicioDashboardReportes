package com.ProyectoGPS.Backend.Metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.ProyectoGPS.Backend.repository.FacturaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class FacturaMetrics {

    private final FacturaRepository facturaRepository;
    private final AtomicLong totalFacturas;
    private final AtomicLong facturasMesActual;
    private final AtomicReference<BigDecimal> montoTotalFacturas;
    private final AtomicReference<BigDecimal> montoFacturasMesActual;
    
    public FacturaMetrics(FacturaRepository facturaRepository, MeterRegistry meterRegistry) {
        this.facturaRepository = facturaRepository;
        this.totalFacturas = new AtomicLong(0);
        this.facturasMesActual = new AtomicLong(0);
        this.montoTotalFacturas = new AtomicReference<>(BigDecimal.ZERO);
        this.montoFacturasMesActual = new AtomicReference<>(BigDecimal.ZERO);
        
        // Registro de métricas
        Gauge.builder("miapp_facturas_totales", totalFacturas, AtomicLong::get)
                .description("Cantidad total de facturas en la base de datos")
                .register(meterRegistry);
                
        Gauge.builder("miapp_facturas_mes_actual", facturasMesActual, AtomicLong::get)
                .description("Cantidad de facturas del mes actual")
                .register(meterRegistry);
                
        Gauge.builder("miapp_monto_total_facturas", montoTotalFacturas, 
                ref -> ref.get().doubleValue())
                .description("Monto total de todas las facturas")
                .register(meterRegistry);
                
        Gauge.builder("miapp_monto_facturas_mes_actual", montoFacturasMesActual, 
                ref -> ref.get().doubleValue())
                .description("Monto total de facturas del mes actual")
                .register(meterRegistry);
    }

    @Scheduled(fixedRate = 30000) // Cada 30 segundos
    public void actualizarFacturasMetricas() {
        // Total de facturas
        long total = facturaRepository.count();
        totalFacturas.set(total);
        
        // Fecha inicio del mes actual
        LocalDateTime inicioMes = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finMes = inicioMes.plusMonths(1).minusSeconds(1);
        
        // Facturas del mes actual
        long facturasMes = facturaRepository.findByFechaEmisionBetween(inicioMes, finMes).size();
        facturasMesActual.set(facturasMes);
        
        // Monto total de facturas (últimos 12 meses para evitar sobrecarga)
        LocalDateTime hace12Meses = LocalDateTime.now().minusMonths(12);
        BigDecimal montoTotal = facturaRepository.sumMontoTotalByFechaEmisionBetween(hace12Meses, LocalDateTime.now());
        montoTotalFacturas.set(montoTotal != null ? montoTotal : BigDecimal.ZERO);
        
        // Monto facturas mes actual
        BigDecimal montoMes = facturaRepository.sumMontoTotalByFechaEmisionBetween(inicioMes, finMes);
        montoFacturasMesActual.set(montoMes != null ? montoMes : BigDecimal.ZERO);
    }
}
