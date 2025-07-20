package com.ProyectoGPS.Backend.dto;

import java.math.BigDecimal;

public class ProductoMovimientoDTO {
    private Long productoId;
    private String codigo;
    private String nombre;
    private String categoria;
    private String laboratorio;
    private Long totalMovimiento;
    private BigDecimal montoTotal;
    private Boolean activo;

    // Constructor
    public ProductoMovimientoDTO() {}

    public ProductoMovimientoDTO(Long productoId, String codigo, String nombre, String categoria, 
                               String laboratorio, Long totalMovimiento, BigDecimal montoTotal, Boolean activo) {
        this.productoId = productoId;
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.laboratorio = laboratorio;
        this.totalMovimiento = totalMovimiento;
        this.montoTotal = montoTotal;
        this.activo = activo;
    }

    // Getters y Setters
    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public Long getTotalMovimiento() {
        return totalMovimiento;
    }

    public void setTotalMovimiento(Long totalMovimiento) {
        this.totalMovimiento = totalMovimiento;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
