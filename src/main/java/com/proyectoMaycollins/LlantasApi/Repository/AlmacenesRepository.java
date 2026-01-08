package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.Almacenes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlmacenesRepository extends JpaRepository<Almacenes, Integer> {
}

