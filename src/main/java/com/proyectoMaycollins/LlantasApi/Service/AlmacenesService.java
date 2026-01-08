package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.Almacenes;
import com.proyectoMaycollins.LlantasApi.Repository.AlmacenesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AlmacenesService {
    private final AlmacenesRepository almacenesRepository;

    public AlmacenesService(AlmacenesRepository almacenesRepository) {
        this.almacenesRepository = almacenesRepository;
    }

    public List<Almacenes> list() { return almacenesRepository.findAll(); }

    public Optional<Almacenes> get(Integer id) { return almacenesRepository.findById(id); }

    @Transactional
    public Almacenes save(Almacenes a) { return almacenesRepository.save(a); }

    @Transactional
    public boolean delete(Integer id) {
        if (almacenesRepository.existsById(id)) { almacenesRepository.deleteById(id); return true; }
        return false;
    }
}

