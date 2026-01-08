package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.ProductoHerramientaDetalle;
import com.proyectoMaycollins.LlantasApi.Repository.ProductoHerramientaDetalleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class ProductoHerramientaDetalleService {
    private final ProductoHerramientaDetalleRepository repository;

    public ProductoHerramientaDetalleService(ProductoHerramientaDetalleRepository repository) {
        this.repository = repository;
    }

    public Optional<ProductoHerramientaDetalle> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public ProductoHerramientaDetalle create(ProductoHerramientaDetalle detalle) {
        return repository.save(detalle);
    }

    @Transactional
    public Optional<ProductoHerramientaDetalle> update(Long id, ProductoHerramientaDetalle cambios) {
        return repository.findById(id).map(existing -> {
            existing.setTipoUso(cambios.getTipoUso());
            return repository.save(existing);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
