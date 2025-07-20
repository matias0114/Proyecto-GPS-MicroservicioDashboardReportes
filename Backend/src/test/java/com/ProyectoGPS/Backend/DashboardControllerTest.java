package com.ProyectoGPS.Backend;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.ProyectoGPS.Backend.controller.DashboardController;
import com.ProyectoGPS.Backend.dto.DashboardDTO;
import com.ProyectoGPS.Backend.dto.FacturaReporteDTO;
import com.ProyectoGPS.Backend.dto.ProductoMovimientoDTO;
import com.ProyectoGPS.Backend.dto.ReporteRequest;
import com.ProyectoGPS.Backend.model.Product;
import com.ProyectoGPS.Backend.service.ReporteService;

class DashboardControllerTest {

    @InjectMocks
    private DashboardController dashboardController;

    @Mock
    private ReporteService reporteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDashboardMetricas_returnsOk() {
        DashboardDTO dashboard = new DashboardDTO();
        when(reporteService.getDashboardData()).thenReturn(dashboard);

        ResponseEntity<DashboardDTO> response = dashboardController.getDashboardMetricas();

        assertEquals(200, response.getStatusCodeValue());
        assertSame(dashboard, response.getBody());
    }

    @Test
    void getDashboardMetricas_exception_returns500() {
        when(reporteService.getDashboardData()).thenThrow(new RuntimeException());

        ResponseEntity<DashboardDTO> response = dashboardController.getDashboardMetricas();
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void getReporteFacturas_returnsOk() {
        List<FacturaReporteDTO> mockList = Arrays.asList(new FacturaReporteDTO());
        when(reporteService.getReporteFacturas(any(ReporteRequest.class))).thenReturn(mockList);

        ResponseEntity<List<FacturaReporteDTO>> resp = dashboardController.getReporteFacturas(null, null, "ProveedorX");

        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(1, resp.getBody().size());
    }

    @Test
    void getReporteFacturas_exception_returns500() {
        when(reporteService.getReporteFacturas(any())).thenThrow(new RuntimeException());

        ResponseEntity<List<FacturaReporteDTO>> resp = dashboardController.getReporteFacturas(null, null, null);
        assertEquals(500, resp.getStatusCodeValue());
    }

    @Test
    void getProductosSinMovimiento_returnsOk() {
        List<Product> productos = Collections.singletonList(new Product());
        when(reporteService.getProductosSinMovimiento(any())).thenReturn(productos);

        ResponseEntity<List<Product>> resp = dashboardController.getProductosSinMovimiento(null, "cat");
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(1, resp.getBody().size());
    }

    @Test
    void getProductosSinMovimiento_exception_returns500() {
        when(reporteService.getProductosSinMovimiento(any())).thenThrow(new RuntimeException());
        ResponseEntity<List<Product>> resp = dashboardController.getProductosSinMovimiento(null, null);
        assertEquals(500, resp.getStatusCodeValue());
    }

    @Test
    void getProductosConMovimiento_returnsOk() {
        List<ProductoMovimientoDTO> lista = Collections.singletonList(new ProductoMovimientoDTO());
        when(reporteService.getProductosConMayorMovimiento(any())).thenReturn(lista);

        ResponseEntity<List<ProductoMovimientoDTO>> resp = dashboardController.getProductosConMovimiento(null, null);
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(1, resp.getBody().size());
    }

    @Test
    void getProductosConMovimiento_exception_returns500() {
        when(reporteService.getProductosConMayorMovimiento(any())).thenThrow(new RuntimeException());
        ResponseEntity<List<ProductoMovimientoDTO>> resp = dashboardController.getProductosConMovimiento(null, null);
        assertEquals(500, resp.getStatusCodeValue());
    }

    @Test
    void exportarFacturasExcel_returnsOk() throws Exception {
        byte[] excel = new byte[]{1,2,3};
        when(reporteService.exportarFacturasExcel(any())).thenReturn(excel);

        ResponseEntity<byte[]> resp = dashboardController.exportarFacturasExcel(null, null, null);
        assertEquals(200, resp.getStatusCodeValue());
        assertArrayEquals(excel, resp.getBody());
    }

    @Test
    void exportarFacturasExcel_exception_returns500() throws Exception {
        when(reporteService.exportarFacturasExcel(any())).thenThrow(new IOException());
        ResponseEntity<byte[]> resp = dashboardController.exportarFacturasExcel(null, null, null);
        assertEquals(500, resp.getStatusCodeValue());
    }

    @Test
    void exportarProductosSinMovimientoExcel_returnsOk() throws Exception {
        byte[] excel = new byte[]{1};
        when(reporteService.exportarProductosSinMovimientoExcel(any())).thenReturn(excel);

        ResponseEntity<byte[]> resp = dashboardController.exportarProductosSinMovimientoExcel(null, null);
        assertEquals(200, resp.getStatusCodeValue());
        assertArrayEquals(excel, resp.getBody());
    }

    @Test
    void exportarProductosSinMovimientoExcel_exception_returns500() throws Exception {
        when(reporteService.exportarProductosSinMovimientoExcel(any())).thenThrow(new IOException());
        ResponseEntity<byte[]> resp = dashboardController.exportarProductosSinMovimientoExcel(null, null);
        assertEquals(500, resp.getStatusCodeValue());
    }

    @Test
    void exportarProductosConMovimientoExcel_returnsOk() throws Exception {
        byte[] excel = new byte[]{1, 2};
        when(reporteService.exportarProductosConMovimientoExcel(any())).thenReturn(excel);

        ResponseEntity<byte[]> resp = dashboardController.exportarProductosConMovimientoExcel(null, null);
        assertEquals(200, resp.getStatusCodeValue());
        assertArrayEquals(excel, resp.getBody());
    }

    @Test
    void exportarProductosConMovimientoExcel_exception_returns500() throws Exception {
        when(reporteService.exportarProductosConMovimientoExcel(any())).thenThrow(new IOException());
        ResponseEntity<byte[]> resp = dashboardController.exportarProductosConMovimientoExcel(null, null);
        assertEquals(500, resp.getStatusCodeValue());
    }

    @Test
    void getHistoricoFacturas_exception_returns500() {
        when(reporteService.getDashboardData()).thenThrow(new RuntimeException());
        ResponseEntity<List<Object[]>> resp = dashboardController.getHistoricoFacturas(12);
        assertEquals(500, resp.getStatusCodeValue());
    }
}