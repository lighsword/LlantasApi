package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.Clientes;
import com.proyectoMaycollins.LlantasApi.Repository.ClientesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientesService {
    private final ClientesRepository clientesRepository;

    public ClientesService(ClientesRepository clientesRepository) {
        this.clientesRepository = clientesRepository;
    }

    public List<Clientes> findAll() {
        return clientesRepository.findAll();
    }

    public Optional<Clientes> findById(Long id) {
        return clientesRepository.findById(id);
    }

    public List<Clientes> findActivos() {
        return clientesRepository.findByActivoTrue();
    }

    public Optional<Clientes> findByEmail(String email) {
        return clientesRepository.findByEmail(email);
    }

    public Optional<Clientes> findByDocumentoIdentidad(String documentoIdentidad) {
        return clientesRepository.findByDocumentoIdentidad(documentoIdentidad);
    }

    @Transactional
    public Clientes create(Clientes cliente) {
        return clientesRepository.save(cliente);
    }

    @Transactional
    public Optional<Clientes> update(Long id, Clientes cambios) {
        return clientesRepository.findById(id).map(existing -> {
            existing.setNombreCliente(cambios.getNombreCliente());
            existing.setEmail(cambios.getEmail());
            existing.setTelefonoCliente(cambios.getTelefonoCliente());
            existing.setDireccion(cambios.getDireccion());
            existing.setDocumentoIdentidad(cambios.getDocumentoIdentidad());
            existing.setTipoCliente(cambios.getTipoCliente());
            existing.setNivelCliente(cambios.getNivelCliente());
            existing.setTotalCompras(cambios.getTotalCompras());
            existing.setActivo(cambios.getActivo());
            return clientesRepository.save(existing);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        if (clientesRepository.existsById(id)) {
            clientesRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

