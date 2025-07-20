package com.ProyectoGPS.Backend.Metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.ProyectoGPS.Backend.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ProductMetrics {

    private final ProductRepository productRepository;
    private final AtomicLong totalProductos;
    private final AtomicLong productosActivos;
    private final AtomicLong productosSinMovimiento;
    
    public ProductMetrics(ProductRepository productRepository, MeterRegistry meterRegistry) {
        this.productRepository = productRepository;
        this.totalProductos = new AtomicLong(0);
        this.productosActivos = new AtomicLong(0);
        this.productosSinMovimiento = new AtomicLong(0);
        
        // Registro de métricas
        Gauge.builder("miapp_productos_totales", totalProductos, AtomicLong::get)
                .description("Cantidad total de productos en la base de datos")
                .register(meterRegistry);
                
        Gauge.builder("miapp_productos_activos", productosActivos, AtomicLong::get)
                .description("Cantidad de productos activos")
                .register(meterRegistry);
                
        Gauge.builder("miapp_productos_sin_movimiento", productosSinMovimiento, AtomicLong::get)
                .description("Cantidad de productos sin movimiento en los últimos 30 días")
                .register(meterRegistry);
    }

    @Scheduled(fixedRate = 60000) // Cada 60 segundos
    public void actualizarProductosMetricas() {
        // Total de productos
        long total = productRepository.count();
        totalProductos.set(total);
        
        // Productos activos
        long activos = productRepository.countActiveProducts();
        productosActivos.set(activos);
        
        // Productos sin movimiento en los últimos 30 días
        LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);
        long sinMovimiento = productRepository.findProductsWithoutMovement(hace30Dias).size();
        productosSinMovimiento.set(sinMovimiento);
    }
}
