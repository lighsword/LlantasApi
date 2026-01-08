package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.MovimientosInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface MovimientosInventarioRepository extends JpaRepository<MovimientosInventario, Long> {

    // Buscar por almacén
    List<MovimientosInventario> findByAlmacenIdOrderByFechaMovimientoDesc(Integer almacenId);

    // Buscar por producto
    List<MovimientosInventario> findByProductoIdOrderByFechaMovimientoDesc(Long productoId);

    // Buscar por almacén y producto
    List<MovimientosInventario> findByAlmacenIdAndProductoIdOrderByFechaMovimientoDesc(
            Integer almacenId, Long productoId);

    // Buscar por tipo de movimiento
    List<MovimientosInventario> findByTipoMovimientoOrderByFechaMovimientoDesc(String tipoMovimiento);

    // Buscar por rango de fechas
    @Query("SELECT m FROM MovimientosInventario m WHERE m.fechaMovimiento BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fechaMovimiento DESC")
    List<MovimientosInventario> findByFechaMovimientoBetween(
            @Param("fechaInicio") OffsetDateTime fechaInicio,
            @Param("fechaFin") OffsetDateTime fechaFin);

    // Buscar por almacén y rango de fechas
    @Query("SELECT m FROM MovimientosInventario m WHERE m.almacenId = :almacenId AND m.fechaMovimiento BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fechaMovimiento DESC")
    List<MovimientosInventario> findByAlmacenIdAndFechaMovimientoBetween(
            @Param("almacenId") Integer almacenId,
            @Param("fechaInicio") OffsetDateTime fechaInicio,
            @Param("fechaFin") OffsetDateTime fechaFin);

    // Contar entradas por almacén
    @Query("SELECT COUNT(m) FROM MovimientosInventario m WHERE m.almacenId = :almacenId AND m.tipoMovimiento = 'ENTRADA'")
    Long countEntradasByAlmacen(@Param("almacenId") Integer almacenId);

    // Contar salidas por almacén
    @Query("SELECT COUNT(m) FROM MovimientosInventario m WHERE m.almacenId = :almacenId AND m.tipoMovimiento = 'SALIDA'")
    Long countSalidasByAlmacen(@Param("almacenId") Integer almacenId);

    // Sumar entradas por almacén
    @Query("SELECT COALESCE(SUM(m.cantidad), 0) FROM MovimientosInventario m WHERE m.almacenId = :almacenId AND m.tipoMovimiento = 'ENTRADA'")
    Integer sumEntradasByAlmacen(@Param("almacenId") Integer almacenId);

    // Sumar salidas por almacén
    @Query("SELECT COALESCE(SUM(m.cantidad), 0) FROM MovimientosInventario m WHERE m.almacenId = :almacenId AND m.tipoMovimiento = 'SALIDA'")
    Integer sumSalidasByAlmacen(@Param("almacenId") Integer almacenId);

    // Último movimiento por producto en almacén
    @Query("SELECT m FROM MovimientosInventario m WHERE m.almacenId = :almacenId AND m.productoId = :productoId ORDER BY m.fechaMovimiento DESC LIMIT 1")
    MovimientosInventario findLastMovimientoByAlmacenAndProducto(
            @Param("almacenId") Integer almacenId,
            @Param("productoId") Long productoId);

    // Movimientos por usuario
    List<MovimientosInventario> findByUsuarioIdOrderByFechaMovimientoDesc(Integer usuarioId);
}

