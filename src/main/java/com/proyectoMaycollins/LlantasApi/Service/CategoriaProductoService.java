package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.DTO.CategoriaProductoDTO;
import com.proyectoMaycollins.LlantasApi.Model.CategoriaProducto;
import com.proyectoMaycollins.LlantasApi.Repository.CategoriaProductoRepository;
import com.proyectoMaycollins.LlantasApi.exceptions.DuplicateResourceException;
import com.proyectoMaycollins.LlantasApi.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
public class CategoriaProductoService {
    private final CategoriaProductoRepository categoriaProductoRepository;

    @Autowired
    public CategoriaProductoService(CategoriaProductoRepository categoriaProductoRepository) {
        this.categoriaProductoRepository = categoriaProductoRepository;
    }

    public @NonNull CategoriaProducto crear(CategoriaProductoDTO dto) {
        validarCategoria(dto);
        CategoriaProducto categoria = new CategoriaProducto();
        BeanUtils.copyProperties(dto, categoria);
        return categoriaProductoRepository.save(categoria);
    }

    public @NonNull CategoriaProducto actualizar(@NonNull Long id, @NonNull CategoriaProductoDTO dto) {
        CategoriaProducto categoria = encontrarPorId(id);
        BeanUtils.copyProperties(dto, categoria);
        return categoriaProductoRepository.save(categoria);
    }

    public void eliminar(@NonNull Long id) {
        CategoriaProducto categoria = encontrarPorId(id);
        categoriaProductoRepository.delete(categoria);
    }

    public @NonNull CategoriaProducto encontrarPorId(@NonNull Long id) {
        return categoriaProductoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
    }

    public List<CategoriaProducto> listarTodos() {
        return categoriaProductoRepository.findAll();
    }

    private void validarCategoria(CategoriaProductoDTO dto) {
        if (categoriaProductoRepository.existsByNombreCategoria(dto.getNombreCategoria())) {
            throw new DuplicateResourceException("Ya existe una categoría con el nombre: " + dto.getNombreCategoria());
        }
    }
}