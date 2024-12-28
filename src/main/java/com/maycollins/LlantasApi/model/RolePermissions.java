package com.maycollins.LlantasApi.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RolePermissions {

    public static Map<String, List<String>> getPermissionsByRole(String role) {
        Map<String, Map<String, List<String>>> allPermissions = new HashMap<>();

        // Admin: Todos los permisos
        allPermissions.put("admin", Map.of(
                "ventas", Arrays.asList("crear", "leer", "actualizar", "eliminar"),
                "inventario", Arrays.asList("crear", "leer", "actualizar", "eliminar"),
                "usuarios", Arrays.asList("crear", "leer", "actualizar", "eliminar"),
                "reportes", Arrays.asList("crear", "leer", "exportar")
        ));

        // Vendedor: Permisos limitados
        allPermissions.put("vendedor", Map.of(
                "ventas", Arrays.asList("crear", "leer", "actualizar"),
                "inventario", Arrays.asList("leer"),
                "reportes", Arrays.asList("leer")
        ));

        // Almacenista: Permisos de inventario
        allPermissions.put("almacenista", Map.of(
                "inventario", Arrays.asList("crear", "leer", "actualizar"),
                "reportes", Arrays.asList("leer")
        ));

        // Supervisor: Permisos de supervisión
        allPermissions.put("supervisor", Map.of(
                "ventas", Arrays.asList("leer", "actualizar"),
                "inventario", Arrays.asList("leer", "actualizar"),
                "reportes", Arrays.asList("crear", "leer", "exportar")
        ));

        // Convertir el rol a minúsculas para hacer la comparación insensible a mayúsculas
        String roleLowerCase = role.toLowerCase();

        // Retornar los permisos del rol específico o un mapa vacío si el rol no existe
        return allPermissions.getOrDefault(roleLowerCase, new HashMap<>());
    }
}