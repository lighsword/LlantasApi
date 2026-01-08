package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.Reportes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para gestionar los reportes generados en el sistema
 * Almacena reportes de ventas, inventario, productos, etc.
 */
@Repository
public interface ReportesRepository extends JpaRepository<Reportes, Integer> {

    /**
     * Busca reportes por tipo
     * @param tipoReporte Tipo de reporte (ventas, inventario, productos, etc.)
     * @return Lista de reportes de ese tipo
     */
    List<Reportes> findByTipoReporte(String tipoReporte);

    /**
     * Busca reportes generados en un rango de fechas
     * @param inicio Fecha de inicio
     * @param fin Fecha de fin
     * @return Lista de reportes en ese rango
     */
    List<Reportes> findByFechaGeneracionBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Obtiene los últimos reportes generados
     * @return Lista de los 10 reportes más recientes
     */
    List<Reportes> findTop10ByOrderByFechaGeneracionDesc();

    /**
     * Busca reportes de un tipo específico ordenados por fecha descendente
     * @param tipoReporte Tipo de reporte
     * @return Lista de reportes ordenados
     */
    List<Reportes> findByTipoReporteOrderByFechaGeneracionDesc(String tipoReporte);
}

