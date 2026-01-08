package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductosRepository extends JpaRepository<Productos, Long> {
    /**
     * Busca productos por código (búsqueda parcial, sin importar mayúsculas)
     */
    List<Productos> findByCodigoProductoContainingIgnoreCase(String codigoProducto);

    /**
     * Busca productos activos
     */
    List<Productos> findByActivoTrue();

    /**
     * Busca productos inactivos (descontinuados)
     */
    List<Productos> findByActivoFalse();
}
