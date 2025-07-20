package com.ProyectoGPS.Backend.repository;

import com.ProyectoGPS.Backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByActive(Boolean active);
    
    List<Product> findByCategory(String category);
    
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name% OR p.code LIKE %:name%")
    List<Product> findByNameOrCodeContaining(@Param("name") String name);
    
    @Query("SELECT COUNT(p) FROM Product p WHERE p.active = true")
    long countActiveProducts();
    
    // Productos sin movimiento (sin detalles de compra en un periodo)
    @Query("SELECT p FROM Product p WHERE p.id NOT IN " +
           "(SELECT DISTINCT dc.product.id FROM DetalleCompra dc " +
           "WHERE dc.compra.fechaCompra >= :fechaInicio)")
    List<Product> findProductsWithoutMovement(@Param("fechaInicio") LocalDateTime fechaInicio);
    
    // Productos con mayor movimiento
    @Query("SELECT p, SUM(dc.cantidad) as totalMovimiento FROM Product p " +
           "JOIN p.detallesCompra dc " +
           "WHERE dc.compra.fechaCompra >= :fechaInicio " +
           "GROUP BY p " +
           "ORDER BY totalMovimiento DESC")
    List<Object[]> findProductsWithMostMovement(@Param("fechaInicio") LocalDateTime fechaInicio);
    
    @Query("SELECT p FROM Product p WHERE p.code = :code")
    Product findByCode(@Param("code") String code);
}
