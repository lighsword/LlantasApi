package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.Precios;
import com.proyectoMaycollins.LlantasApi.Model.enums.TipoPrecio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreciosRepository extends JpaRepository<Precios, Long> {

    /**
     * Buscar todos los precios de un producto
     */
    List<Precios> findByProductoId(Long productoId);

    /**
     * Buscar todos los precios activos de un producto
     */
    List<Precios> findByProductoIdAndActivoTrue(Long productoId);

    /**
     * Buscar precio activo de un producto por tipo específico
     */
    Optional<Precios> findByProductoIdAndTipoAndActivoTrue(Long productoId, TipoPrecio tipo);

    /**
     * Buscar todos los precios por tipo
     */
    List<Precios> findByTipoAndActivoTrue(TipoPrecio tipo);

    /**
     * Desactivar todos los precios anteriores de un producto para un tipo específico
     */
    @Modifying
    @Query("UPDATE Precios p SET p.activo = false WHERE p.productoId = :productoId AND p.tipo = :tipo AND p.activo = true")
    void desactivarPreciosAnteriores(@Param("productoId") Long productoId, @Param("tipo") TipoPrecio tipo);

    /**
     * Verificar si existe un precio activo para un producto y tipo
     */
    boolean existsByProductoIdAndTipoAndActivoTrue(Long productoId, TipoPrecio tipo);

    /**
     * Contar precios activos por producto
     */
    @Query("SELECT COUNT(p) FROM Precios p WHERE p.productoId = :productoId AND p.activo = true")
    long contarPreciosActivosPorProducto(@Param("productoId") Long productoId);

    /**
     * Obtener historial de precios de un producto por tipo
     */
    @Query("SELECT p FROM Precios p WHERE p.productoId = :productoId AND p.tipo = :tipo ORDER BY p.fechaInicio DESC")
    List<Precios> obtenerHistorialPrecios(@Param("productoId") Long productoId, @Param("tipo") TipoPrecio tipo);
}

