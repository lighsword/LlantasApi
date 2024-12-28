package com.maycollins.LlantasApi.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RolePermissions {

    public static Map<String, Map<String, List<String>>> getPermissionsByRole() {
        Map<String, Map<String, List<String>>> permissions = new HashMap<>();

        // Admin: Todos los permisos
        permissions.put("admin", Map.of(
                "sales", Arrays.asList("create", "read", "update", "delete"),
                "inventory", Arrays.asList("create", "read", "update", "delete"),
                "users", Arrays.asList("create", "read", "update", "delete")
        ));

        // Vendedor: Permisos limitados
        permissions.put("seller", Map.of(
                "sales", Arrays.asList("create", "read", "update"),
                "inventory", Arrays.asList("read")
        ));

        // Almacenista: Permisos de inventario
        permissions.put("warehouse", Map.of(
                "inventory", Arrays.asList("create", "read", "update")
        ));

        // Supervisor: Permisos de ventas e inventario
        permissions.put("supervisor", Map.of(
                "sales", Arrays.asList("create", "read", "update", "delete"),
                "inventory", Arrays.asList("create", "read", "update", "delete")
        ));

        return permissions;
    }
}