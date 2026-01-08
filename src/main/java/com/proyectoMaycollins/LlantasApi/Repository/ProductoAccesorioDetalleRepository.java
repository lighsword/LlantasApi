package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.ProductoAccesorioDetalle;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.proyectoMaycollins.LlantasApi.Model.ProductoAccesorioDetalle;

@Repository
public interface ProductoAccesorioDetalleRepository extends JpaRepository<ProductoAccesorioDetalle, Long> {

}


