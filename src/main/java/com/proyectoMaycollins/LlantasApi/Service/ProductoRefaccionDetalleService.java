package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.ProductoRefaccionDetalle;
import com.proyectoMaycollins.LlantasApi.Repository.ProductoRefaccionDetalleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class ProductoRefaccionDetalleService {
    private final ProductoRefaccionDetalleRepository repository;

    public ProductoRefaccionDetalleService(ProductoRefaccionDetalleRepository repository) {
        this.repository = repository;
    }

    public Optional<ProductoRefaccionDetalle> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public ProductoRefaccionDetalle create(ProductoRefaccionDetalle detalle) {
        return repository.save(detalle);
    }

    @Transactional
    public Optional<ProductoRefaccionDetalle> update(Long id, ProductoRefaccionDetalle cambios) {
        return repository.findById(id).map(existing -> {
            existing.setNumeroParte(cambios.getNumeroParte());
            existing.setSistemaAsociado(cambios.getSistemaAsociado());
            existing.setVehiculoAplicable(cambios.getVehiculoAplicable());
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
