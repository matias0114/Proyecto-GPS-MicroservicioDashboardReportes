package com.ProyectoGPS.Backend.Integracion;

import static org.hamcrest.Matchers.containsString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ProyectoGPS.Backend.service.ReporteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") 
public class DashboardIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReporteService reporteService; 

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getDashboardMetricas_OK() throws Exception {
        mockMvc.perform(get("/api/dashboard/metricas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void getReporteFacturas_OK() throws Exception {
        mockMvc.perform(get("/api/dashboard/reportes/facturas")
                        .param("proveedor", "ProveedorTest"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getProductosSinMovimiento_OK() throws Exception {
        mockMvc.perform(get("/api/dashboard/reportes/productos-sin-movimiento")
                        .param("categoria", "CategoriaX"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getProductosConMovimiento_OK() throws Exception {
        mockMvc.perform(get("/api/dashboard/reportes/productos-con-movimiento")
                        .param("categoria", "CategoriaX"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void exportarFacturasExcel_OK() throws Exception {
        mockMvc.perform(get("/api/dashboard/exportar/facturas-excel")
                        .param("proveedor", "ProveedorTest"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", containsString("application/octet-stream")))
                .andExpect(header().string("Content-Disposition", containsString("reporte_facturas.xlsx")));
    }

    @Test
    void exportarProductosSinMovimientoExcel_OK() throws Exception {
        mockMvc.perform(get("/api/dashboard/exportar/productos-sin-movimiento-excel")
                        .param("categoria", "CategoriaX"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", containsString("application/octet-stream")))
                .andExpect(header().string("Content-Disposition", containsString("productos_sin_movimiento.xlsx")));
    }

    @Test
    void exportarProductosConMovimientoExcel_OK() throws Exception {
        mockMvc.perform(get("/api/dashboard/exportar/productos-con-movimiento-excel")
                        .param("categoria", "CategoriaX"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", containsString("application/octet-stream")))
                .andExpect(header().string("Content-Disposition", containsString("productos_con_mayor_movimiento.xlsx")));
    }

    @Test
    void getHistoricoFacturas_OK() throws Exception {
        mockMvc.perform(get("/api/dashboard/historico/facturas")
                        .param("meses", "12"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}