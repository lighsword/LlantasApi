package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.Compras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para gestionar operaciones de base de datos de Compras
 * Permite gestionar las compras realizadas a proveedores
 */
@Repository
public interface ComprasRepository extends JpaRepository<Compras, Integer> {

    /**
     * Busca todas las compras realizadas a un proveedor específico
     * @param proveedorId ID del proveedor
     * @return Lista de compras del proveedor
     */
    List<Compras> findByProveedorId(Integer proveedorId);

    /**
     * Busca todas las compras realizadas por un usuario específico
     * @param usuarioId ID del usuario que registró la compra
     * @return Lista de compras del usuario
     */
    List<Compras> findByUsuarioId(Integer usuarioId);

    /**
     * Busca compras en un rango de fechas
     * @param inicio Fecha de inicio
     * @param fin Fecha de fin
     * @return Lista de compras en el rango de fechas
     */
    List<Compras> findByFechaCompraBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Obtiene el total de compras en un periodo
     * @param inicio Fecha de inicio
     * @param fin Fecha de fin
     * @return Suma total de las compras
     */
    @Query("SELECT SUM(c.total) FROM Compras c WHERE c.fechaCompra BETWEEN :inicio AND :fin")
    Double calcularTotalComprasPeriodo(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Obtiene las últimas compras ordenadas por fecha descendente
     * @return Lista de compras recientes
     */
    List<Compras> findTop10ByOrderByFechaCompraDesc();
}

