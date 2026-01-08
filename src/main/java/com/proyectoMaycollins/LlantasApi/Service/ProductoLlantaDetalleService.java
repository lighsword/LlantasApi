package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.ProductoLlantaDetalle;
import com.proyectoMaycollins.LlantasApi.Repository.ProductoLlantaDetalleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class ProductoLlantaDetalleService {
    private final ProductoLlantaDetalleRepository repository;

    public ProductoLlantaDetalleService(ProductoLlantaDetalleRepository repository) {
        this.repository = repository;
    }

    public Optional<ProductoLlantaDetalle> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public ProductoLlantaDetalle create(ProductoLlantaDetalle detalle) {
        return repository.save(detalle);
    }

    @Transactional
    public Optional<ProductoLlantaDetalle> update(Long id, ProductoLlantaDetalle cambios) {
        return repository.findById(id).map(existing -> {
            existing.setMedidaLlanta(cambios.getMedidaLlanta());
            existing.setIndiceCarga(cambios.getIndiceCarga());
            existing.setIndiceVelocidad(cambios.getIndiceVelocidad());
            existing.setTipoTerreno(cambios.getTipoTerreno());
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
