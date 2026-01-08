package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para gestionar el inventario de productos en almacenes
 */
@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Inventario.InventarioId> {

    /**
     * Busca registros de inventario por almacén
     */
    List<Inventario> findByAlmacenId(Integer almacenId);

    /**
     * Busca registros de inventario por producto
     */
    List<Inventario> findByProductoId(Integer productoId);

    /**
     * Busca registros de inventario por producto, ordenados por cantidad descendente
     * Útil para descontar stock del almacén con más disponibilidad primero
     */
    List<Inventario> findByProductoIdOrderByCantidadDesc(Long productoId);

    /**
     * Calcula el stock total de un producto sumando las cantidades de todos los almacenes
     * @param productoId ID del producto
     * @return Stock total disponible en todos los almacenes
     */
    @Query("SELECT SUM(i.cantidad) FROM Inventario i WHERE i.productoId = :productoId")
    Integer calcularStockTotalPorProducto(Long productoId);

    /**
     * Busca productos con stock total menor al mínimo especificado
     * Agrupa por producto y suma las cantidades de todos los almacenes
     */
    @Query("SELECT i.productoId, SUM(i.cantidad) as stockTotal " +
           "FROM Inventario i " +
           "GROUP BY i.productoId " +
           "HAVING SUM(i.cantidad) < :stockMinimo")
    List<Object[]> findProductosConStockBajo(Integer stockMinimo);
}



