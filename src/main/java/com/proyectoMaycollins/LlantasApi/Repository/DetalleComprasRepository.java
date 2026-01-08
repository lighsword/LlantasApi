package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.DetalleCompras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repositorio para gestionar los detalles de las compras
 * Maneja las líneas de productos incluidos en cada compra
 */
@Repository
public interface DetalleComprasRepository extends JpaRepository<DetalleCompras, DetalleCompras.DetalleComprasId> {

    /**
     * Busca todos los detalles de una compra específica
     * @param compraId ID de la compra
     * @return Lista de detalles de la compra
     */
    @Query("SELECT dc FROM DetalleCompras dc WHERE dc.compraId = :compraId")
    List<DetalleCompras> findByCompraId(Integer compraId);

    /**
     * Busca todos los detalles que incluyen un producto específico
     * @param productoId ID del producto
     * @return Lista de detalles con ese producto
     */
    @Query("SELECT dc FROM DetalleCompras dc WHERE dc.productoId = :productoId")
    List<DetalleCompras> findByProductoId(Long productoId);

    /**
     * Elimina todos los detalles de una compra
     * @param compraId ID de la compra
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM DetalleCompras dc WHERE dc.compraId = :compraId")
    void deleteByCompraId(Integer compraId);

    /**
     * Calcula el total de una compra sumando los subtotales de sus detalles
     * @param compraId ID de la compra
     * @return Total de la compra
     */
    @Query("SELECT SUM(dc.subtotal) FROM DetalleCompras dc WHERE dc.compraId = :compraId")
    Double calcularTotalCompra(Integer compraId);
}

