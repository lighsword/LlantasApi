package com.proyectoMaycollins.LlantasApi.Repository;


import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.proyectoMaycollins.LlantasApi.Model.Promociones;

@Repository
public interface PromocionesRepository extends JpaRepository<Promociones, Long> {
    List<Promociones> findByActivaTrue();
}