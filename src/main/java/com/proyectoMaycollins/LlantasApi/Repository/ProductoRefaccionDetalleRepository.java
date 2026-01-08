package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.ProductoRefaccionDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRefaccionDetalleRepository extends JpaRepository<ProductoRefaccionDetalle, Long> {
}
