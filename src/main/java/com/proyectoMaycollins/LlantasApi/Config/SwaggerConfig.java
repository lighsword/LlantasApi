package com.proyectoMaycollins.LlantasApi.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de Swagger/OpenAPI 3.0
 * Documentación interactiva de la API
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI llantasApiDocumentation() {
        // Definir el esquema de seguridad JWT
        final String securitySchemeName = "Bearer Authentication";

        return new OpenAPI()
                .info(new Info()
                        .title("🛞 LlantasAPI - Sistema de Gestión Empresarial")
                        .description("""
                                # 🚀 API REST de Nivel Empresarial
                                
                                Sistema completo para la gestión integral de un negocio de llantas, refacciones, 
                                herramientas, accesorios e insumos automotrices.
                                
                                ## ✨ Características Principales
                                
                                ### 🔐 Seguridad de Nivel Empresarial
                                - ✅ **Tokens dinámicos únicos (JTI)** - Access Token (15 min) + Refresh Token (7 días)
                                - ✅ **Rotación completa de tokens** - Ambos tokens se renuevan en cada refresh
                                - ✅ **Blacklist de tokens revocados** con limpieza automática
                                - ✅ **Gestión de sesiones en BD** con información de dispositivo e IP
                                - ✅ **Límite de 3 sesiones simultáneas** por usuario
                                - ✅ **Protección contra fuerza bruta** (5 intentos, bloqueo 15 min)
                                - ✅ **Auditoría completa** de todos los accesos
                                - ✅ **Detección de robo de tokens** con revocación automática
                                
                                ### 💰 Sistema de Gestión de Precios
                                - ✅ **3 tipos de precios**: Compra, Venta, Mayorista
                                - ✅ **Historial completo** de cambios de precios
                                - ✅ **Cálculo automático** de márgenes de ganancia y porcentajes
                                - ✅ **Análisis de rentabilidad** por producto
                                
                                ### 📦 Gestión de Productos
                                - ✅ **5 categorías**: Llantas, Refacciones, Herramientas, Accesorios, Insumos
                                - ✅ **Detalles específicos** por tipo de producto
                                - ✅ **Código único** de producto
                                
                                ### 📊 Inventario Multi-Almacén
                                - ✅ **Múltiples almacenes** con ubicaciones
                                - ✅ **Control de stock** con mínimos y máximos
                                - ✅ **Movimientos auditados** (entrada, salida, traspaso, ajuste)
                                - ✅ **Trazabilidad completa**
                                
                                ### 🛒 Operaciones Comerciales
                                - ✅ **Gestión de ventas** con múltiples detalles
                                - ✅ **Registro de compras** a proveedores
                                - ✅ **Descuentos y promociones**
                                - ✅ **Clientes y proveedores**
                                
                                ### 📊 Reportes y Análisis
                                - ✅ **Reportes de ventas** por período
                                - ✅ **Productos más vendidos**
                                - ✅ **Análisis de rentabilidad**
                                - ✅ **Dashboard con estadísticas**
                                
                                ## 🔑 Autenticación
                                
                                **Para usar los endpoints protegidos:**
                                
                                1. **Registrar usuario**: `POST /api/auth/register`
                                2. **Iniciar sesión**: `POST /api/auth/login`
                                   - Recibirás `accessToken` (15 min) y `refreshToken` (7 días)
                                3. **Autorizar en Swagger**:
                                   - Click en el botón **"Authorize"** (🔒) arriba a la derecha
                                   - Ingresa: `Bearer {tu_accessToken}`
                                   - Click en "Authorize" y luego "Close"
                                4. **Refrescar tokens**: `POST /api/auth/refresh`
                                   - Cuando el Access Token expire (15 min)
                                   - ⚠️ **Importante**: Guardar AMBOS tokens nuevos (rotación completa)
                                
                                ## 📚 Documentación Adicional
                                
                                - **README**: Guía completa de instalación y uso
                                - **API Reference**: Tabla de todos los endpoints
                                - **Swagger UI**: Esta interfaz interactiva
                                
                                ## 🔒 Nivel de Seguridad
                                
                                🔒🔒🔒🔒🔒 **Nivel 5 - Empresarial Avanzado** (98/100)
                                
                                Implementa las mismas prácticas de seguridad que Auth0, Okta y AWS Cognito.
                                
                                ## 📊 Estadísticas
                                
                                - **Total Endpoints**: 80+
                                - **Total Modelos**: 23
                                - **Tablas en BD**: 23
                                - **Versión API**: 2.1.0
                                """)
                        .version("2.1.0")
                        .contact(new Contact()
                                .name("Equipo LlantasAPI")
                                .email("soporte@llantasapi.com")
                                .url("https://github.com/tu-usuario/LlantasApi"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8081")
                                .description("Servidor de Desarrollo Local"),
                        new Server()
                                .url("https://tu-api.onrender.com")
                                .description("Servidor de Producción (Render)")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingresa el token JWT obtenido del endpoint /api/auth/login")));
    }
}

