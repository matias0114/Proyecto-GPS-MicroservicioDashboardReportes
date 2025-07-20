package com.ProyectoGPS.Backend.controller;

import com.ProyectoGPS.Backend.dto.*;
import com.ProyectoGPS.Backend.model.Product;
import com.ProyectoGPS.Backend.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/metricas")
    public ResponseEntity<DashboardDTO> getDashboardMetricas() {
        try {
            DashboardDTO dashboard = reporteService.getDashboardData();
            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // REPORTES EN JSON
    @GetMapping("/reportes/facturas")
    public ResponseEntity<List<FacturaReporteDTO>> getReporteFacturas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(required = false) String proveedor) {
        
        try {
            ReporteRequest request = new ReporteRequest(fechaInicio, fechaFin);
            request.setProveedor(proveedor);
            
            List<FacturaReporteDTO> facturas = reporteService.getReporteFacturas(request);
            return ResponseEntity.ok(facturas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/reportes/productos-sin-movimiento")
    public ResponseEntity<List<Product>> getProductosSinMovimiento(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) String categoria) {
        
        try {
            ReporteRequest request = new ReporteRequest();
            request.setFechaInicio(fechaInicio);
            request.setCategoria(categoria);
            
            List<Product> productos = reporteService.getProductosSinMovimiento(request);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/reportes/productos-con-movimiento")
    public ResponseEntity<List<ProductoMovimientoDTO>> getProductosConMovimiento(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) String categoria) {
        
        try {
            ReporteRequest request = new ReporteRequest();
            request.setFechaInicio(fechaInicio);
            request.setCategoria(categoria);
            
            List<ProductoMovimientoDTO> productos = reporteService.getProductosConMayorMovimiento(request);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // EXPORTAR A EXCEL
    @GetMapping("/exportar/facturas-excel")
    public ResponseEntity<byte[]> exportarFacturasExcel(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(required = false) String proveedor) {
        
        try {
            ReporteRequest request = new ReporteRequest(fechaInicio, fechaFin);
            request.setProveedor(proveedor);
            
            byte[] excelData = reporteService.exportarFacturasExcel(request);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "reporte_facturas.xlsx");
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
                
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/exportar/productos-sin-movimiento-excel")
    public ResponseEntity<byte[]> exportarProductosSinMovimientoExcel(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) String categoria) {
        
        try {
            ReporteRequest request = new ReporteRequest();
            request.setFechaInicio(fechaInicio);
            request.setCategoria(categoria);
            
            byte[] excelData = reporteService.exportarProductosSinMovimientoExcel(request);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "productos_sin_movimiento.xlsx");
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
                
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/exportar/productos-con-movimiento-excel")
    public ResponseEntity<byte[]> exportarProductosConMovimientoExcel(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) String categoria) {
        
        try {
            ReporteRequest request = new ReporteRequest();
            request.setFechaInicio(fechaInicio);
            request.setCategoria(categoria);
            
            byte[] excelData = reporteService.exportarProductosConMovimientoExcel(request);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "productos_con_mayor_movimiento.xlsx");
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
                
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ENDPOINT PARA HISTÓRICO DE VENTAS/FACTURAS
    @GetMapping("/historico/facturas")
    public ResponseEntity<List<Object[]>> getHistoricoFacturas(
            @RequestParam(defaultValue = "12") int meses) {
        
        try {
            // Reutilizamos el método del servicio para obtener datos agrupados por mes
            DashboardDTO dashboard = reporteService.getDashboardData();
            List<Object[]> historico = dashboard.getFacturasUltimos12Meses();
            
            return ResponseEntity.ok(historico);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
