package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.Permisos;
import com.proyectoMaycollins.LlantasApi.Model.enums.Accion;
import com.proyectoMaycollins.LlantasApi.Model.enums.Modulo;
import com.proyectoMaycollins.LlantasApi.Model.enums.Role;
import com.proyectoMaycollins.LlantasApi.Repository.PermisosRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;

/**
 * Servicio para manejar permisos RBAC (Role-Based Access Control)
 * Valida permisos granulares por rol, módulo y acción
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PermisosService {

    private final PermisosRepository permisosRepository;

    // Cache de permisos para evitar consultas repetidas
    private Map<String, Boolean> cachePermisos = new HashMap<>();

    /**
     * Inicializa permisos por defecto si no existen
     */
    @PostConstruct
    public void inicializarPermisosDefecto() {
        if (permisosRepository.count() == 0) {
            log.info("Inicializando permisos por defecto...");
            crearPermisosDefecto();
        }
        // Cargar cache
        cargarCachePermisos();
    }

    /**
     * Verifica si el usuario actual tiene permiso para una acción en un módulo
     */
    public boolean tienePermiso(Modulo modulo, Accion accion) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        String rolUsuario = obtenerRolUsuario(auth);
        if (rolUsuario == null) {
            return false;
        }

        // ADMIN siempre tiene todos los permisos
        if ("ADMIN".equals(rolUsuario)) {
            return true;
        }

        return verificarPermiso(rolUsuario, modulo, accion);
    }

    /**
     * Verifica permiso dado un rol específico
     */
    public boolean verificarPermiso(String rolStr, Modulo modulo, Accion accion) {
        try {
            Role rol = Role.valueOf(rolStr.toUpperCase());

            // Verificar en cache primero
            String cacheKey = generarCacheKey(rol, modulo, accion);
            if (cachePermisos.containsKey(cacheKey)) {
                return cachePermisos.get(cacheKey);
            }

            // Consultar BD
            boolean tienePermiso = permisosRepository.tienePermiso(rol, modulo, accion);
            cachePermisos.put(cacheKey, tienePermiso);

            return tienePermiso;
        } catch (IllegalArgumentException e) {
            log.warn("Rol no válido: {}", rolStr);
            return false;
        }
    }

    /**
     * Obtiene todos los permisos de un rol
     */
    public List<Permisos> obtenerPermisosPorRol(Role rol) {
        return permisosRepository.findByRolAndPermitidoTrue(rol);
    }

    /**
     * Verifica si un rol tiene permiso para una acción en un módulo
     */
    public boolean tienePermiso(Role rol, Modulo modulo, Accion accion) {
        // ADMIN siempre tiene todos los permisos
        if (rol == Role.ADMIN) {
            return true;
        }
        return verificarPermiso(rol.name(), modulo, accion);
    }

    /**
     * Obtiene los permisos en formato para frontend (mapa de módulo -> acciones permitidas)
     */
    public Map<String, Map<String, Boolean>> obtenerPermisosParaFrontend(Role rol) {
        Map<String, Map<String, Boolean>> resultado = new HashMap<>();

        // Inicializar todos los módulos con todas las acciones en false
        for (Modulo modulo : Modulo.values()) {
            Map<String, Boolean> acciones = new HashMap<>();
            for (Accion accion : Accion.values()) {
                acciones.put(accion.name(), false);
            }
            resultado.put(modulo.name(), acciones);
        }

        // Si es ADMIN, todas las acciones son true
        if (rol == Role.ADMIN) {
            for (String modulo : resultado.keySet()) {
                for (String accion : resultado.get(modulo).keySet()) {
                    resultado.get(modulo).put(accion, true);
                }
            }
            return resultado;
        }

        // Obtener permisos del rol y actualizar el mapa
        List<Permisos> permisos = permisosRepository.findByRolAndPermitidoTrue(rol);
        for (Permisos p : permisos) {
            if (resultado.containsKey(p.getModulo().name())) {
                resultado.get(p.getModulo().name()).put(p.getAccion().name(), p.getPermitido());
            }
        }

        return resultado;
    }

    /**
     * Obtiene la matriz completa de permisos: Rol -> Módulo -> Lista de Acciones permitidas
     */
    public Map<Role, Map<Modulo, List<Accion>>> obtenerMatrizPermisos() {
        Map<Role, Map<Modulo, List<Accion>>> matriz = new HashMap<>();

        for (Role rol : Role.values()) {
            Map<Modulo, List<Accion>> modulosAcciones = new HashMap<>();

            List<Permisos> permisosRol = permisosRepository.findByRolAndPermitidoTrue(rol);

            for (Permisos p : permisosRol) {
                modulosAcciones
                    .computeIfAbsent(p.getModulo(), k -> new ArrayList<>())
                    .add(p.getAccion());
            }

            matriz.put(rol, modulosAcciones);
        }

        return matriz;
    }

    /**
     * Inicializa los permisos por defecto (método público para llamar desde controller)
     */
    public void inicializarPermisosPorDefecto() {
        crearPermisosDefecto();
        invalidarCache();
        log.info("Permisos por defecto inicializados manualmente");
    }

    /**
     * Guarda o actualiza un permiso específico
     */
    public Permisos guardarPermiso(Role rol, Modulo modulo, Accion accion, boolean permitido, String descripcion) {
        // Buscar si ya existe
        Optional<Permisos> existente = permisosRepository.findByRolAndModuloAndAccion(rol, modulo, accion);

        Permisos permiso;
        if (existente.isPresent()) {
            permiso = existente.get();
            permiso.setPermitido(permitido);
            if (descripcion != null) {
                permiso.setDescripcion(descripcion);
            }
        } else {
            permiso = Permisos.builder()
                    .rol(rol)
                    .modulo(modulo)
                    .accion(accion)
                    .permitido(permitido)
                    .descripcion(descripcion != null ? descripcion : "Permiso creado manualmente")
                    .build();
        }

        Permisos guardado = permisosRepository.save(permiso);

        // Actualizar cache
        String cacheKey = generarCacheKey(rol, modulo, accion);
        cachePermisos.put(cacheKey, permitido);

        log.info("Permiso guardado: {} - {} - {} = {}", rol, modulo, accion, permitido);

        return guardado;
    }

    /**
     * Obtiene el rol del usuario autenticado
     */
    private String obtenerRolUsuario(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_") || Arrays.stream(Role.values()).anyMatch(r -> r.name().equals(a)))
                .map(a -> a.replace("ROLE_", ""))
                .findFirst()
                .orElse(null);
    }

    private String generarCacheKey(Role rol, Modulo modulo, Accion accion) {
        return String.format("%s_%s_%s", rol.name(), modulo.name(), accion.name());
    }

    private void cargarCachePermisos() {
        List<Permisos> todosPermisos = permisosRepository.findAll();
        for (Permisos p : todosPermisos) {
            String key = generarCacheKey(p.getRol(), p.getModulo(), p.getAccion());
            cachePermisos.put(key, p.getPermitido());
        }
        log.info("Cache de permisos cargado: {} entradas", cachePermisos.size());
    }

    /**
     * Invalida el cache de permisos (llamar después de modificar permisos)
     */
    public void invalidarCache() {
        cachePermisos.clear();
        cargarCachePermisos();
    }

    /**
     * Crea los permisos por defecto para cada rol
     */
    private void crearPermisosDefecto() {
        List<Permisos> permisosDefecto = new ArrayList<>();

        // ADMIN: Todos los permisos en todos los módulos
        for (Modulo modulo : Modulo.values()) {
            for (Accion accion : Accion.values()) {
                permisosDefecto.add(crearPermiso(Role.ADMIN, modulo, accion, true, "Permiso completo para administrador"));
            }
        }

        // VENDEDOR: Permisos en ventas, clientes y productos (lectura)
        permisosDefecto.addAll(crearPermisosVendedor());

        // ALMACENISTA: Permisos en inventario y productos
        permisosDefecto.addAll(crearPermisosAlmacenista());

        // COMPRADOR: Permisos en compras y proveedores
        permisosDefecto.addAll(crearPermisosComprador());

        permisosRepository.saveAll(permisosDefecto);
        log.info("Permisos por defecto creados: {} registros", permisosDefecto.size());
    }

    private List<Permisos> crearPermisosVendedor() {
        List<Permisos> permisos = new ArrayList<>();

        // Ventas: CRUD completo
        for (Accion accion : Arrays.asList(Accion.VER, Accion.CREAR, Accion.EDITAR, Accion.IMPRIMIR)) {
            permisos.add(crearPermiso(Role.VENDEDOR, Modulo.VENTAS, accion, true, "Vendedor puede gestionar ventas"));
        }

        // Clientes: CRUD completo
        for (Accion accion : Arrays.asList(Accion.VER, Accion.CREAR, Accion.EDITAR)) {
            permisos.add(crearPermiso(Role.VENDEDOR, Modulo.CLIENTES, accion, true, "Vendedor puede gestionar clientes"));
        }

        // Productos: Solo lectura
        permisos.add(crearPermiso(Role.VENDEDOR, Modulo.PRODUCTOS, Accion.VER, true, "Vendedor puede ver productos"));

        // Precios: Solo lectura
        permisos.add(crearPermiso(Role.VENDEDOR, Modulo.PRECIOS, Accion.VER, true, "Vendedor puede ver precios"));

        // Dashboard: Solo lectura
        permisos.add(crearPermiso(Role.VENDEDOR, Modulo.DASHBOARD, Accion.VER, true, "Vendedor puede ver dashboard"));

        // Reportes de ventas
        permisos.add(crearPermiso(Role.VENDEDOR, Modulo.REPORTES, Accion.VER, true, "Vendedor puede ver reportes"));
        permisos.add(crearPermiso(Role.VENDEDOR, Modulo.REPORTES, Accion.EXPORTAR, true, "Vendedor puede exportar reportes"));

        return permisos;
    }

    private List<Permisos> crearPermisosAlmacenista() {
        List<Permisos> permisos = new ArrayList<>();

        // Inventario: CRUD completo
        for (Accion accion : Arrays.asList(Accion.VER, Accion.CREAR, Accion.EDITAR, Accion.AJUSTAR)) {
            permisos.add(crearPermiso(Role.ALMACENISTA, Modulo.INVENTARIO, accion, true, "Almacenista gestiona inventario"));
        }

        // Productos: CRUD
        for (Accion accion : Arrays.asList(Accion.VER, Accion.CREAR, Accion.EDITAR)) {
            permisos.add(crearPermiso(Role.ALMACENISTA, Modulo.PRODUCTOS, accion, true, "Almacenista gestiona productos"));
        }

        // Almacenes: CRUD
        for (Accion accion : Arrays.asList(Accion.VER, Accion.CREAR, Accion.EDITAR)) {
            permisos.add(crearPermiso(Role.ALMACENISTA, Modulo.ALMACENES, accion, true, "Almacenista gestiona almacenes"));
        }

        // Categorías: Lectura
        permisos.add(crearPermiso(Role.ALMACENISTA, Modulo.CATEGORIAS, Accion.VER, true, "Almacenista puede ver categorías"));

        // Dashboard
        permisos.add(crearPermiso(Role.ALMACENISTA, Modulo.DASHBOARD, Accion.VER, true, "Almacenista puede ver dashboard"));

        return permisos;
    }

    private List<Permisos> crearPermisosComprador() {
        List<Permisos> permisos = new ArrayList<>();

        // Compras: CRUD completo
        for (Accion accion : Arrays.asList(Accion.VER, Accion.CREAR, Accion.EDITAR, Accion.APROBAR)) {
            permisos.add(crearPermiso(Role.COMPRADOR, Modulo.COMPRAS, accion, true, "Comprador gestiona compras"));
        }

        // Proveedores: CRUD
        for (Accion accion : Arrays.asList(Accion.VER, Accion.CREAR, Accion.EDITAR)) {
            permisos.add(crearPermiso(Role.COMPRADOR, Modulo.PROVEEDORES, accion, true, "Comprador gestiona proveedores"));
        }

        // Productos: Lectura
        permisos.add(crearPermiso(Role.COMPRADOR, Modulo.PRODUCTOS, Accion.VER, true, "Comprador puede ver productos"));

        // Precios: Lectura
        permisos.add(crearPermiso(Role.COMPRADOR, Modulo.PRECIOS, Accion.VER, true, "Comprador puede ver precios"));

        // Dashboard
        permisos.add(crearPermiso(Role.COMPRADOR, Modulo.DASHBOARD, Accion.VER, true, "Comprador puede ver dashboard"));

        return permisos;
    }

    private Permisos crearPermiso(Role rol, Modulo modulo, Accion accion, boolean permitido, String descripcion) {
        return Permisos.builder()
                .rol(rol)
                .modulo(modulo)
                .accion(accion)
                .permitido(permitido)
                .descripcion(descripcion)
                .build();
    }
}

