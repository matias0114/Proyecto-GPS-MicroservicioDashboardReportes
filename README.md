# Microservicio Dashboard y Reportes

Este microservicio proporciona un panel de métricas clave (Dashboard) y generación de informes sobre ventas, facturas, productos sin movimiento, etc.

## Funcionalidades

### Dashboard
- Panel de métricas clave con datos en tiempo real
- Métricas de Prometheus para monitoreo
- Datos agregados de facturas, compras, productos y usuarios

### Módulo de Informes
- Ingreso de facturas
- Consumos
- Productos sin movimiento
- Productos con mayor movimiento  
- Histórico de ventas
- Exportación a Excel

## Endpoints Disponibles

### Dashboard
- `GET /api/dashboard/metricas` - Obtiene todas las métricas del dashboard

### Reportes JSON
- `GET /api/dashboard/reportes/facturas` - Reporte de facturas
  - Parámetros: `fechaInicio`, `fechaFin`, `proveedor`
- `GET /api/dashboard/reportes/productos-sin-movimiento` - Productos sin movimiento
  - Parámetros: `fechaInicio`, `categoria`
- `GET /api/dashboard/reportes/productos-con-movimiento` - Productos con mayor movimiento
  - Parámetros: `fechaInicio`, `categoria`

### Exportar Excel
- `GET /api/dashboard/exportar/facturas-excel` - Exporta facturas a Excel
- `GET /api/dashboard/exportar/productos-sin-movimiento-excel` - Exporta productos sin movimiento a Excel  
- `GET /api/dashboard/exportar/productos-con-movimiento-excel` - Exporta productos con mayor movimiento a Excel

### Histórico
- `GET /api/dashboard/historico/facturas` - Histórico de facturas agrupado por mes
  - Parámetros: `meses` (default: 12)

## Métricas de Prometheus

Las siguientes métricas están disponibles en `/actuator/prometheus`:

### Usuarios
- `miapp_usuarios_totales` - Cantidad total de usuarios

### Facturas  
- `miapp_facturas_totales` - Cantidad total de facturas
- `miapp_facturas_mes_actual` - Facturas del mes actual
- `miapp_monto_total_facturas` - Monto total de facturas (últimos 12 meses)
- `miapp_monto_facturas_mes_actual` - Monto facturas del mes actual

### Compras
- `miapp_compras_totales` - Cantidad total de compras
- `miapp_compras_pendientes` - Compras pendientes
- `miapp_compras_recibidas` - Compras recibidas  
- `miapp_monto_total_compras` - Monto total de compras (últimos 12 meses)

### Productos
- `miapp_productos_totales` - Cantidad total de productos
- `miapp_productos_activos` - Productos activos
- `miapp_productos_sin_movimiento` - Productos sin movimiento (últimos 30 días)

## Entidades del Modelo

### Factura
- ID, número, fecha emisión, monto total
- Relación con Compra
- RUT proveedor

### Compra  
- ID, fecha, proveedor, monto total, estado
- Estados: PENDIENTE, RECIBIDA, FACTURADA, CANCELADA
- Relación con Facturas y DetalleCompra

### Product
- ID, código, nombre, descripción, categoría, laboratorio
- Precios base, método de pricing, unidad
- Relación con Batches, PriceLists, DetallesCompra

### Usuario
- ID, username, email, nombre, apellido
- Rol (ADMIN, USUARIO, SUPERVISOR)
- Fechas de creación y último acceso

## Configuración

### Base de Datos
- PostgreSQL en Render
- Conexión configurada en `application.properties`
- JPA con Hibernate para ORM

### Dependencias
- Spring Boot 3.4.5
- Spring Data JPA
- Spring Security  
- Micrometer Prometheus
- Apache POI (Excel export)
- PostgreSQL Driver

### Puerto
- Aplicación corre en puerto 8085
- Métricas disponibles en `/actuator/prometheus`

## Despliegue

### Docker
```bash
docker build -t microservicio-dashboard-reportes .
docker run -p 8085:8085 microservicio-dashboard-reportes
```

### Jenkins Pipeline
- Pipeline configurado en `Jenkinsfile`
- Build automático con Maven
- Deploy en servidor de producción

## Ejemplos de Uso

### Obtener métricas del dashboard
```bash
curl http://localhost:8085/api/dashboard/metricas
```

### Exportar facturas a Excel
```bash
curl "http://localhost:8085/api/dashboard/exportar/facturas-excel?fechaInicio=2024-01-01T00:00:00&fechaFin=2024-12-31T23:59:59" -o facturas.xlsx
```

### Ver productos sin movimiento
```bash
curl "http://localhost:8085/api/dashboard/reportes/productos-sin-movimiento?fechaInicio=2024-01-01T00:00:00&categoria=medicamentos"
```
