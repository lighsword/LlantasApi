package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.Productos;
import com.proyectoMaycollins.LlantasApi.Repository.ProductosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductosService {
    private final ProductosRepository productosRepository;

    public ProductosService(ProductosRepository productosRepository) {
        this.productosRepository = productosRepository;
    }

    public List<Productos> findAll() {
        return productosRepository.findAll();
    }

    public Optional<Productos> findById(Long id) {
        return productosRepository.findById(id);
    }

    public List<Productos> searchByNombre(String codigo) {
        return productosRepository.findByCodigoProductoContainingIgnoreCase(codigo);
    }

    public List<Productos> findActivos() {
        return productosRepository.findByActivoTrue();
    }

    @Transactional
    public Productos create(Productos producto) {
        return productosRepository.save(producto);
    }

    @Transactional
    public Optional<Productos> update(Long id, Productos cambios) {
        return productosRepository.findById(id).map(existing -> {
            existing.setCodigoProducto(cambios.getCodigoProducto());
            existing.setNombre(cambios.getNombre());
            existing.setDescripcion(cambios.getDescripcion());
            existing.setImagenUrl(cambios.getImagenUrl());
            existing.setPrecioVenta(cambios.getPrecioVenta());
            existing.setPrecioMayorista(cambios.getPrecioMayorista());
            existing.setPrecioCompra(cambios.getPrecioCompra());
            existing.setActivo(cambios.getActivo());
            existing.setCategoriaId(cambios.getCategoriaId());
            existing.setMarca(cambios.getMarca());
            existing.setModelo(cambios.getModelo());
            existing.setStockActual(cambios.getStockActual());
            existing.setStockMinimo(cambios.getStockMinimo());
            return productosRepository.save(existing);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        if (productosRepository.existsById(id)) {
            productosRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
