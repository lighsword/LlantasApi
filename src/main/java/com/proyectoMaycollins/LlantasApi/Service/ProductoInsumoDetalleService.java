package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.ProductoInsumoDetalle;
import com.proyectoMaycollins.LlantasApi.Repository.ProductoInsumoDetalleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductoInsumoDetalleService {
    private final ProductoInsumoDetalleRepository repository;

    public ProductoInsumoDetalleService(ProductoInsumoDetalleRepository repository) {
        this.repository = repository;
    }

    public Optional<ProductoInsumoDetalle> findByProductoId(Long productoId) {
        return repository.findById(productoId);
    }

    @Transactional
    public ProductoInsumoDetalle save(ProductoInsumoDetalle detalle) {
        return repository.save(detalle);
    }

    @Transactional
    public void deleteByProductoId(Long productoId) {
        repository.deleteById(productoId);
    }
}

