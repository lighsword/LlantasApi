package com.proyectoMaycollins.LlantasApi.Repository;

import com.proyectoMaycollins.LlantasApi.Model.Clientes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientesRepository extends JpaRepository<Clientes, Long> {
    List<Clientes> findByActivoTrue();
    Optional<Clientes> findByEmail(String email);
    Optional<Clientes> findByDocumentoIdentidad(String documentoIdentidad);

    /**
     * Busca los clientes ordenados por total de compras descendente
     * Útil para generar reportes de mejores clientes
     */
    List<Clientes> findAllByOrderByTotalComprasDesc();

    /**
     * Método auxiliar que devuelve los top clientes
     */
    default List<Clientes> findTopClientesByTotalCompras() {
        return findAllByOrderByTotalComprasDesc();
    }
}

