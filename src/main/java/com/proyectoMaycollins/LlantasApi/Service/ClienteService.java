package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.Cliente;
import com.proyectoMaycollins.LlantasApi.Repository.ClienteRepository;
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
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public @NonNull Cliente crear(Cliente cliente) {
        log.info("Creando nuevo cliente: {}", cliente.getNombre());
        return clienteRepository.save(cliente);
    }

    public @NonNull Cliente actualizar(Long id, Cliente cliente) {
        log.info("Actualizando cliente con ID: {}", id);
        cliente.setClienteId(id);
        return clienteRepository.save(cliente);
    }

    public void eliminar(Long id) {
        log.info("Eliminando cliente con ID: {}", id);
        clienteRepository.deleteById(id);
    }

    public @NonNull Cliente encontrarPorId(Long id) {
        log.info("Buscando cliente con ID: {}", id);
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
    }

    public List<Cliente> listarTodos() {
        log.info("Listando todos los clientes");
        return clienteRepository.findAll();
    }

    public Cliente findByDocumentoIdentidad(String documentoIdentidad) {
        log.info("Buscando cliente con documento: {}", documentoIdentidad);
        return clienteRepository.findByDocumentoIdentidad(documentoIdentidad)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con documento: " + documentoIdentidad));
    }
}