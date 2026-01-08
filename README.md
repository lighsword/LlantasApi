# 🚗 LlantasAPI - Sistema de Gestión para Tienda de Llantas

API REST desarrollada en **Spring Boot 3.4** con autenticación JWT, sistema RBAC (Control de Acceso Basado en Roles), y gestión completa de inventario para tiendas de llantas y accesorios automotrices.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Autenticación JWT](#-autenticación-jwt)
- [Endpoints de la API](#-endpoints-de-la-api)
- [Configuración en Postman](#-configuración-en-postman)
- [Swagger UI](#-swagger-ui)
- [Seguridad](#-seguridad)
- [Roles y Permisos](#-roles-y-permisos)

---

## ✨ Características

- ✅ **Autenticación JWT** con Access Token (15 min) y Refresh Token (7 días)
- ✅ **Sistema RBAC** con permisos granulares por módulo y acción
- ✅ **Rotación de tokens** para máxima seguridad
- ✅ **Gestión de sesiones** con límite de 3 sesiones simultáneas
- ✅ **Alertas de seguridad automáticas** (detección de ataques de fuerza bruta)
- ✅ **Auditoría completa** de accesos con IP y User-Agent
- ✅ **Headers de seguridad HTTP** (HSTS, CSP, X-Frame-Options)
- ✅ **Rate limiting** para intentos de login
- ✅ **Soft delete** para productos
- ✅ **Documentación Swagger/OpenAPI**

---

## 🔧 Requisitos Previos

- **Java 21** o superior
- **PostgreSQL 15+**
- **Maven 3.9+** (o usar el wrapper incluido `mvnw`)

---

## 🚀 Instalación

### 1. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/LlantasApi.git
cd LlantasApi
```

### 2. Crear la base de datos
```sql
CREATE DATABASE BackendLlantas;
```

### 3. Ejecutar script de migración de seguridad
```sql
\i sql/migracion_seguridad.sql
```

### 4. Compilar y ejecutar
```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

La API estará disponible en: `http://localhost:8081`

---

## ⚙️ Configuración

### Archivo `application.properties`

```properties
# Base de datos
spring.datasource.url=jdbc:postgresql://localhost:5432/BackendLlantas
spring.datasource.username=postgres
spring.datasource.password=tu_password

# JWT
security.jwt.secret=tu_clave_secreta_de_256_bits
security.jwt.access-token-expiration-ms=900000      # 15 minutos
security.jwt.refresh-token-expiration-ms=604800000  # 7 días

# Seguridad
security.session.max-concurrent=3
security.login.max-attempts=5
security.login.lockout-duration-minutes=15
```

### Variables de Entorno (Producción)

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `DATABASE_URL` | URL de PostgreSQL | `jdbc:postgresql://host:5432/db` |
| `DB_USERNAME` | Usuario de BD | `postgres` |
| `DB_PASSWORD` | Contraseña de BD | `secreto` |
| `JWT_SECRET_KEY` | Clave JWT (256 bits) | `tu_clave_muy_larga_y_segura` |
| `CORS_ALLOWED_ORIGINS` | Orígenes permitidos | `https://tuapp.com` |

---

## 🔐 Autenticación JWT

### Flujo de Autenticación

```
┌─────────┐     POST /api/auth/login      ┌─────────┐
│ Cliente │ ─────────────────────────────▶ │   API   │
│         │ ◀───────────────────────────── │         │
└─────────┘  { accessToken, refreshToken } └─────────┘
     │
     │  (usar accessToken en cada request)
     │
     ▼
┌─────────┐     GET /api/productos         ┌─────────┐
│ Cliente │ ─────────────────────────────▶ │   API   │
│         │  Header: Authorization:        │         │
│         │  Bearer eyJhbGciOiJ...         │         │
└─────────┘                                └─────────┘
     │
     │  (cuando accessToken expire, usar refreshToken)
     │
     ▼
┌─────────┐     POST /api/auth/refresh     ┌─────────┐
│ Cliente │ ─────────────────────────────▶ │   API   │
│         │ ◀───────────────────────────── │         │
└─────────┘  { nuevo accessToken,          └─────────┘
               nuevo refreshToken }
```

### Tiempos de Expiración

| Token | Duración | Uso |
|-------|----------|-----|
| **Access Token** | 15 minutos | Autenticar cada request |
| **Refresh Token** | 7 días | Obtener nuevos tokens |

---

## 📡 Endpoints de la API

### 🔐 Autenticación (`/api/auth`)

#### Registrar Usuario
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "usuario@ejemplo.com",
  "nombre": "Juan Pérez",
  "password": "contraseña123",
  "rol": "VENDEDOR"
}
```

**Respuesta exitosa (200):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "expiresIn": 900000,
  "usuario": {
    "id": 1,
    "email": "usuario@ejemplo.com",
    "nombre": "Juan Pérez",
    "rol": "VENDEDOR"
  }
}
```

**Roles disponibles:** `ADMIN`, `VENDEDOR`, `ALMACENISTA`, `COMPRADOR`

---

#### Iniciar Sesión
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "usuario@ejemplo.com",
  "password": "contraseña123"
}
```

**Respuesta exitosa (200):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "expiresIn": 900000,
  "usuario": {
    "id": 1,
    "email": "usuario@ejemplo.com",
    "nombre": "Juan Pérez",
    "rol": "VENDEDOR"
  }
}
```

**Errores posibles:**
| Código | Descripción |
|--------|-------------|
| 401 | Credenciales inválidas |
| 423 | Usuario bloqueado (5+ intentos fallidos) |

---

#### Refrescar Tokens
```http
POST /api/auth/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Respuesta exitosa (200):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "rotated": true
}
```

> ⚠️ **IMPORTANTE:** Guarda el nuevo `refreshToken`. El anterior queda invalidado.

---

#### Cerrar Sesión
```http
POST /api/auth/logout
Authorization: Bearer {accessToken}
```

**Respuesta (200):**
```json
{
  "mensaje": "Sesión cerrada exitosamente"
}
```

---

#### Cerrar Todas las Sesiones
```http
POST /api/auth/logout-all
Authorization: Bearer {accessToken}
```

---

### 👤 Usuarios (`/api/usuarios`) - Solo ADMIN

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/usuarios` | Listar todos los usuarios |
| `GET` | `/api/usuarios/{id}` | Obtener usuario por ID |
| `POST` | `/api/usuarios` | Crear usuario |
| `PUT` | `/api/usuarios/{id}` | Actualizar usuario |
| `DELETE` | `/api/usuarios/{id}` | Eliminar usuario |

#### Crear Usuario
```http
POST /api/usuarios
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "email": "nuevo@ejemplo.com",
  "nombre": "Nuevo Usuario",
  "password": "password123",
  "rol": "VENDEDOR",
  "activo": true
}
```

---

### 📦 Productos (`/api/productos`)

| Método | Endpoint | Descripción | Acceso |
|--------|----------|-------------|--------|
| `GET` | `/api/productos` | Todos los productos | ADMIN, ALMACENISTA |
| `GET` | `/api/productos/visibles` | Productos en catálogo (stock > 0) | Todos |
| `GET` | `/api/productos/agotados` | Productos sin stock | ADMIN, ALMACENISTA |
| `GET` | `/api/productos/descontinuados` | Productos inactivos | ADMIN |
| `GET` | `/api/productos/{id}` | Obtener por ID | Todos |
| `GET` | `/api/productos/buscar?q=texto` | Buscar productos | Todos |
| `POST` | `/api/productos` | Crear producto | ADMIN, ALMACENISTA |
| `PUT` | `/api/productos/{id}` | Actualizar producto | ADMIN, ALMACENISTA |
| `DELETE` | `/api/productos/{id}` | Descontinuar (soft delete) | ADMIN |

#### Crear Producto
```http
POST /api/productos
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "codigoProducto": "LLA-001",
  "descripcion": "Llanta Michelin 205/55R16",
  "marca": "Michelin",
  "modelo": "Primacy 4",
  "precioCompra": 150.00,
  "precioVenta": 220.00,
  "categoriaId": 1,
  "activo": true
}
```

---

### 👥 Clientes (`/api/clientes`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/clientes` | Listar todos |
| `GET` | `/api/clientes/activos` | Solo activos |
| `GET` | `/api/clientes/{id}` | Por ID |
| `GET` | `/api/clientes/email/{email}` | Por email |
| `GET` | `/api/clientes/documento/{doc}` | Por documento |
| `POST` | `/api/clientes` | Crear |
| `PUT` | `/api/clientes/{id}` | Actualizar |
| `DELETE` | `/api/clientes/{id}` | Eliminar |

#### Crear Cliente
```http
POST /api/clientes
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "nombre": "María García",
  "email": "maria@ejemplo.com",
  "telefono": "999888777",
  "documentoIdentidad": "12345678",
  "tipoDocumento": "DNI",
  "direccion": "Av. Principal 123",
  "activo": true
}
```

---

### 💰 Ventas (`/api/ventas`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/ventas` | Listar ventas |
| `GET` | `/api/ventas/{id}` | Obtener venta |
| `GET` | `/api/ventas/{id}/detalles` | Detalles de venta |
| `POST` | `/api/ventas` | Crear venta |
| `POST` | `/api/ventas/{id}/detalles` | Agregar detalle |

#### Crear Venta
```http
POST /api/ventas
Authorization: Bearer {accessToken}
Content-Type: application/json

{
  "clienteId": 1,
  "usuarioId": 1,
  "metodoPago": "EFECTIVO",
  "observaciones": "Venta al contado"
}
```

---

### 📊 Inventario (`/api/inventario`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/inventario` | Todo el inventario |
| `GET` | `/api/inventario/almacen/{id}` | Por almacén |
| `GET` | `/api/inventario/producto/{id}` | Por producto |
| `GET` | `/api/inventario/{productoId}/{almacenId}` | Específico |
| `POST` | `/api/inventario` | Crear/Actualizar |
| `DELETE` | `/api/inventario/{productoId}/{almacenId}` | Eliminar |

---

### 🔐 Permisos RBAC (`/api/permisos`)

| Método | Endpoint | Descripción | Acceso |
|--------|----------|-------------|--------|
| `GET` | `/api/permisos/mis-permisos` | Mis permisos | Todos |
| `GET` | `/api/permisos/verificar?modulo=X&accion=Y` | Verificar permiso | Todos |
| `GET` | `/api/permisos/Role/{rol}` | Permisos de un rol | ADMIN |
| `GET` | `/api/permisos/matriz` | Matriz completa | ADMIN |
| `POST` | `/api/permisos/inicializar` | Cargar defaults | ADMIN |
| `PUT` | `/api/permisos` | Modificar permiso | ADMIN |

#### Obtener Mis Permisos (para Frontend)
```http
GET /api/permisos/mis-permisos
Authorization: Bearer {accessToken}
```

**Respuesta:**
```json
{
  "VENTAS": {
    "VER": true,
    "CREAR": true,
    "EDITAR": true,
    "ELIMINAR": false
  },
  "CLIENTES": {
    "VER": true,
    "CREAR": true,
    "EDITAR": true,
    "ELIMINAR": false
  },
  "PRODUCTOS": {
    "VER": true,
    "CREAR": false,
    "EDITAR": false,
    "ELIMINAR": false
  }
}
```

---

### 🛡️ Seguridad (`/api/seguridad`) - Solo ADMIN

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/seguridad/resumen` | Resumen de seguridad 24h |
| `GET` | `/api/seguridad/estadisticas?horasAtras=24` | Estadísticas de accesos |
| `GET` | `/api/seguridad/ip-sospechosa?ip=X` | Verificar IP |
| `GET` | `/api/seguridad/auditoria/usuario/{id}` | Auditoría de usuario |
| `GET` | `/api/seguridad/auditoria/ip/{ip}` | Auditoría por IP |
| `GET` | `/api/seguridad/sesiones/usuario/{id}` | Sesiones activas |
| `POST` | `/api/seguridad/sesiones/cerrar-todas/{id}` | Forzar logout |
| `POST` | `/api/seguridad/analizar` | Ejecutar análisis manual |

#### Obtener Estadísticas
```http
GET /api/seguridad/estadisticas?horasAtras=48
Authorization: Bearer {accessToken}
```

**Respuesta:**
```json
{
  "periodo": "Últimas 48 horas",
  "desde": "2026-01-06T10:00:00",
  "hasta": "2026-01-08T10:00:00",
  "logins": 150,
  "logouts": 120,
  "accesosDenegados": 8,
  "alertasSeguridad": 2,
  "ipsUnicas": 25,
  "sesionesEstimadas": 30
}
```

---

### 📁 Otros Endpoints

| Módulo | Base URL | Descripción |
|--------|----------|-------------|
| Categorías | `/api/categorias` | CRUD de categorías |
| Almacenes | `/api/almacenes` | Gestión de almacenes |
| Compras | `/api/compras` | Compras a proveedores |
| Proveedores | `/api/proveedores` | Gestión de proveedores |
| Precios | `/api/precios` | Historial de precios |
| Promociones | `/api/promociones` | Gestión de promociones |
| Reportes | `/api/reportes` | Reportes del sistema |
| Dashboard | `/api/dashboard` | Métricas generales |
| Notificaciones | `/api/notificaciones` | Sistema de notificaciones |
| Movimientos | `/api/movimientos-inventario` | Movimientos de stock |

---

## 📮 Configuración en Postman

### 1. Crear Colección

1. Abre Postman
2. Click en **"New Collection"**
3. Nombre: `LlantasAPI`

### 2. Configurar Variables de Colección

Ve a la pestaña **Variables** de la colección:

| Variable | Initial Value | Current Value |
|----------|---------------|---------------|
| `base_url` | `http://localhost:8081` | `http://localhost:8081` |
| `access_token` | (vacío) | (vacío) |
| `refresh_token` | (vacío) | (vacío) |

### 3. Configurar Autenticación Automática

En la pestaña **Authorization** de la colección:
- Type: **Bearer Token**
- Token: `{{access_token}}`

### 4. Script para Guardar Tokens Automáticamente

En el request de **Login**, ve a **Tests** y agrega:

```javascript
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.collectionVariables.set("access_token", jsonData.accessToken);
    pm.collectionVariables.set("refresh_token", jsonData.refreshToken);
    console.log("✅ Tokens guardados automáticamente");
}
```

### 5. Importar Colección desde Swagger

1. Abre: `http://localhost:8081/v3/api-docs`
2. Copia el JSON
3. En Postman: **Import** → **Raw text** → Pega el JSON

---

## 📖 Swagger UI

Accede a la documentación interactiva:

```
http://localhost:8081/swagger-ui.html
```

### Autenticación en Swagger

1. Haz login con `/api/auth/login`
2. Copia el `accessToken` de la respuesta
3. Click en **"Authorize"** (candado verde)
4. Ingresa: `Bearer {tu_access_token}`
5. Click **"Authorize"**

Ahora puedes probar todos los endpoints directamente desde Swagger.

---

## 🔒 Seguridad

### Headers de Seguridad HTTP

La API incluye automáticamente:

```http
X-Frame-Options: DENY
X-Content-Type-Options: nosniff
Content-Security-Policy: default-src 'self'
Referrer-Policy: strict-origin-when-cross-origin
```

### Protecciones Implementadas

| Protección | Descripción |
|------------|-------------|
| **Rate Limiting** | Máximo 5 intentos de login fallidos → bloqueo 15 min |
| **Rotación de Tokens** | Refresh token cambia en cada renovación |
| **Blacklist de Tokens** | Tokens revocados no pueden reutilizarse |
| **Límite de Sesiones** | Máximo 3 sesiones simultáneas por usuario |
| **Auditoría Completa** | Log de IP, User-Agent, endpoint, resultado |
| **Alertas Automáticas** | Detección de ataques cada 5 minutos |
| **HTTPS Forzado** | Obligatorio en producción |
| **CORS Configurado** | Sin wildcard en producción |

---

## 👥 Roles y Permisos

### Roles del Sistema

| Rol | Descripción |
|-----|-------------|
| `ADMIN` | Acceso total a todos los módulos |
| `VENDEDOR` | Ventas, clientes, consulta de productos/precios |
| `ALMACENISTA` | Inventario, productos, almacenes |
| `COMPRADOR` | Compras, proveedores, consulta de productos |

### Matriz de Permisos por Defecto

| Módulo | ADMIN | VENDEDOR | ALMACENISTA | COMPRADOR |
|--------|-------|----------|-------------|-----------|
| Usuarios | ✅ CRUD | ❌ | ❌ | ❌ |
| Productos | ✅ CRUD | 👁️ Ver | ✅ CRUD | 👁️ Ver |
| Ventas | ✅ CRUD | ✅ CRUD | ❌ | ❌ |
| Clientes | ✅ CRUD | ✅ CRUD | ❌ | ❌ |
| Inventario | ✅ CRUD | ❌ | ✅ CRUD | ❌ |
| Compras | ✅ CRUD | ❌ | ❌ | ✅ CRUD |
| Proveedores | ✅ CRUD | ❌ | ❌ | ✅ CRUD |
| Reportes | ✅ | ✅ | 👁️ | 👁️ |
| Dashboard | ✅ | ✅ | ✅ | ✅ |
| Seguridad | ✅ | ❌ | ❌ | ❌ |

---

## 🧪 Ejemplos de Prueba Rápida

### 1. Registrar Admin
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@test.com","nombre":"Admin","password":"admin123","rol":"ADMIN"}'
```

### 2. Login
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@test.com","password":"admin123"}'
```

### 3. Listar Productos
```bash
curl -X GET http://localhost:8081/api/productos/visibles \
  -H "Authorization: Bearer {tu_access_token}"
```

---

## 📝 Códigos de Error

| Código | Significado |
|--------|-------------|
| 200 | Éxito |
| 201 | Creado exitosamente |
| 400 | Datos inválidos |
| 401 | No autenticado / Token inválido |
| 403 | Sin permisos (Forbidden) |
| 404 | Recurso no encontrado |
| 409 | Conflicto (ej: email duplicado) |
| 423 | Usuario bloqueado |
| 500 | Error interno del servidor |

---

## 🤝 Contribución

1. Fork el proyecto
2. Crea tu rama (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT.

---

## 📞 Soporte

- 📧 Email: soporte@llantasapi.com
- 📖 Documentación: `/swagger-ui.html`
- 🐛 Issues: GitHub Issues

---

**Desarrollado con ❤️ por el equipo de LlantasAPI**

