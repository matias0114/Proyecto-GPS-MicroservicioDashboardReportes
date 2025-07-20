package com.ProyectoGPS.Backend.service;

import com.ProyectoGPS.Backend.dto.*;
import com.ProyectoGPS.Backend.model.*;
import com.ProyectoGPS.Backend.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private FacturaRepository facturaRepository;
    
    @Autowired
    private CompraRepository compraRepository;
    
    @Autowired
    private ProductRepository productRepository;

    public DashboardDTO getDashboardData() {
        DashboardDTO dashboard = new DashboardDTO();
        
        // Fechas para cálculos
        LocalDateTime inicioMes = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finMes = inicioMes.plusMonths(1).minusSeconds(1);
        LocalDateTime hace12Meses = LocalDateTime.now().minusMonths(12);
        LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);
        
        // Métricas de facturas
        dashboard.setTotalFacturas(facturaRepository.count());
        dashboard.setFacturasMesActual(facturaRepository.findByFechaEmisionBetween(inicioMes, finMes).size());
        
        BigDecimal montoTotal = facturaRepository.sumMontoTotalByFechaEmisionBetween(hace12Meses, LocalDateTime.now());
        dashboard.setMontoTotalFacturas(montoTotal != null ? montoTotal : BigDecimal.ZERO);
        
        BigDecimal montoMes = facturaRepository.sumMontoTotalByFechaEmisionBetween(inicioMes, finMes);
        dashboard.setMontoFacturasMesActual(montoMes != null ? montoMes : BigDecimal.ZERO);
        
        // Métricas de compras
        dashboard.setTotalCompras(compraRepository.count());
        dashboard.setComprasPendientes(compraRepository.findByEstado(Compra.EstadoCompra.PENDIENTE).size());
        dashboard.setComprasRecibidas(compraRepository.findByEstado(Compra.EstadoCompra.RECIBIDA).size());
        
        BigDecimal montoCompras = compraRepository.sumMontoTotalByFechaCompraBetween(hace12Meses, LocalDateTime.now());
        dashboard.setMontoTotalCompras(montoCompras != null ? montoCompras : BigDecimal.ZERO);
        
        // Métricas de productos
        dashboard.setTotalProductos(productRepository.count());
        dashboard.setProductosActivos(productRepository.countActiveProducts());
        dashboard.setProductosSinMovimiento(productRepository.findProductsWithoutMovement(hace30Dias).size());
        
        // Datos adicionales para gráficos
        dashboard.setFacturasUltimos12Meses(facturaRepository.findFacturasGroupedByMonth(hace12Meses));
        
        List<Object[]> productosConMovimiento = productRepository.findProductsWithMostMovement(hace30Dias);
        List<ProductoMovimientoDTO> productosDTO = productosConMovimiento.stream()
            .limit(10) // Top 10
            .map(obj -> {
                Product p = (Product) obj[0];
                Long movimiento = (Long) obj[1];
                return new ProductoMovimientoDTO(p.getId(), p.getCode(), p.getName(), 
                    p.getCategory(), p.getLaboratory(), movimiento, p.getBasePrice(), p.getActive());
            })
            .collect(Collectors.toList());
        dashboard.setProductosConMayorMovimiento(productosDTO);
        
        return dashboard;
    }

    public List<FacturaReporteDTO> getReporteFacturas(ReporteRequest request) {
        List<Factura> facturas;
        
        if (request.getFechaInicio() != null && request.getFechaFin() != null) {
            facturas = facturaRepository.findByFechaEmisionBetween(request.getFechaInicio(), request.getFechaFin());
        } else {
            // Por defecto, últimos 30 días
            LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);
            facturas = facturaRepository.findByFechaEmisionBetween(hace30Dias, LocalDateTime.now());
        }
        
        if (request.getProveedor() != null && !request.getProveedor().isEmpty()) {
            facturas = facturas.stream()
                .filter(f -> f.getCompra().getProveedor().toLowerCase()
                    .contains(request.getProveedor().toLowerCase()))
                .collect(Collectors.toList());
        }
        
        return facturas.stream()
            .map(f -> new FacturaReporteDTO(f.getId(), f.getNumeroFactura(), f.getFechaEmision(),
                f.getMontoTotal(), f.getRutProveedor(), f.getCompra().getProveedor(), f.getCompra().getId()))
            .collect(Collectors.toList());
    }

    public List<Product> getProductosSinMovimiento(ReporteRequest request) {
        LocalDateTime fechaLimite = request.getFechaInicio() != null ? 
            request.getFechaInicio() : LocalDateTime.now().minusDays(30);
            
        List<Product> productos = productRepository.findProductsWithoutMovement(fechaLimite);
        
        if (request.getCategoria() != null && !request.getCategoria().isEmpty()) {
            productos = productos.stream()
                .filter(p -> p.getCategory() != null && 
                    p.getCategory().toLowerCase().contains(request.getCategoria().toLowerCase()))
                .collect(Collectors.toList());
        }
        
        return productos;
    }

    public List<ProductoMovimientoDTO> getProductosConMayorMovimiento(ReporteRequest request) {
        LocalDateTime fechaLimite = request.getFechaInicio() != null ? 
            request.getFechaInicio() : LocalDateTime.now().minusDays(30);
            
        List<Object[]> resultados = productRepository.findProductsWithMostMovement(fechaLimite);
        
        return resultados.stream()
            .limit(50) // Top 50
            .map(obj -> {
                Product p = (Product) obj[0];
                Long movimiento = (Long) obj[1];
                return new ProductoMovimientoDTO(p.getId(), p.getCode(), p.getName(), 
                    p.getCategory(), p.getLaboratory(), movimiento, p.getBasePrice(), p.getActive());
            })
            .collect(Collectors.toList());
    }

    public byte[] exportarFacturasExcel(ReporteRequest request) throws IOException {
        List<FacturaReporteDTO> facturas = getReporteFacturas(request);
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reporte de Facturas");
        
        // Crear encabezados
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Número Factura", "Fecha Emisión", "Monto Total", "RUT Proveedor", "Proveedor", "Compra ID"};
        
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Llenar datos
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        int rowNum = 1;
        
        for (FacturaReporteDTO factura : facturas) {
            Row row = sheet.createRow(rowNum++);
            
            row.createCell(0).setCellValue(factura.getId());
            row.createCell(1).setCellValue(factura.getNumeroFactura());
            row.createCell(2).setCellValue(factura.getFechaEmision().format(formatter));
            row.createCell(3).setCellValue(factura.getMontoTotal().doubleValue());
            row.createCell(4).setCellValue(factura.getRutProveedor());
            row.createCell(5).setCellValue(factura.getProveedor());
            row.createCell(6).setCellValue(factura.getCompraId());
        }
        
        // Ajustar ancho de columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        return outputStream.toByteArray();
    }

    public byte[] exportarProductosSinMovimientoExcel(ReporteRequest request) throws IOException {
        List<Product> productos = getProductosSinMovimiento(request);
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Productos Sin Movimiento");
        
        // Crear encabezados
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Código", "Nombre", "Descripción", "Categoría", "Laboratorio", "Precio Base", "Activo"};
        
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Llenar datos
        int rowNum = 1;
        
        for (Product producto : productos) {
            Row row = sheet.createRow(rowNum++);
            
            row.createCell(0).setCellValue(producto.getId());
            row.createCell(1).setCellValue(producto.getCode());
            row.createCell(2).setCellValue(producto.getName());
            row.createCell(3).setCellValue(producto.getDescription() != null ? producto.getDescription() : "");
            row.createCell(4).setCellValue(producto.getCategory() != null ? producto.getCategory() : "");
            row.createCell(5).setCellValue(producto.getLaboratory() != null ? producto.getLaboratory() : "");
            row.createCell(6).setCellValue(producto.getBasePrice() != null ? producto.getBasePrice().doubleValue() : 0.0);
            row.createCell(7).setCellValue(producto.getActive() ? "Sí" : "No");
        }
        
        // Ajustar ancho de columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        return outputStream.toByteArray();
    }

    public byte[] exportarProductosConMovimientoExcel(ReporteRequest request) throws IOException {
        List<ProductoMovimientoDTO> productos = getProductosConMayorMovimiento(request);
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Productos Con Mayor Movimiento");
        
        // Crear encabezados
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Código", "Nombre", "Categoría", "Laboratorio", "Total Movimiento", "Precio Base", "Activo"};
        
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Llenar datos
        int rowNum = 1;
        
        for (ProductoMovimientoDTO producto : productos) {
            Row row = sheet.createRow(rowNum++);
            
            row.createCell(0).setCellValue(producto.getProductoId());
            row.createCell(1).setCellValue(producto.getCodigo());
            row.createCell(2).setCellValue(producto.getNombre());
            row.createCell(3).setCellValue(producto.getCategoria() != null ? producto.getCategoria() : "");
            row.createCell(4).setCellValue(producto.getLaboratorio() != null ? producto.getLaboratorio() : "");
            row.createCell(5).setCellValue(producto.getTotalMovimiento());
            row.createCell(6).setCellValue(producto.getMontoTotal() != null ? producto.getMontoTotal().doubleValue() : 0.0);
            row.createCell(7).setCellValue(producto.getActivo() ? "Sí" : "No");
        }
        
        // Ajustar ancho de columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        return outputStream.toByteArray();
    }
}
