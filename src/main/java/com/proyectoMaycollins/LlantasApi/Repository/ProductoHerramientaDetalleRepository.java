package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.ProductoHerramientaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoHerramientaDetalleRepository extends JpaRepository<ProductoHerramientaDetalle, Long> {
}
