package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.Producto;
import com.proyectoMaycollins.LlantasApi.Repository.ProductoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductoService {
    private final ProductoRepository productoRepository;

    public @NonNull Producto crear(Producto producto) {
        log.info("Creando nuevo producto: {}", producto.getNombreProducto());
        return productoRepository.save(producto);
    }

    public @NonNull Producto actualizar(Long id, Producto producto) {
        log.info("Actualizando producto con ID: {}", id);
        producto.setProductoId(id);
        return productoRepository.save(producto);
    }

    public void eliminar(Long id) {
        log.info("Eliminando producto con ID: {}", id);
        productoRepository.deleteById(id);
    }

    public @NonNull Producto encontrarPorId(Long id) {
        log.info("Buscando producto con ID: {}", id);
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    public List<Producto> listarTodos() {
        log.info("Listando todos los productos");
        return productoRepository.findAll();
    }

    public List<Producto> listarProductosActivos() {
        log.info("Listando productos activos");
        return productoRepository.findByActivoTrue();
    }
}