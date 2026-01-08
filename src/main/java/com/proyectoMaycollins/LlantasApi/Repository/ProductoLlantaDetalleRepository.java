package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.ProductoLlantaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoLlantaDetalleRepository extends JpaRepository<ProductoLlantaDetalle, Long> {
}
