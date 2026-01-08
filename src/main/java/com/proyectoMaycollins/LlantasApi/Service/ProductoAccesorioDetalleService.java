package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.ProductoAccesorioDetalle;
import com.proyectoMaycollins.LlantasApi.Repository.ProductoAccesorioDetalleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductoAccesorioDetalleService {

    private final ProductoAccesorioDetalleRepository repository;

    public ProductoAccesorioDetalleService(ProductoAccesorioDetalleRepository repository) {
        this.repository = repository;
    }

    public Optional<ProductoAccesorioDetalle> findByProductoId(Long productoId) {
        return repository.findById(productoId);
    }

    @Transactional
    public ProductoAccesorioDetalle save(ProductoAccesorioDetalle detalle) {
        return repository.save(detalle);
    }

    @Transactional
    public void deleteByProductoId(Long productoId) {
        repository.deleteById(productoId);
    }
}
