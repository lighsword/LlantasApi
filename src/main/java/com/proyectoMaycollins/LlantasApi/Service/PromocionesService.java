package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.Promociones;
import com.proyectoMaycollins.LlantasApi.Repository.PromocionesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PromocionesService {
    private final PromocionesRepository promocionesRepository;

    public PromocionesService(PromocionesRepository promocionesRepository) {
        this.promocionesRepository = promocionesRepository;
    }

    public List<Promociones> list() { return promocionesRepository.findAll(); }
    public List<Promociones> activas() { return promocionesRepository.findByActivaTrue(); }
    public Optional<Promociones> get(Long id) { return promocionesRepository.findById(id); }

    @Transactional
    public Promociones save(Promociones p) { return promocionesRepository.save(p); }

    @Transactional
    public boolean delete(Long id) {
        if (promocionesRepository.existsById(id)) { promocionesRepository.deleteById(id); return true; }
        return false;
    }
}

