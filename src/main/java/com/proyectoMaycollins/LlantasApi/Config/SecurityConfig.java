package com.proyectoMaycollins.LlantasApi.Config;

import com.proyectoMaycollins.LlantasApi.Config.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfigurationSource corsConfigurationSource;
    private final Environment environment;

    @Value("${security.https.required:false}")
    private boolean httpsRequired;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Headers de seguridad HTTP
            .headers(headers -> headers
                // X-Frame-Options: Previene clickjacking
                .frameOptions(frame -> frame.deny())
                // X-Content-Type-Options: Previene MIME sniffing
                .contentTypeOptions(content -> {})
                // X-XSS-Protection: Protección XSS adicional
                .xssProtection(xss -> {})
                // Referrer-Policy: Controla información enviada en cabecera Referer
                .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                // Content-Security-Policy: Política de seguridad de contenido
                .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'; frame-ancestors 'none'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self'"))
                // Cache-Control para respuestas
                .cacheControl(cache -> {})
            )

            // HTTPS forzado en producción
            .requiresChannel(channel -> {
                if (isProduccion()) {
                    channel.anyRequest().requiresSecure();
                }
            })

            // Autorización por endpoints con RBAC
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()

                // Endpoints de administración - Solo ADMIN
                .requestMatchers("/api/usuarios/**").hasAuthority("ADMIN")
                .requestMatchers("/api/permisos/**").hasAuthority("ADMIN")
                .requestMatchers("/actuator/**").hasAuthority("ADMIN")

                // Endpoints de ventas - ADMIN y VENDEDOR
                .requestMatchers(HttpMethod.GET, "/api/ventas/**").hasAnyAuthority("ADMIN", "VENDEDOR")
                .requestMatchers(HttpMethod.POST, "/api/ventas/**").hasAnyAuthority("ADMIN", "VENDEDOR")
                .requestMatchers(HttpMethod.PUT, "/api/ventas/**").hasAnyAuthority("ADMIN", "VENDEDOR")
                .requestMatchers(HttpMethod.DELETE, "/api/ventas/**").hasAuthority("ADMIN")

                // Endpoints de clientes - ADMIN y VENDEDOR
                .requestMatchers("/api/clientes/**").hasAnyAuthority("ADMIN", "VENDEDOR")

                // Endpoints de inventario - ADMIN y ALMACENISTA
                .requestMatchers("/api/inventario/**").hasAnyAuthority("ADMIN", "ALMACENISTA")
                .requestMatchers("/api/movimientos-inventario/**").hasAnyAuthority("ADMIN", "ALMACENISTA")
                .requestMatchers("/api/almacenes/**").hasAnyAuthority("ADMIN", "ALMACENISTA")

                // Endpoints de compras - ADMIN y COMPRADOR
                .requestMatchers(HttpMethod.GET, "/api/compras/**").hasAnyAuthority("ADMIN", "COMPRADOR")
                .requestMatchers(HttpMethod.POST, "/api/compras/**").hasAnyAuthority("ADMIN", "COMPRADOR")
                .requestMatchers(HttpMethod.PUT, "/api/compras/**").hasAnyAuthority("ADMIN", "COMPRADOR")
                .requestMatchers(HttpMethod.DELETE, "/api/compras/**").hasAuthority("ADMIN")

                // Endpoints de proveedores - ADMIN y COMPRADOR
                .requestMatchers("/api/proveedores/**").hasAnyAuthority("ADMIN", "COMPRADOR")

                // Endpoints de productos - Lectura para todos, escritura limitada
                .requestMatchers(HttpMethod.GET, "/api/productos/**").hasAnyAuthority("ADMIN", "VENDEDOR", "ALMACENISTA", "COMPRADOR")
                .requestMatchers(HttpMethod.POST, "/api/productos/**").hasAnyAuthority("ADMIN", "ALMACENISTA")
                .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasAnyAuthority("ADMIN", "ALMACENISTA")
                .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasAuthority("ADMIN")

                // Endpoints de categorías - Lectura para todos, escritura solo ADMIN
                .requestMatchers(HttpMethod.GET, "/api/categorias/**").hasAnyAuthority("ADMIN", "VENDEDOR", "ALMACENISTA", "COMPRADOR")
                .requestMatchers(HttpMethod.POST, "/api/categorias/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/categorias/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/categorias/**").hasAuthority("ADMIN")

                // Endpoints de precios - Lectura para varios, escritura solo ADMIN
                .requestMatchers(HttpMethod.GET, "/api/precios/**").hasAnyAuthority("ADMIN", "VENDEDOR", "COMPRADOR")
                .requestMatchers(HttpMethod.POST, "/api/precios/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/precios/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/precios/**").hasAuthority("ADMIN")

                // Endpoints de promociones - ADMIN y VENDEDOR
                .requestMatchers("/api/promociones/**").hasAnyAuthority("ADMIN", "VENDEDOR")

                // Endpoints de reportes - ADMIN y acceso parcial otros
                .requestMatchers("/api/reportes/**").hasAnyAuthority("ADMIN", "VENDEDOR", "ALMACENISTA", "COMPRADOR")

                // Dashboard - Todos los autenticados
                .requestMatchers("/api/dashboard/**").hasAnyAuthority("ADMIN", "VENDEDOR", "ALMACENISTA", "COMPRADOR")

                // Notificaciones - Todos los autenticados
                .requestMatchers("/api/notificaciones/**").authenticated()

                // Enums públicos
                .requestMatchers("/api/enums/**").authenticated()

                // Cualquier otro request requiere autenticación
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Firewall HTTP estricto que deshabilita métodos innecesarios como TRACE
     */
    @Bean
    public HttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        // Solo permitir métodos HTTP necesarios (deshabilita TRACE, TRACK, etc.)
        firewall.setAllowedHttpMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        // No permitir URLs con punto y coma (previene ataques de path parameter)
        firewall.setAllowSemicolon(false);
        // No permitir backslash en URLs
        firewall.setAllowBackSlash(false);
        // No permitir URL encoding de slash
        firewall.setAllowUrlEncodedSlash(false);
        // No permitir porcentajes dobles
        firewall.setAllowUrlEncodedDoubleSlash(false);
        return firewall;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Verifica si está en entorno de producción
     */
    private boolean isProduccion() {
        return httpsRequired ||
               Arrays.asList(environment.getActiveProfiles()).contains("prod") ||
               Arrays.asList(environment.getActiveProfiles()).contains("production");
    }
}
