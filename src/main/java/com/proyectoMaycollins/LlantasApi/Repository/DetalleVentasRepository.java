package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.DetalleVentas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVentasRepository extends JpaRepository<DetalleVentas, Long> {
    List<DetalleVentas> findByVentaId(Long ventaId);

    /**
     * Obtiene los productos más vendidos agrupando por producto
     * Retorna una lista con productos ordenados por cantidad vendida
     */
    @Query("SELECT dv.producto, SUM(dv.cantidad) as totalVendido " +
           "FROM DetalleVentas dv " +
           "GROUP BY dv.producto " +
           "ORDER BY totalVendido DESC")
    List<Object[]> findProductosMasVendidos();
}

