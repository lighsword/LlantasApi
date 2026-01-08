package com.proyectoMaycollins.LlantasApi.Service;

import com.proyectoMaycollins.LlantasApi.Model.Usuarios;
import com.proyectoMaycollins.LlantasApi.Repository.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuariosService {

    private final UsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Usuarios> findAll() {
        return usuariosRepository.findAll();
    }

    public Optional<Usuarios> findById(Long id) {
        return usuariosRepository.findById(id);
    }

    public boolean existsByEmail(String email) {
        return usuariosRepository.existsByEmail(email);
    }

    public Usuarios create(Usuarios usuario, boolean hashPassword) {
        if (hashPassword && usuario.getPassword() != null) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        return usuariosRepository.save(usuario);
    }

    public Optional<Usuarios> update(Long id, Usuarios cambios, boolean hashPassword) {
        return usuariosRepository.findById(id).map(u -> {
            if (cambios.getEmail() != null) u.setEmail(cambios.getEmail());
            if (cambios.getNombre() != null) u.setNombre(cambios.getNombre());
            if (cambios.getPassword() != null) {
                u.setPassword(hashPassword ? passwordEncoder.encode(cambios.getPassword()) : cambios.getPassword());
            }
            if (cambios.getRol() != null) u.setRol(cambios.getRol());
            if (cambios.getActivo() != null) u.setActivo(cambios.getActivo());
            return usuariosRepository.save(u);
        });
    }

    public boolean delete(Long id) {
        return usuariosRepository.findById(id).map(u -> {
            usuariosRepository.delete(u);
            return true;
        }).orElse(false);
    }
}

