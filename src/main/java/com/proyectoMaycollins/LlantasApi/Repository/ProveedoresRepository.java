package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.Proveedores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para gestionar operaciones de base de datos de Proveedores
 * Extiende JpaRepository para tener acceso a métodos CRUD básicos
 */
@Repository
public interface ProveedoresRepository extends JpaRepository<Proveedores, Integer> {

    /**
     * Busca un proveedor por su email
     * @param email Email del proveedor
     * @return Optional con el proveedor si existe
     */
    Optional<Proveedores> findByEmail(String email);

    /**
     * Busca un proveedor por su nombre (búsqueda exacta)
     * @param nombre Nombre del proveedor
     * @return Optional con el proveedor si existe
     */
    Optional<Proveedores> findByNombre(String nombre);

    /**
     * Verifica si existe un proveedor con el email dado
     * @param email Email a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);
}

