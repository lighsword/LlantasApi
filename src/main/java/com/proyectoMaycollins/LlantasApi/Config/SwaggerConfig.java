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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Configuración de Swagger/OpenAPI 3.0
 * Documentación interactiva de la API
 */
@Configuration
public class SwaggerConfig {
    private static final Logger log = LoggerFactory.getLogger(SwaggerConfig.class);

    @Bean
    public OpenAPI llantasApiDocumentation() {
        try {
            final String securitySchemeName = "Bearer Authentication";

            return new OpenAPI()
                    .info(new Info()
                            .title("🛞 LlantasAPI - Sistema de Gestión de Llantas")
                            .description("API REST de nivel empresarial para gestión integral de llantas, refacciones, herramientas, accesorios e insumos automotrices. " +
                                    "Incluye autenticación JWT, RBAC, auditoría completa y gestión multi-almacén.")
                            .version("2.1.0")
                            .contact(new Contact()
                                    .name("Equipo LlantasAPI")
                                    .email("soporte@llantasapi.com")
                                    .url("https://github.com/lighsword/LlantasApi"))
                            .license(new License()
                                    .name("MIT License")
                                    .url("https://opensource.org/licenses/MIT")))
                    .servers(List.of(
                            new Server()
                                    .url("http://localhost:8081")
                                    .description("Servidor Local"),
                            new Server()
                                    .url("https://tu-api.onrender.com")
                                    .description("Servidor Producción")
                    ))
                    .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                    .components(new Components()
                            .addSecuritySchemes(securitySchemeName,
                                    new SecurityScheme()
                                            .name(securitySchemeName)
                                            .type(SecurityScheme.Type.HTTP)
                                            .scheme("bearer")
                                            .bearerFormat("JWT")
                                            .description("Token JWT obtenido de /api/auth/login")));
        } catch (Exception e) {
            log.error("Error configurando OpenAPI", e);
            // Retornar configuración básica en caso de error
            return new OpenAPI()
                    .info(new Info()
                            .title("LlantasAPI")
                            .version("2.1.0"));
        }
    }
}

