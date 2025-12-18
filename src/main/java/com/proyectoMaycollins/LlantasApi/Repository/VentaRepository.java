package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByCliente_ClienteId(Long clienteId);
    List<Venta> findByUsuario_Id(Long usuarioId);
    List<Venta> findByEstado(com.proyectoMaycollins.LlantasApi.Model.EstadoVenta estado);
}