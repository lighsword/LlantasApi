package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.Inventario;
import com.proyectoMaycollins.LlantasApi.Repository.InventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {
    private final InventarioRepository inventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    public List<Inventario> listAll() {
        return inventarioRepository.findAll();
    }

    public List<Inventario> listByAlmacen(Integer almacenId) {
        return inventarioRepository.findByAlmacenId(almacenId);
    }

    public List<Inventario> listByProducto(Integer productoId) {
        return inventarioRepository.findByProductoId(productoId);
    }

    public Optional<Inventario> get(Integer productoId, Integer almacenId) {
        Inventario.InventarioId id = new Inventario.InventarioId();
        id.setProductoId(productoId);
        id.setAlmacenId(almacenId);
        return inventarioRepository.findById(id);
    }

    @Transactional
    public Inventario upsert(Inventario inv) {
        inv.setFechaActualizacion(OffsetDateTime.now());
        return inventarioRepository.save(inv);
    }

    @Transactional
    public void delete(Integer productoId, Integer almacenId) {
        Inventario.InventarioId id = new Inventario.InventarioId();
        id.setProductoId(productoId);
        id.setAlmacenId(almacenId);
        inventarioRepository.deleteById(id);
    }
}

