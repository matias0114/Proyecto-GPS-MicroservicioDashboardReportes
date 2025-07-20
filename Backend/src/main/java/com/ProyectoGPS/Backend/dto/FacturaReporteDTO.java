package com.ProyectoGPS.Backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FacturaReporteDTO {
    private Long id;
    private String numeroFactura;
    private LocalDateTime fechaEmision;
    private BigDecimal montoTotal;
    private String rutProveedor;
    private String proveedor;
    private Long compraId;

    // Constructor
    public FacturaReporteDTO() {}

    public FacturaReporteDTO(Long id, String numeroFactura, LocalDateTime fechaEmision, 
                           BigDecimal montoTotal, String rutProveedor, String proveedor, Long compraId) {
        this.id = id;
        this.numeroFactura = numeroFactura;
        this.fechaEmision = fechaEmision;
        this.montoTotal = montoTotal;
        this.rutProveedor = rutProveedor;
        this.proveedor = proveedor;
        this.compraId = compraId;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getRutProveedor() {
        return rutProveedor;
    }

    public void setRutProveedor(String rutProveedor) {
        this.rutProveedor = rutProveedor;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public Long getCompraId() {
        return compraId;
    }

    public void setCompraId(Long compraId) {
        this.compraId = compraId;
    }
}
