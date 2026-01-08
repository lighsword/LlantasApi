package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.Proveedores;
import com.proyectoMaycollins.LlantasApi.Repository.ProveedoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar la lógica de negocio de Proveedores
 * Maneja operaciones CRUD y validaciones de proveedores
 */
@Service
public class ProveedoresService {

    @Autowired
    private ProveedoresRepository proveedoresRepository;

    /**
     * Obtiene todos los proveedores registrados
     * @return Lista de todos los proveedores
     */
    public List<Proveedores> obtenerTodos() {
        return proveedoresRepository.findAll();
    }

    /**
     * Busca un proveedor por su ID
     * @param id ID del proveedor
     * @return Optional con el proveedor si existe
     */
    public Optional<Proveedores> obtenerPorId(Integer id) {
        return proveedoresRepository.findById(id);
    }

    /**
     * Busca un proveedor por su email
     * @param email Email del proveedor
     * @return Optional con el proveedor si existe
     */
    public Optional<Proveedores> obtenerPorEmail(String email) {
        return proveedoresRepository.findByEmail(email);
    }

    /**
     * Busca un proveedor por su nombre
     * @param nombre Nombre del proveedor
     * @return Optional con el proveedor si existe
     */
    public Optional<Proveedores> obtenerPorNombre(String nombre) {
        return proveedoresRepository.findByNombre(nombre);
    }

    /**
     * Crea un nuevo proveedor
     * Valida que el email no esté duplicado
     * @param proveedor Datos del proveedor a crear
     * @return Proveedor creado
     * @throws RuntimeException si el email ya existe
     */
    @Transactional
    public Proveedores crear(Proveedores proveedor) {
        // Validar que el email no esté duplicado
        if (proveedor.getEmail() != null &&
            proveedoresRepository.existsByEmail(proveedor.getEmail())) {
            throw new RuntimeException("Ya existe un proveedor con el email: " + proveedor.getEmail());
        }

        // Guardar el proveedor
        return proveedoresRepository.save(proveedor);
    }

    /**
     * Actualiza un proveedor existente
     * Valida que el proveedor exista y que el email no esté duplicado
     * @param id ID del proveedor a actualizar
     * @param proveedorActualizado Datos actualizados
     * @return Proveedor actualizado
     * @throws RuntimeException si el proveedor no existe o el email está duplicado
     */
    @Transactional
    public Proveedores actualizar(Integer id, Proveedores proveedorActualizado) {
        // Verificar que el proveedor existe
        Proveedores proveedorExistente = proveedoresRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + id));

        // Validar email duplicado si se está cambiando
        if (proveedorActualizado.getEmail() != null &&
            !proveedorActualizado.getEmail().equals(proveedorExistente.getEmail())) {
            if (proveedoresRepository.existsByEmail(proveedorActualizado.getEmail())) {
                throw new RuntimeException("Ya existe un proveedor con el email: " + proveedorActualizado.getEmail());
            }
        }

        // Actualizar campos
        if (proveedorActualizado.getNombre() != null) {
            proveedorExistente.setNombre(proveedorActualizado.getNombre());
        }
        if (proveedorActualizado.getContacto() != null) {
            proveedorExistente.setContacto(proveedorActualizado.getContacto());
        }
        if (proveedorActualizado.getTelefono() != null) {
            proveedorExistente.setTelefono(proveedorActualizado.getTelefono());
        }
        if (proveedorActualizado.getEmail() != null) {
            proveedorExistente.setEmail(proveedorActualizado.getEmail());
        }
        if (proveedorActualizado.getDireccion() != null) {
            proveedorExistente.setDireccion(proveedorActualizado.getDireccion());
        }

        // Guardar cambios
        return proveedoresRepository.save(proveedorExistente);
    }

    /**
     * Elimina un proveedor por su ID
     * Verifica que el proveedor exista antes de eliminarlo
     * @param id ID del proveedor a eliminar
     * @throws RuntimeException si el proveedor no existe
     */
    @Transactional
    public void eliminar(Integer id) {
        // Verificar que el proveedor existe
        if (!proveedoresRepository.existsById(id)) {
            throw new RuntimeException("Proveedor no encontrado con ID: " + id);
        }

        // Eliminar el proveedor
        proveedoresRepository.deleteById(id);
    }

    /**
     * Verifica si existe un proveedor con el email dado
     * @param email Email a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existePorEmail(String email) {
        return proveedoresRepository.existsByEmail(email);
    }
}

