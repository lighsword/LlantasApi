package com.proyectoMaycollins.LlantasApi.Controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Tests para Endpoints de Enums y Categorías")
public class EnumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ==================== TESTS PARA ENUM CONTROLLER ====================

    @Test
    @DisplayName("GET /api/enums - Debe retornar todos los enums del sistema")
    public void testGetAllEnums() throws Exception {
        mockMvc.perform(get("/api/enums")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categorias", notNullValue()))
                .andExpect(jsonPath("$.roles", notNullValue()))
                .andExpect(jsonPath("$.estadosVenta", notNullValue())
                );
    }

    @Test
    @DisplayName("GET /api/enums/categorias - Debe retornar solo las categorías en formato array")
    public void testGetCategoriasEnum() throws Exception {
        mockMvc.perform(get("/api/enums/categorias")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)))
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(5)))
                .andExpect(jsonPath("$[0]", is("LLANTAS")))
                .andExpect(jsonPath("$[1]", is("HERRAMIENTAS")))
                .andExpect(jsonPath("$[2]", is("REFACCIONES")))
                .andExpect(jsonPath("$[3]", is("ACCESORIOS")))
                .andExpect(jsonPath("$[4]", is("INSUMOS")));
    }

    @Test
    @DisplayName("GET /api/enums/categorias - Verificar que todos los valores están en MAYÚSCULAS")
    public void testCategoriasEnMayusculas() throws Exception {
        mockMvc.perform(get("/api/enums/categorias")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", everyItem(matchesRegex("^[A-Z_]+$"))));
    }

    @Test
    @DisplayName("GET /api/enums/roles - Debe retornar los roles disponibles")
    public void testGetRolesEnum() throws Exception {
        mockMvc.perform(get("/api/enums/roles")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)))
                .andExpect(jsonPath("$.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("GET /api/enums/estados-venta - Debe retornar los estados de venta")
    public void testGetEstadosVentaEnum() throws Exception {
        mockMvc.perform(get("/api/enums/estados-venta")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)))
                .andExpect(jsonPath("$.length()", greaterThan(0)));
    }

    // ==================== TESTS PARA CATEGORIAS CONTROLLER ====================

    @Test
    @DisplayName("GET /api/categorias - Debe retornar lista de categorías de la BD")
    public void testGetAllCategoriasFromDB() throws Exception {
        mockMvc.perform(get("/api/categorias")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    // ==================== TESTS COMPARATIVOS ====================

    @Test
    @DisplayName("COMPARACIÓN: /api/enums/categorias retorna array de strings vs /api/categorias retorna array de objetos")
    public void testDifferencesBetweenEndpoints() throws Exception {
        // Test /api/enums/categorias - Retorna array de strings
        mockMvc.perform(get("/api/enums/categorias")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]", isA(String.class)))
                .andExpect(content().json("[\"LLANTAS\",\"HERRAMIENTAS\",\"REFACCIONES\",\"ACCESORIOS\",\"INSUMOS\"]"));

        // Test /api/categorias - Retorna array de objetos (si los hay en BD)
        mockMvc.perform(get("/api/categorias")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }

    @Test
    @DisplayName("VERIFICACIÓN: /api/enums/categorias no contiene información sensible de BD")
    public void testEnumNoContainesSensitiveData() throws Exception {
        mockMvc.perform(get("/api/enums/categorias")
                .contentType("application/json"))
                .andExpect(status().isOk())
                // No debe contener "id", "descripcion", "activo" u otros campos de BD
                .andExpect(jsonPath("$[*]", everyItem(not(containsString("id")))))
                .andExpect(jsonPath("$[*]", everyItem(not(containsString("descripcion")))));
    }

    @Test
    @DisplayName("VERIFICACIÓN: Endpoint de enum es seguro sin autenticación")
    public void testEnumEndpointIsPublic() throws Exception {
        mockMvc.perform(get("/api/enums/categorias")
                .contentType("application/json")
                // Sin header Authorization
        )
                .andExpect(status().isOk());
    }
}

