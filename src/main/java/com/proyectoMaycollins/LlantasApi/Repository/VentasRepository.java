package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.Ventas;
import com.proyectoMaycollins.LlantasApi.Model.enums.EstadoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentasRepository extends JpaRepository<Ventas, Long> {
    List<Ventas> findByClienteId(Long clienteId);
    List<Ventas> findByEstado(EstadoVenta estado);

    /**
     * Busca ventas en un rango de fechas
     * Útil para reportes de ventas por periodo
     */
    List<Ventas> findByFechaVentaBetween(LocalDateTime inicio, LocalDateTime fin);
}
