package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.Categorias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriasRepository extends JpaRepository<Categorias, Long> {
    Optional<Categorias> findByNombre(String nombre);
}

