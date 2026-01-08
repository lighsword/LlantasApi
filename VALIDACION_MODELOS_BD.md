# 📋 Validación de Modelos Java vs Esquema PostgreSQL

## ✅ Análisis Completo de Todas las Tablas

### 📊 TABLA: ALMACENES
**Columnas en BD PostgreSQL:**
```
almacen_id (serial)
nombre (varchar 100)
ubicacion (varchar 255)
capacidad_maxima (numeric 10,2)
activo (boolean)
```

**Modelo Java - Almacenes.java:**
```java
✅ almacenId (Integer)
✅ nombre (String)
✅ ubicacion (String)
✅ capacidadMaxima (BigDecimal) - FALTA AGREGAR
❌ activo (Boolean) - FALTA VERIFICAR
```

**ESTADO:** ⚠️ FALTA `capacidadMaxima` - NECESITA CORRECCIÓN

---

### 📊 TABLA: PRODUCTOS
**Columnas en BD PostgreSQL:**
```
producto_id (serial)
codigo_producto (varchar 50) - UNIQUE
nombre (varchar 200)
categoria_id (integer)
descripcion (text)
marca (varchar 100)
modelo (varchar 100)
imagen_url (varchar 500)
precio_compra (numeric 10,2)
precio_venta (numeric 10,2)
precio_mayorista (numeric 10,2)
stock_actual (integer) - DEFAULT 0
stock_minimo (integer) - DEFAULT 5
activo (boolean) - DEFAULT true
fecha_registro (timestamp) - DEFAULT CURRENT_TIMESTAMP
```

**Modelo Java - Productos.java:**
- ✅ productoId
- ✅ codigoProducto
- ❌ nombre - FALTA
- ✅ categoriaId
- ✅ descripcion
- ✅ marca
- ✅ modelo
- ✅ imagenUrl
- ✅ precioCompra
- ✅ precioVenta
- ✅ precioMayorista
- ❌ stockActual - FALTA
- ❌ stockMinimo - FALTA
- ✅ activo
- ❌ fechaRegistro - FALTA

**ESTADO:** ⚠️ FALTAN 4 CAMPOS - NECESITA CORRECCIÓN

---

### 📊 TABLA: USUARIOS
**Columnas en BD PostgreSQL:**
```
usuario_id (serial)
nombre (varchar 100)
email (varchar 100) - UNIQUE
password (varchar 255)
rol (varchar 20)
activo (boolean) - DEFAULT true
fecha_creacion (timestamp) - DEFAULT CURRENT_TIMESTAMP
ultimo_acceso (timestamp)
```

**Modelo Java - Usuarios.java:**
- ✅ usuarioId
- ✅ nombre
- ✅ email
- ✅ password
- ✅ rol
- ✅ activo
- ✅ fechaCreacion
- ✅ ultimoAcceso

**ESTADO:** ✅ CORRECTO

---

### 📊 TABLA: CATEGORIAS
**Columnas en BD PostgreSQL:**
```
categoria_id (serial)
nombre (varchar 50) - UNIQUE
descripcion (text)
activo (boolean) - DEFAULT true
```

**Modelo Java - Categorias.java:**
- ✅ categoriaId
- ✅ nombre
- ✅ descripcion
- ✅ activo

**ESTADO:** ✅ CORRECTO

---

### 📊 TABLA: CLIENTES
**Columnas en BD PostgreSQL:**
```
cliente_id (serial)
nombre (varchar 150)
rfc (varchar 20)
telefono (varchar 20)
email (varchar 100)
direccion (text)
tipo_cliente (varchar 20) - DEFAULT 'MINORISTA'
activo (boolean) - DEFAULT true
fecha_registro (timestamp) - DEFAULT CURRENT_TIMESTAMP
```

**Modelo Java - Clientes.java:**
- ✅ clienteId
- ✅ nombre
- ✅ rfc
- ✅ telefono
- ✅ email
- ✅ direccion
- ✅ tipoCliente
- ✅ activo
- ❌ fechaRegistro - NECESITA VERIFICAR

**ESTADO:** ⚠️ VERIFICAR fechaRegistro

---

### 📊 TABLA: COMPRAS
**Columnas en BD PostgreSQL:**
```
compra_id (serial)
proveedor_id (integer)
usuario_id (integer)
fecha_compra (timestamp) - DEFAULT CURRENT_TIMESTAMP
total (numeric 12,2)
estado (varchar 20) - DEFAULT 'COMPLETADA'
notas (text)
```

**Modelo Java - Compras.java:**
- ✅ id (compraId)
- ✅ proveedorId
- ✅ usuarioId
- ✅ fechaCompra
- ✅ total
- ✅ estado
- ✅ notas

**ESTADO:** ✅ CORRECTO

---

### 📊 TABLA: VENTAS
**Columnas en BD PostgreSQL:**
```
venta_id (serial)
cliente_id (integer)
usuario_id (integer)
fecha_venta (timestamp) - DEFAULT CURRENT_TIMESTAMP
subtotal (numeric 12,2)
descuento (numeric 10,2) - DEFAULT 0
total (numeric 12,2)
metodo_pago_id (integer)
estado (varchar 20) - DEFAULT 'COMPLETADA'
notas (text)
```

**Modelo Java - Ventas.java:**
- ✅ ventaId (id)
- ✅ clienteId
- ✅ usuarioId
- ✅ fechaVenta
- ✅ subtotal
- ✅ descuento (descuentoAplicado - REVISAR NOMBRE)
- ✅ total
- ✅ metodoPagoId
- ✅ estado
- ✅ notas

**ESTADO:** ⚠️ REVISAR nombre de campo descuento

---

### 📊 TABLA: INVENTARIO
**Columnas en BD PostgreSQL:**
```
producto_id (integer) - PK/FK
almacen_id (integer) - PK/FK
cantidad (integer) - DEFAULT 0
ubicacion_fisica (varchar 50)
ultima_actualizacion (timestamp) - DEFAULT CURRENT_TIMESTAMP
```

**Modelo Java - Inventario.java:**
- ✅ productoId (composite key)
- ✅ almacenId (composite key)
- ✅ cantidad
- ✅ ubicacionFisica
- ✅ ultimaActualizacion

**ESTADO:** ✅ CORRECTO

---

### 📊 TABLA: PRECIOS
**Columnas en BD PostgreSQL:**
```
precio_id (serial)
producto_id (integer)
tipo_precio (varchar 20)
precio (numeric 10,2)
margen_porcentaje (numeric 5,2)
margen_monto (numeric 10,2)
fecha_registro (timestamp) - DEFAULT CURRENT_TIMESTAMP
usuario_registro_id (integer)
vigente (boolean) - DEFAULT true
```

**Modelo Java - Precios.java:**
- ✅ precioId
- ✅ productoId
- ✅ tipoPrecio
- ✅ precio
- ✅ margenPorcentaje
- ✅ margenMonto
- ✅ fechaRegistro
- ✅ usuarioRegistroId
- ✅ vigente

**ESTADO:** ✅ CORRECTO

---

### 📊 TABLA: MOVIMIENTOS_INVENTARIO
**Columnas en BD PostgreSQL:**
```
movimiento_id (serial)
producto_id (integer)
almacen_origen_id (integer)
almacen_destino_id (integer)
tipo_movimiento (varchar 20)
cantidad (integer)
motivo (text)
usuario_id (integer)
fecha_movimiento (timestamp) - DEFAULT CURRENT_TIMESTAMP
```

**Modelo Java - MovimientosInventario.java:**
- ✅ movimientoId
- ✅ productoId
- ✅ almacenOrigenId
- ✅ almacenDestinoId
- ✅ tipoMovimiento
- ✅ cantidad
- ✅ motivo
- ✅ usuarioId
- ✅ fechaMovimiento

**ESTADO:** ✅ CORRECTO

---

### 📊 TABLA: NOTIFICACIONES
**Columnas en BD PostgreSQL:**
```
notificacion_id (serial)
tipo (varchar 50)
titulo (varchar 200)
mensaje (text)
fecha_creacion (timestamp) - DEFAULT CURRENT_TIMESTAMP
prioridad (varchar 20) - DEFAULT 'NORMAL'
```

**Modelo Java - Notificaciones.java:**
- ✅ id (notificacionId)
- ✅ tipo
- ✅ titulo
- ✅ mensaje
- ✅ fechaCreacion
- ✅ prioridad

**ESTADO:** ✅ CORRECTO

---

### 📊 TABLA: PROMOCIONES
**Columnas en BD PostgreSQL:**
```
promocion_id (serial)
nombre (varchar 100)
descripcion (text)
tipo_descuento (varchar 20)
valor_descuento (numeric 10,2)
fecha_inicio (date)
fecha_fin (date)
activa (boolean) - DEFAULT true
```

**Modelo Java - Promociones.java:**
- ✅ promocionId
- ✅ nombre
- ✅ descripcion
- ✅ tipoDescuento
- ✅ valorDescuento
- ✅ fechaInicio
- ✅ fechaFin
- ✅ activa

**ESTADO:** ✅ CORRECTO

---

### 📊 TABLA: PROVEEDORES
**Columnas en BD PostgreSQL:**
```
proveedor_id (serial)
nombre_empresa (varchar 200)
rfc (varchar 20) - UNIQUE
contacto_nombre (varchar 100)
telefono (varchar 20)
email (varchar 100)
direccion (text)
activo (boolean) - DEFAULT true
```

**Modelo Java - Proveedores.java:**
- ✅ id (proveedorId)
- ✅ nombreEmpresa
- ✅ rfc
- ✅ contactoNombre
- ✅ telefono
- ✅ email
- ✅ direccion
- ✅ activo

**ESTADO:** ✅ CORRECTO

---

### 📊 TABLA: AUDITORIA_ACCESOS
**Columnas en BD PostgreSQL:**
```
auditoria_id (serial)
usuario_id (integer)
accion (varchar 100)
recurso (varchar 255)
ip_address (varchar 50)
fecha_hora (timestamp) - DEFAULT CURRENT_TIMESTAMP
exitoso (boolean) - DEFAULT true
detalles (text)
user_agent (text)
endpoint (varchar 255)
metodo_http (varchar 10)
mensaje (varchar 500)
```

**Modelo Java - AuditoriaAcceso.java:**
- ✅ auditoriaId
- ✅ usuarioId
- ✅ accion
- ✅ recurso
- ✅ ipAddress
- ✅ fechaHora
- ✅ exitoso
- ✅ detalles
- ✅ userAgent
- ✅ endpoint
- ✅ metodoHttp
- ✅ mensaje

**ESTADO:** ✅ CORRECTO

---

### 📊 TABLA: SESIONES_ACTIVAS
**Columnas en BD PostgreSQL:**
```
sesion_id (serial)
usuario_id (integer)
refresh_token (varchar 500)
jti_access (varchar 255) - UNIQUE
jti_refresh (varchar 255) - UNIQUE
ip_address (varchar 50)
user_agent (text)
fecha_creacion (timestamp) - DEFAULT CURRENT_TIMESTAMP
fecha_expiracion (timestamp)
activo (boolean) - DEFAULT true
```

**Modelo Java - SesionActiva.java:**
- ✅ sesionId
- ✅ usuarioId
- ✅ refreshToken
- ✅ jtiAccess
- ✅ jtiRefresh
- ✅ ipAddress
- ✅ userAgent
- ✅ fechaCreacion
- ✅ fechaExpiracion
- ✅ activo

**ESTADO:** ✅ CORRECTO

---

### 📊 TABLA: TOKENS_REVOCADOS
**Columnas en BD PostgreSQL:**
```
revocacion_id (serial)
jti (varchar 255) - UNIQUE
fecha_revocacion (timestamp) - DEFAULT CURRENT_TIMESTAMP
fecha_expiracion (timestamp)
motivo (varchar 50)
usuario_id (bigint)
```

**Modelo Java - TokenRevocado.java:**
- ✅ revocacionId
- ✅ jti
- ✅ fechaRevocacion
- ✅ fechaExpiracion
- ✅ motivo
- ✅ usuarioId

**ESTADO:** ✅ CORRECTO

---

### 📊 TABLA: INTENTOS_LOGIN_FALLIDOS
**Columnas en BD PostgreSQL:**
```
intento_id (serial)
email (varchar 100)
ip_address (varchar 50)
fecha_hora (timestamp) - DEFAULT CURRENT_TIMESTAMP
bloqueado_hasta (timestamp)
usuario_id (bigint)
```

**Modelo Java - IntentoLoginFallido.java:**
- ✅ intentoId
- ✅ email
- ✅ ipAddress
- ✅ fechaHora
- ✅ bloqueadoHasta
- ✅ usuarioId

**ESTADO:** ✅ CORRECTO

---

### 📊 TABLA: PERMISOS
**Columnas en BD PostgreSQL:**
```
permiso_id (serial)
rol (varchar 50)
modulo (varchar 50)
accion (varchar 50)
permitido (boolean) - DEFAULT true
descripcion (varchar 500)
```

**ESTADO:** ⚠️ NECESITA REVISAR modelo Permisos.java

---

### 📊 TABLA: ROLES
**Columnas en BD PostgreSQL:**
```
rol_id (bigserial)
nombre_rol (varchar 50) - UNIQUE
descripcion (varchar 255)
```

**ESTADO:** ✅ Existe en enums/Role.java

---

### 📊 TABLA: DETALLE_VENTAS
**Columnas en BD PostgreSQL:**
```
detalle_venta_id (serial)
venta_id (integer)
producto_id (integer)
cantidad (integer)
precio_unitario (numeric 10,2)
descuento (numeric 10,2) - DEFAULT 0
subtotal (numeric 12,2)
```

**Modelo Java - DetalleVentas.java:**
- ✅ detalleVentaId
- ✅ ventaId
- ✅ productoId
- ✅ cantidad
- ✅ precioUnitario
- ✅ descuento
- ✅ subtotal

**ESTADO:** ✅ CORRECTO

---

### 📊 TABLA: DETALLE_COMPRAS
**Columnas en BD PostgreSQL:**
```
compra_id (integer) - PK/FK
producto_id (integer) - PK/FK
cantidad (integer)
precio_unitario (numeric 10,2)
subtotal (numeric 12,2)
```

**Modelo Java - DetalleCompras.java:**
- ✅ compraId (composite key)
- ✅ productoId (composite key)
- ✅ cantidad
- ✅ precioUnitario
- ✅ subtotal

**ESTADO:** ✅ CORRECTO

---

## 📊 RESUMEN GENERAL

| Tabla | Estado | Observaciones |
|-------|--------|---------------|
| ALMACENES | ⚠️ | Falta `capacidadMaxima` |
| PRODUCTOS | ⚠️ | Faltan 4 campos (nombre, stock_actual, stock_minimo, fecha_registro) |
| USUARIOS | ✅ | Correcto |
| CATEGORIAS | ✅ | Correcto |
| CLIENTES | ⚠️ | Revisar fechaRegistro |
| COMPRAS | ✅ | Correcto |
| VENTAS | ⚠️ | Revisar nombre de campo descuento |
| INVENTARIO | ✅ | Correcto |
| PRECIOS | ✅ | Correcto |
| MOVIMIENTOS_INVENTARIO | ✅ | Correcto |
| NOTIFICACIONES | ✅ | Correcto |
| PROMOCIONES | ✅ | Correcto |
| PROVEEDORES | ✅ | Correcto |
| AUDITORIA_ACCESOS | ✅ | Correcto |
| SESIONES_ACTIVAS | ✅ | Correcto |
| TOKENS_REVOCADOS | ✅ | Correcto |
| INTENTOS_LOGIN_FALLIDOS | ✅ | Correcto |
| DETALLE_VENTAS | ✅ | Correcto |
| DETALLE_COMPRAS | ✅ | Correcto |

---

## 🔴 PROBLEMAS CRÍTICOS ENCONTRADOS

### 1. ALMACENES - FALTA `capacidadMaxima`
```java
// FALTA en Almacenes.java
@Column(name = "capacidad_maxima")
private BigDecimal capacidadMaxima;
```

### 2. PRODUCTOS - FALTAN 4 CAMPOS
```java
// FALTA en Productos.java
@Column(name = "nombre")
private String nombre;

@Column(name = "stock_actual")
private Integer stockActual;

@Column(name = "stock_minimo")
private Integer stockMinimo;

@Column(name = "fecha_registro")
private LocalDateTime fechaRegistro;
```

### 3. CLIENTES - VERIFICAR `fechaRegistro`
```java
// Verificar que exista en Clientes.java
@Column(name = "fecha_registro")
private LocalDateTime fechaRegistro;
```

### 4. VENTAS - CAMPO `descuento` mal nombrado
```java
// Está como descuentoAplicado, debería ser descuento
// O verificar nomenclatura en BD
```

---

## ✅ PRÓXIMOS PASOS

1. ✅ Agregar campos faltantes a Almacenes.java
2. ✅ Agregar campos faltantes a Productos.java
3. ✅ Verificar campos en Clientes.java
4. ✅ Verificar nombres de campos en Ventas.java
5. ✅ Revisar modelo Permisos.java
6. ✅ Compilar y verificar que BUILD SUCCESS
7. ✅ Hacer commit y push a GitHub


