package com.proyectoMaycollins.LlantasApi.Config.jwt;

import com.proyectoMaycollins.LlantasApi.Model.Usuarios;
import com.proyectoMaycollins.LlantasApi.Repository.UsuariosRepository;
import com.proyectoMaycollins.LlantasApi.Service.SesionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuariosRepository usuariosRepository;
    private final SesionService sesionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                // 1. Validar que el token no esté en la blacklist y tenga sesión activa
                if (!sesionService.validarToken(token)) {
                    log.warn("Token inválido o revocado detectado");
                    registrarAccesoDenegado(request, "Token inválido o revocado");
                    filterChain.doFilter(request, response);
                    return;
                }

                // 2. Extraer email del token
                String email = jwtService.extractSubject(token);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // 3. Validar que el token sea válido
                    if (!jwtService.validateToken(token, email)) {
                        log.warn("Token expirado o inválido para usuario: {}", email);
                        registrarAccesoDenegado(request, "Token expirado");
                        filterChain.doFilter(request, response);
                        return;
                    }

                    // 4. Buscar usuario en BD
                    Optional<Usuarios> opt = usuariosRepository.findByEmail(email);

                    if (opt.isPresent()) {
                        Usuarios u = opt.get();

                        // 5. Verificar que el usuario esté activo
                        if (!Boolean.TRUE.equals(u.getActivo())) {
                            log.warn("Intento de acceso de usuario inactivo: {}", email);
                            registrarAccesoDenegado(request, "Usuario inactivo");
                            filterChain.doFilter(request, response);
                            return;
                        }

                        // 6. Crear autenticación
                        String authority = u.getRol() == null ? null : u.getRol().name();
                        UserDetails userDetails = User.withUsername(u.getEmail())
                                .password(u.getPassword())
                                .authorities(authority == null ? Collections.emptyList() :
                                        Collections.singletonList(new SimpleGrantedAuthority(authority)))
                                .build();

                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);

                        // 7. Registrar acceso exitoso en auditoría
                        sesionService.registrarAuditoria(u.getId(), "ACCESS");
                    } else {
                        log.warn("Usuario no encontrado para email: {}", email);
                        registrarAccesoDenegado(request, "Usuario no encontrado");
                    }
                }
            } catch (Exception e) {
                log.error("Error procesando token JWT: {}", e.getMessage());
                registrarAccesoDenegado(request, "Error procesando token: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    private void registrarAccesoDenegado(HttpServletRequest request, String mensaje) {
        sesionService.registrarAuditoria(null, "ACCESS_DENIED");
    }

    private String obtenerIpCliente(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
