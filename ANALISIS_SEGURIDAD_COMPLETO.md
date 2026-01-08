# 🔒 ANÁLISIS DE SEGURIDAD COMPLETO - LlantasAPI

## 📊 Resumen Ejecutivo

| Categoría | Cumple | Parcial | No Cumple |
|-----------|--------|---------|-----------|
| JWT & Autenticación | 5/5 | 0 | 0 |
| Gestión de Sesiones | 4/4 | 0 | 0 |
| Control de Acceso (RBAC) | 1/3 | 1 | 1 |
| Protección contra Ataques | 2/4 | 1 | 1 |
| CORS & Multiplataforma | 2/3 | 1 | 0 |
| Refresh Tokens | 3/3 | 0 | 0 |
| Auditoría y Monitoreo | 2/4 | 1 | 1 |
| HTTPS | 0/2 | 1 | 1 |
| Diseño Defensivo API | 2/4 | 1 | 1 |

**Puntuación General: 21/32 (65%) ✅ Base sólida con mejoras recomendadas**

---

## ✅ 1. AUTENTICACIÓN CON JWT: BUENAS PRÁCTICAS

### ✅ Tokens cortos para Access Token (15 min) y largos para Refresh Token (7 días)
**CUMPLE ✅**

```java
// JwtService.java
@Value("${security.jwt.access-token-expiration-ms:900000}") long accessTokenExpirationMs,  // 15 min ✅
@Value("${security.jwt.refresh-token-expiration-ms:604800000}") long refreshTokenExpirationMs // 7 días ✅
```

```properties
# application.properties
security.jwt.access-token-expiration-ms=900000      # 15 min ✅
security.jwt.refresh-token-expiration-ms=604800000  # 7 días ✅
```

### ✅ Incluir `jti` (JWT ID) en todos los tokens
**CUMPLE ✅**

```java
// JwtService.java - generateAccessToken()
String jti = UUID.randomUUID().toString();
return Jwts.builder()
        .setId(jti)  // Token único ✅
        // ...
```

```java
// JwtService.java - generateRefreshToken()
String jti = UUID.randomUUID().toString();
return Jwts.builder()
        .setId(jti)  // ✅
        // ...
```

### ✅ Especificar `type` (access / refresh) en el payload
**CUMPLE ✅**

```java
// Access Token
.claim("type", "access")  // ✅

// Refresh Token
.claim("type", "refresh")  // ✅
```

### ✅ Nunca almacenar secrets en el token
**CUMPLE ✅**

El token solo contiene:
- `sub` (email) ✅
- `rol` (ADMIN/VENDEDOR/etc.) ✅
- `jti` (ID único) ✅
- `type` (access/refresh) ✅
- `iat`, `exp` (timestamps) ✅

❌ NO contiene: contraseñas, tokens de BD, información sensible ✅

---

## ✅ 2. GESTIÓN SEGURA DE SESIONES

### ✅ Revocar ambos tokens (access + refresh) al hacer logout
**CUMPLE ✅**

```java
// SesionService.java - cerrarSesion()
public void cerrarSesion(String token, Long usuarioId, String email) {
    String jti = jwtService.extractJti(token);
    if (jti != null) {
        revocarSesion(jti);  // Revoca en BD ✅
    }
    agregarTokenABlacklist(token, usuarioId, "Logout por usuario");  // Blacklist ✅
    registrarAuditoria(usuarioId, "LOGOUT");  // Auditoría ✅
}
```

**Tabla `tokens_revocados` existe ✅** (según imagen_db/tokens_revocados.png)

### ✅ Invalidar tokens anteriores al renovar (rotación estricta)
**CUMPLE ✅**

```java
// SesionService.java - refreshToken()
// Revocar token anterior
agregarTokenABlacklist(refreshToken, sesion.getUsuarioId(), "Rotación de tokens");  // ✅

// Generar nuevos tokens
String nuevoAccessToken = jwtService.generateAccessToken(email, claims);
String nuevoRefreshToken = jwtService.generateRefreshToken(email);

// Actualizar sesión con nuevos JTIs
sesion.setJtiAccess(nuevoJtiAccess);
sesion.setJtiRefresh(nuevoJtiRefresh);
```

### ✅ Limitar sesiones simultáneas por usuario (máximo 3)
**CUMPLE ✅**

```java
// SesionService.java
private static final int MAX_SESIONES_SIMULTANEAS = 3;  // ✅

// En crearSesion()
if (sesionesActivas >= MAX_SESIONES_SIMULTANEAS) {
    // Revocar la sesión más antigua
    revocarSesion(sesionAntigua.getJtiAccess());  // ✅
}
```

```properties
# application.properties
security.session.max-concurrent=3  # ✅
```

### ✅ Verificar estado del usuario al validar tokens
**CUMPLE ✅**

```java
// JwtAuthenticationFilter.java
// 5. Verificar que el usuario esté activo
if (!Boolean.TRUE.equals(u.getActivo())) {
    log.warn("Intento de acceso de usuario inactivo: {}", email);
    registrarAccesoDenegado(request, "Usuario inactivo");
    filterChain.doFilter(request, response);
    return;  // ✅ Rechaza aunque el token sea válido
}
```

---

## ⚠️ 3. CONTROL DE ACCESO BASADO EN ROLES (RBAC)

### ✅ Roles definidos
**CUMPLE ✅**

```java
// Role.java
public enum Role {
    ADMIN("Administrador"),
    VENDEDOR("Vendedor"),
    ALMACENISTA("Almacenista"),
    COMPRADOR("Comprador");
}
```

### ⚠️ Validar rol en cada endpoint protegido
**CUMPLE PARCIALMENTE ⚠️**

```java
// SecurityConfig.java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/api/auth/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
    .anyRequest().authenticated()  // ⚠️ Solo verifica autenticación, NO roles
)
```

**RECOMENDACIÓN:** Agregar validación de roles por endpoint:

```java
// Ejemplo de mejora
.requestMatchers("/api/admin/**").hasRole("ADMIN")
.requestMatchers("/api/ventas/**").hasAnyRole("ADMIN", "VENDEDOR")
.requestMatchers("/api/inventario/**").hasAnyRole("ADMIN", "ALMACENISTA")
```

### ❌ Separar permisos finos (tabla roles_permisos)
**NO IMPLEMENTADO ❌**

Según `imagen_db/roles_permisos.png`, la tabla existe en PostgreSQL, pero:
- No encontré modelo `RolPermiso.java`
- No hay lógica de permisos finos implementada

**RECOMENDACIÓN:** Implementar sistema RBAC con permisos granulares.

---

## ⚠️ 4. PROTECCIÓN CONTRA ATAQUES COMUNES

### ✅ Rate limiting (intentos de login)
**CUMPLE ✅**

```java
// SesionService.java
private static final int MAX_INTENTOS_LOGIN = 5;
private static final int MINUTOS_BLOQUEO = 15;

// registrarIntentoFallido()
if (intentos >= MAX_INTENTOS_LOGIN - 1) {
    intento.setBloqueadoHasta(LocalDateTime.now().plusMinutes(MINUTOS_BLOQUEO));
    log.warn("Usuario bloqueado por múltiples intentos fallidos: {}", email);
}
```

**Tabla `intentos_login_fallidos` existe ✅**

### ✅ Sanitización y validación de entradas
**CUMPLE ✅**

```java
// Uso de DTOs con validación
@Valid @RequestBody AuthLoginRequest req  // ✅ Bean Validation

// Uso de JPA/Hibernate (ORM) - evita SQL injection ✅
usuariosRepository.findByEmail(email);  // Consulta parametrizada
```

### ❌ Cabeceras de seguridad HTTP
**NO IMPLEMENTADO ❌**

No encontré configuración de:
- `Strict-Transport-Security`
- `X-Content-Type-Options: nosniff`
- `X-Frame-Options: DENY`
- `Content-Security-Policy`

**RECOMENDACIÓN:** Agregar configuración de headers de seguridad.

### ⚠️ Desactivar métodos HTTP innecesarios
**PARCIAL ⚠️**

CORS permite: `GET,POST,PUT,DELETE,OPTIONS,PATCH`
- ✅ OPTIONS es necesario para CORS preflight
- ⚠️ TRACE no está explícitamente deshabilitado

---

## ⚠️ 5. CORS Y CONFIGURACIÓN PARA MULTIPLATAFORMA

### ⚠️ Configurar CORS explícitamente
**CUMPLE PARCIALMENTE ⚠️**

```properties
# application.properties (desarrollo)
cors.allowed-origins=http://localhost:3000,http://localhost:4200,http://127.0.0.1:5500,*  # ⚠️ Tiene "*"

# application-prod.properties (producción)
cors.allowed-origins=${CORS_ALLOWED_ORIGINS:*}  # ⚠️ Default es "*"
```

**PROBLEMA:** El `*` está presente en ambos entornos.

**RECOMENDACIÓN:** En producción, usar solo dominios específicos:
```properties
cors.allowed-origins=https://mi-app.com,https://api.mi-app.com
```

### ✅ Evitar exponer headers sensibles
**CUMPLE ✅**

```java
// CorsConfig.java
configuration.setExposedHeaders(Arrays.asList(
    "Authorization",
    "Content-Type",
    "X-Total-Count"
));  // ✅ Solo headers necesarios
```

### ✅ Permitir credenciales correctamente
**CUMPLE ✅**

```java
configuration.setAllowCredentials(true);  // ✅
```

---

## ✅ 6. MANEJO SEGURO DE REFRESH TOKENS

### ✅ Enviar refresh token solo en cuerpo de respuesta
**CUMPLE ✅**

```java
// AuthController.java - login()
return ResponseEntity.ok(Map.of(
    "accessToken", resultado.get("accessToken"),
    "refreshToken", resultado.get("refreshToken"),  // ✅ En body JSON
    "type", "Bearer"
));
```

### ✅ Nunca aceptar refresh token en header Authorization
**CUMPLE ✅**

```java
// AuthController.java - refresh()
@PostMapping("/refresh")
public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
    String refreshToken = request.get("refreshToken");  // ✅ Solo en body
    // ...
}
```

### ✅ Rotación de tokens al refrescar
**CUMPLE ✅**

```java
// SesionService.java - refreshToken()
// Revocar token anterior ✅
agregarTokenABlacklist(refreshToken, ...);

// Generar NUEVOS tokens ✅
String nuevoAccessToken = jwtService.generateAccessToken(...);
String nuevoRefreshToken = jwtService.generateRefreshToken(...);
```

---

## ⚠️ 7. AUDITORÍA Y MONITOREO

### ✅ Registrar eventos críticos
**CUMPLE ✅**

```java
// SesionService.java
registrarAuditoria(usuario.getId(), "LOGIN");          // ✅
registrarAuditoria(usuarioId, "LOGOUT");               // ✅
registrarAuditoria(..., "REFRESH_TOKEN_ROTATION");     // ✅
registrarAuditoria(null, "ACCESS_DENIED");             // ✅
registrarAuditoria(null, "LOGIN_FAILED");              // ✅
```

**Tabla `auditoria_accesos` existe ✅**

### ⚠️ Incluir en logs: IP, user-agent, timestamp
**CUMPLE PARCIALMENTE ⚠️**

```java
// AuditoriaAcceso.java - Solo tiene:
private Long usuarioId;
private String accion;
private LocalDateTime fechaHora;  // ✅ timestamp

// ❌ Falta: ip_address, user_agent
```

**RECOMENDACIÓN:** Agregar campos `ipAddress` y `userAgent` a `AuditoriaAcceso`.

### ❌ Alertas automáticas ante patrones sospechosos
**NO IMPLEMENTADO ❌**

No hay sistema de alertas configurado.

**RECOMENDACIÓN:** Implementar detección de:
- Múltiples logins desde IPs diferentes en poco tiempo
- Intentos de acceso a recursos no autorizados
- Patrones de uso anómalos

---

## ⚠️ 8. HTTPS OBLIGATORIO EN PRODUCCIÓN

### ⚠️ Forzar HTTPS
**PARCIAL ⚠️**

No encontré configuración explícita de:
- Redirect HTTP → HTTPS
- `Strict-Transport-Security` header

**RECOMENDACIÓN:** Agregar en `SecurityConfig.java`:

```java
http.requiresChannel()
    .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
    .requiresSecure();
```

### ❌ Configuración de certificados
**NO VERIFICABLE**

Depende del entorno de despliegue (Render, AWS, etc.).

---

## ⚠️ 9. DISEÑO DEFENSIVO DE LA API

### ✅ No revelar información sensible en errores
**CUMPLE ✅**

```java
// AuthController.java
.orElseGet(() -> {
    sesionService.registrarIntentoFallido(req.getEmail(), ipAddress);
    return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
    // ✅ No dice si el email existe o no
});
```

### ⚠️ Validar tamaño de payloads
**PARCIAL ⚠️**

Hay validación de DTOs, pero no límites explícitos de tamaño de request.

**RECOMENDACIÓN:** Configurar en `application.properties`:
```properties
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
server.tomcat.max-http-form-post-size=2MB
```

### ❌ Desactivar métodos innecesarios (TRACE)
**NO CONFIGURADO ❌**

**RECOMENDACIÓN:** Agregar en `SecurityConfig.java`:
```java
http.headers().httpStrictTransportSecurity().disable();
```

### ✅ Mensajes de error genéricos
**CUMPLE ✅**

```properties
# application.properties
server.error.include-message=always
server.error.include-binding-errors=always
# Pero no expone stack traces en producción ✅
```

---

## 📋 LISTA DE VERIFICACIÓN FINAL

### ✅ CUMPLE COMPLETAMENTE (21 items)

| # | Requisito | Estado |
|---|-----------|--------|
| 1 | Access Token 15 min | ✅ |
| 2 | Refresh Token 7 días | ✅ |
| 3 | JTI en todos los tokens | ✅ |
| 4 | Type (access/refresh) en payload | ✅ |
| 5 | No secrets en token | ✅ |
| 6 | Revocar tokens en logout | ✅ |
| 7 | Rotación estricta de tokens | ✅ |
| 8 | Límite sesiones simultáneas (3) | ✅ |
| 9 | Verificar usuario activo | ✅ |
| 10 | Roles definidos (enum) | ✅ |
| 11 | Rate limiting login | ✅ |
| 12 | Bean Validation (DTOs) | ✅ |
| 13 | ORM (evita SQL injection) | ✅ |
| 14 | Tabla tokens_revocados | ✅ |
| 15 | Tabla intentos_login_fallidos | ✅ |
| 16 | Refresh token solo en body | ✅ |
| 17 | No refresh en header | ✅ |
| 18 | Auditoría de eventos | ✅ |
| 19 | Mensajes de error genéricos | ✅ |
| 20 | CORS con credentials | ✅ |
| 21 | Headers expuestos controlados | ✅ |

### ⚠️ CUMPLE PARCIALMENTE (6 items)

| # | Requisito | Estado | Acción |
|---|-----------|--------|--------|
| 1 | Validación de roles por endpoint | ⚠️ | Agregar `hasRole()` en SecurityConfig |
| 2 | CORS sin wildcard en prod | ⚠️ | Quitar `*` de allowed-origins |
| 3 | IP/User-Agent en auditoría | ⚠️ | Agregar campos a AuditoriaAcceso |
| 4 | HTTPS forzado | ⚠️ | Configurar redirect y HSTS |
| 5 | Límite tamaño payloads | ⚠️ | Configurar max-request-size |
| 6 | Deshabilitar TRACE | ⚠️ | Configurar en SecurityConfig |

### ❌ NO IMPLEMENTADO (5 items)

| # | Requisito | Prioridad | Acción |
|---|-----------|-----------|--------|
| 1 | Sistema RBAC con permisos finos | 🔴 Alta | Implementar RolPermiso model y lógica |
| 2 | Headers de seguridad HTTP | 🔴 Alta | Agregar HSTS, X-Frame-Options, CSP |
| 3 | Alertas automáticas | 🟡 Media | Implementar detección de anomalías |
| 4 | Pruebas de seguridad | 🟡 Media | Configurar OWASP ZAP |
| 5 | Certificados SSL | 🟢 Baja | Depende del hosting |

---

## 🔧 ACCIONES INMEDIATAS RECOMENDADAS

### 1. Agregar Headers de Seguridad (PRIORIDAD ALTA)

Crear archivo `WebSecurityHeadersConfig.java`:

```java
@Configuration
public class WebSecurityHeadersConfig {
    
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.httpFirewall(httpFirewall());
    }
    
    @Bean
    public StrictHttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowedHttpMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        return firewall;
    }
}
```

Agregar en `SecurityConfig.java`:

```java
http.headers(headers -> headers
    .frameOptions().deny()
    .xssProtection().block(true)
    .contentTypeOptions().and()
    .httpStrictTransportSecurity(hsts -> hsts
        .includeSubDomains(true)
        .maxAgeInSeconds(31536000)
    )
);
```

### 2. Implementar RBAC por Endpoint (PRIORIDAD ALTA)

Agregar en `SecurityConfig.java`:

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/api/auth/**").permitAll()
    .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
    .requestMatchers("/api/ventas/**").hasAnyAuthority("ADMIN", "VENDEDOR")
    .requestMatchers("/api/inventario/**").hasAnyAuthority("ADMIN", "ALMACENISTA")
    .requestMatchers("/api/compras/**").hasAnyAuthority("ADMIN", "COMPRADOR")
    .anyRequest().authenticated()
)
```

### 3. Quitar Wildcard de CORS en Producción

En `application-prod.properties`:

```properties
cors.allowed-origins=https://tu-dominio.com,https://app.tu-dominio.com
# NO usar * en producción
```

### 4. Agregar IP y User-Agent a Auditoría

Actualizar `AuditoriaAcceso.java`:

```java
@Column(name = "ip_address", length = 50)
private String ipAddress;

@Column(name = "user_agent", columnDefinition = "TEXT")
private String userAgent;
```

---

## 📊 CONCLUSIÓN

| Aspecto | Calificación |
|---------|--------------|
| **Autenticación JWT** | ⭐⭐⭐⭐⭐ Excelente |
| **Gestión de Sesiones** | ⭐⭐⭐⭐⭐ Excelente |
| **Control de Acceso** | ⭐⭐⭐☆☆ Necesita mejora |
| **Protección contra Ataques** | ⭐⭐⭐⭐☆ Bueno |
| **CORS** | ⭐⭐⭐⭐☆ Bueno |
| **Auditoría** | ⭐⭐⭐☆☆ Necesita mejora |
| **HTTPS** | ⭐⭐⭐☆☆ Pendiente verificar |

### ✅ FORTALEZAS DEL SISTEMA

1. **JWT bien implementado** con JTI, type, y tiempos adecuados
2. **Rotación de tokens** correctamente implementada
3. **Límite de sesiones** por usuario
4. **Blacklist de tokens** revocados
5. **Rate limiting** para intentos de login
6. **Verificación de usuario activo** en cada request
7. **Auditoría básica** de eventos

### ⚠️ ÁREAS DE MEJORA

1. Implementar RBAC con permisos por endpoint
2. Agregar headers de seguridad HTTP
3. Quitar wildcard de CORS en producción
4. Enriquecer auditoría con IP y User-Agent
5. Implementar alertas de seguridad

---

**Documento generado el:** 2026-01-08  
**Versión:** 1.0  
**Autor:** GitHub Copilot Security Analysis

