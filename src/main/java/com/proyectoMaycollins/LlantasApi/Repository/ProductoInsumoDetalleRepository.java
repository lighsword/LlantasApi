package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.ProductoInsumoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoInsumoDetalleRepository extends JpaRepository<ProductoInsumoDetalle, Long> {
}

