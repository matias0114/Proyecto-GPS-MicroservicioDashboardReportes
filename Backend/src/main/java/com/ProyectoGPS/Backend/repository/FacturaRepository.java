package com.ProyectoGPS.Backend.repository;

import com.ProyectoGPS.Backend.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    
    List<Factura> findByFechaEmisionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<Factura> findByRutProveedor(String rutProveedor);
    
    @Query("SELECT SUM(f.montoTotal) FROM Factura f WHERE f.fechaEmision BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal sumMontoTotalByFechaEmisionBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                                 @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT COUNT(f) FROM Factura f WHERE f.fechaEmision >= :fechaInicio")
    long countFacturasFromDate(@Param("fechaInicio") LocalDateTime fechaInicio);
    
    @Query("SELECT f FROM Factura f JOIN f.compra c WHERE c.proveedor LIKE %:proveedor%")
    List<Factura> findByProveedorContaining(@Param("proveedor") String proveedor);
    
    @Query("SELECT YEAR(f.fechaEmision) as año, MONTH(f.fechaEmision) as mes, SUM(f.montoTotal) as total " +
           "FROM Factura f WHERE f.fechaEmision >= :fechaInicio " +
           "GROUP BY YEAR(f.fechaEmision), MONTH(f.fechaEmision) " +
           "ORDER BY año DESC, mes DESC")
    List<Object[]> findFacturasGroupedByMonth(@Param("fechaInicio") LocalDateTime fechaInicio);
}
