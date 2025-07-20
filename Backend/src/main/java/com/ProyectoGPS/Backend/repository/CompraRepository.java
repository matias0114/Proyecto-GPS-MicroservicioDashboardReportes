package com.ProyectoGPS.Backend.repository;

import com.ProyectoGPS.Backend.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    
    List<Compra> findByEstado(Compra.EstadoCompra estado);
    
    List<Compra> findByFechaCompraBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    @Query("SELECT SUM(c.montoTotal) FROM Compra c WHERE c.fechaCompra BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal sumMontoTotalByFechaCompraBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                                @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT COUNT(c) FROM Compra c WHERE c.fechaCompra >= :fechaInicio")
    long countComprasFromDate(@Param("fechaInicio") LocalDateTime fechaInicio);
    
    @Query("SELECT c FROM Compra c WHERE c.proveedor LIKE %:proveedor%")
    List<Compra> findByProveedorContaining(@Param("proveedor") String proveedor);
}
