package com.ProyectoGPS.Backend.dto;

import java.math.BigDecimal;
import java.util.List;

public class DashboardDTO {
    private long totalFacturas;
    private long facturasMesActual;
    private BigDecimal montoTotalFacturas;
    private BigDecimal montoFacturasMesActual;
    
    private long totalCompras;
    private long comprasPendientes;
    private long comprasRecibidas;
    private BigDecimal montoTotalCompras;
    
    private long totalProductos;
    private long productosActivos;
    private long productosSinMovimiento;
    
    private List<Object[]> facturasUltimos12Meses;
    private List<ProductoMovimientoDTO> productosConMayorMovimiento;

    // Constructor
    public DashboardDTO() {}

    // Getters y Setters
    public long getTotalFacturas() {
        return totalFacturas;
    }

    public void setTotalFacturas(long totalFacturas) {
        this.totalFacturas = totalFacturas;
    }

    public long getFacturasMesActual() {
        return facturasMesActual;
    }

    public void setFacturasMesActual(long facturasMesActual) {
        this.facturasMesActual = facturasMesActual;
    }

    public BigDecimal getMontoTotalFacturas() {
        return montoTotalFacturas;
    }

    public void setMontoTotalFacturas(BigDecimal montoTotalFacturas) {
        this.montoTotalFacturas = montoTotalFacturas;
    }

    public BigDecimal getMontoFacturasMesActual() {
        return montoFacturasMesActual;
    }

    public void setMontoFacturasMesActual(BigDecimal montoFacturasMesActual) {
        this.montoFacturasMesActual = montoFacturasMesActual;
    }

    public long getTotalCompras() {
        return totalCompras;
    }

    public void setTotalCompras(long totalCompras) {
        this.totalCompras = totalCompras;
    }

    public long getComprasPendientes() {
        return comprasPendientes;
    }

    public void setComprasPendientes(long comprasPendientes) {
        this.comprasPendientes = comprasPendientes;
    }

    public long getComprasRecibidas() {
        return comprasRecibidas;
    }

    public void setComprasRecibidas(long comprasRecibidas) {
        this.comprasRecibidas = comprasRecibidas;
    }

    public BigDecimal getMontoTotalCompras() {
        return montoTotalCompras;
    }

    public void setMontoTotalCompras(BigDecimal montoTotalCompras) {
        this.montoTotalCompras = montoTotalCompras;
    }

    public long getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(long totalProductos) {
        this.totalProductos = totalProductos;
    }

    public long getProductosActivos() {
        return productosActivos;
    }

    public void setProductosActivos(long productosActivos) {
        this.productosActivos = productosActivos;
    }

    public long getProductosSinMovimiento() {
        return productosSinMovimiento;
    }

    public void setProductosSinMovimiento(long productosSinMovimiento) {
        this.productosSinMovimiento = productosSinMovimiento;
    }

    public List<Object[]> getFacturasUltimos12Meses() {
        return facturasUltimos12Meses;
    }

    public void setFacturasUltimos12Meses(List<Object[]> facturasUltimos12Meses) {
        this.facturasUltimos12Meses = facturasUltimos12Meses;
    }

    public List<ProductoMovimientoDTO> getProductosConMayorMovimiento() {
        return productosConMayorMovimiento;
    }

    public void setProductosConMayorMovimiento(List<ProductoMovimientoDTO> productosConMayorMovimiento) {
        this.productosConMayorMovimiento = productosConMayorMovimiento;
    }
}
