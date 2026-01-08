package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.Categorias;
import com.proyectoMaycollins.LlantasApi.Repository.CategoriasRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriasService {
    private final CategoriasRepository categoriasRepository;

    public CategoriasService(CategoriasRepository categoriasRepository) {
        this.categoriasRepository = categoriasRepository;
    }

    public List<Categorias> findAll() { return categoriasRepository.findAll(); }

    public Optional<Categorias> findById(Long id) { return categoriasRepository.findById(id); }

    public Optional<Categorias> findByNombre(String nombre) { return categoriasRepository.findByNombre(nombre); }

    @Transactional
    public Categorias create(Categorias categoria) { return categoriasRepository.save(categoria); }

    @Transactional
    public Optional<Categorias> update(Long id, Categorias cambios) {
        return categoriasRepository.findById(id).map(existing -> {
            existing.setNombre(cambios.getNombre());
            existing.setDescripcion(cambios.getDescripcion());
            existing.setActivo(cambios.getActivo());
            return categoriasRepository.save(existing);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        if (categoriasRepository.existsById(id)) {
            categoriasRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

